package com.ruoyi.rec.scala

import org.apache.spark.SparkConf
import org.apache.spark.mllib.recommendation.{ALS, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.jblas.DoubleMatrix


case class MeetingRating( userId: Long, meetingId: Long, score: Double, timestamp: Int )
case class MongoConfig( uri: String, db: String )
// 定义推荐对象
case class Recommendation(meetingId: Long, score: Double)
// 定义用户的推荐列表
case class UserRecs(userId: Long, recs: Seq[Recommendation])
// 定义meeting相似度列表
case class MeetingRecs(meetingId: Long, recs: Seq[Recommendation])

object LFM {
  val MONGODB_RATING_COLLECTION = "user_ratings"
  val USER_REC = "user_rec"
  val MEETING_REC = "meeting_rec"
  val USER_MAX_LENGTH = 10
  def main(args: Array[String]): Unit = {
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
        rating => (rating.userId, rating.meetingId, rating.score)
      ).cache()

    // 提取出用户和商品的数据集
    val userRDD = ratingRDD.map(_._1.intValue()).distinct()
    val meetingRDD = ratingRDD.map(_._2.intValue()).distinct()

    // 训练LFM
    val trainData = ratingRDD.map(x => Rating(x._1.intValue(), x._2.intValue(), x._3))
    // 定义模型训练参数
    // rank 隐特征个数 iterations 跌打次数 lambda 正则化系数
    val (rank, iterations, lambda) = (5, 10, 0.01)
    val model = ALS.train(trainData, rank, iterations, lambda)
    // 获得评分矩阵，获得用户的推荐列表
    // 用userRDD和meetingRDD求笛卡尔积，获得空userMeetingRDD
    val userMeetings: RDD[(Int, Int)] = userRDD.cartesian(meetingRDD)
    val preRating = model.predict(userMeetings)

    val userRecs = preRating.filter(_.rating > 0)
      .map(
        rating => (rating.user, ( rating.product, rating.rating ))
      )
      .groupByKey()
      .map {
        case (userId, recs) =>
          // 按照rating降序排列
          UserRecs(userId, recs.toList.sortWith(_._2 > _._2).take(USER_MAX_LENGTH).map(x => Recommendation(x._1, x._2)))
      }
      .toDF()
    userRecs.write
      .option("uri", mongoConfig.uri)
      .option("collection", USER_REC)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()

    // 利用meeting feature计算相似度
    val meetingFeatures = model.productFeatures.map{
      case (meetingId, features) => (meetingId, new DoubleMatrix(features))
    }
    // meeting之间计算cosine相似度
    val meetingRecs = meetingFeatures.cartesian(meetingFeatures)
      .filter{
        case (a, b) => a._1 != b._1
      }
      .map {
        case (a, b) =>
          val simScore = consine(a._2, b._2)
          (a._1, (b._1, simScore))
      }
      .filter(_._2._2 > 0.4)
      .groupByKey()
      .map {
        case (meetingId, recs) =>
          MeetingRecs(meetingId, recs.toList.sortWith(_._2 > _._2).map(x => Recommendation(x._1, x._2)))
      }
      .toDF()

    meetingRecs.write
      .option("uri", mongoConfig.uri)
      .option("collection", MEETING_REC)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()

    spark.stop()
  }

  def consine(meeting1: DoubleMatrix, meeting2: DoubleMatrix):Double = {
    meeting1.dot(meeting2) / (meeting1.norm2() * meeting2.norm2())
  }
}
