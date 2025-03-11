package com.ruoyi.rec.scala

import breeze.numerics.sqrt
import com.ruoyi.rec.scala.LFM.MONGODB_RATING_COLLECTION
import org.apache.spark.SparkConf
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession


object ALSTrain {
  def main(args: Array[String]) = {
    val config = Map(
      "spark.cores" -> "local[*]",
      "mongo.uri" -> "mongodb://root:123456@192.168.31.94:27017/meeting?authSource=admin&authMechanism=SCRAM-SHA-1",
      "mongo.db" -> "meeting"
    );
    // 创建spark config
    val sparkConf = new SparkConf().setMaster(config("spark.cores")).setAppName("LFM");
    // 创建spark session
    val spark = SparkSession.builder().config(sparkConf).getOrCreate();
    import spark.implicits._
    implicit val mongoConfig = MongoConfig(config("mongo.uri"), config("mongo.db"));
    // 加载数据
    val ratingRDD = spark.read
      .option("uri", mongoConfig.uri)
      .option("collection", MONGODB_RATING_COLLECTION)
      .format("com.mongodb.spark.sql")
      .load()
      .as[MeetingRating]
      .rdd
      .map(
        rating => Rating(rating.userId.intValue(), rating.meetingId.intValue(), rating.score)
      ).cache()

    // 数据集切分
    val splits = ratingRDD.randomSplit(Array(0.8, 0.2))
    val trainingRDD = splits(0)
    val testingRDD = splits(1)

    // 输出最优参数
    adjustALSParams( trainingRDD, testingRDD )

    spark.stop()
  }

  def adjustALSParams(trainData: RDD[Rating], testData: RDD[Rating]): Unit = {
    // 遍历数组中定义的参数取值
    val result = for( rank <- Array(20, 30, 50, 100);lambda <- Array(1, 0.1, 0.01))
      yield {
        val model = ALS.train(trainData, rank, 10, lambda)
        val rmse = getRMSE(model, testData)
        (rank, lambda, rmse)
      }
    // 按照rmse排序并输出最优参数
    println(result.minBy(_._3))
  }

  def getRMSE(model: MatrixFactorizationModel, data: RDD[Rating]): Double = {
    val userMeetings = data.map(item => (item.user, item.product))
    val meetingRating = model.predict(userMeetings)
    val observed = data.map( item=> ( (item.user, item.product),  item.rating ) )
    val predict = meetingRating.map( item=> ( (item.user, item.product),  item.rating ) )
    sqrt(
      observed.join(predict).map{
      case ( (userId, productId), (actual, pre) ) =>
        val err = actual - pre
        err * err
    }.mean())
  }
}
