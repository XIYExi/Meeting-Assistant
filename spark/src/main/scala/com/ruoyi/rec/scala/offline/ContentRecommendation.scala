package com.ruoyi.rec.scala.offline

import org.apache.spark.SparkConf
import org.apache.spark.ml.feature.{HashingTF, IDF, Tokenizer}
import org.apache.spark.ml.linalg.SparseVector
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col
import org.jblas.DoubleMatrix


/**
 * 基于内容的离线推荐
 */

// 会议内容类
case class Meeting(meetingId: Long, title: String, category: Integer, meetingType: Integer)
// 评分类
case class MeetingRating( userId: Long, meetingId: Long, score: Double, timestamp: Int )
// mongo配置类
case class MongoConfig( uri: String, db: String )
// 定义推荐对象
case class Recommendation(meetingId: Long, score: Double)
// 定义meeting相似度列表
case class MeetingRecs(meetingId: Long, recs: Seq[Recommendation])


object ContentRecommendation {
  val MONGODB_MEETING_SCHEMA = "meeting"
  val MEETING_REC = "content_meeting_rec"
  val USER_MAX_LENGTH = 10
  def main(args: Array[String]): Unit = {
    val config = Map(
      "spark.cores" -> "local[*]",
      "mongo.uri" -> "mongodb://root:123456@192.168.31.94:27017/meeting?authSource=admin&authMechanism=SCRAM-SHA-1",
      "mongo.db" -> "meeting"
    );

    // 数据库连接信息
    val jdbc_url = "jdbc:mysql://localhost:3306/ry-cloud"
    val jdbc_properties = Map(
      "user" ->  "root",
      "password" ->  "123456",
      "driver" ->  "com.mysql.cj.jdbc.Driver"
    );

    // 创建spark config
    val sparkConf = new SparkConf().setMaster(config("spark.cores")).setAppName("LFM");
    // 创建spark session
    val spark = SparkSession.builder().config(sparkConf).getOrCreate();
    import spark.implicits._
    implicit val mongoConfig = MongoConfig(config("mongo.uri"), config("mongo.db"));
    // 去mysql加载会议数据
    val meetingTypeDF = spark.read
      .format("jdbc")
      .option("url", jdbc_url)
      .option("dbtable", MONGODB_MEETING_SCHEMA)
      .option("numPartitions", "10")
      .option("user", jdbc_properties("user"))
      .option("password", jdbc_properties("password"))
      .option("driver", jdbc_properties("driver"))
      .load()
      .select(
        col("id").as("meetingId"),
        col("title"),
        col("type").as("category"),
        col("meeting_type").as("meetingType")
      )
      .as[Meeting]
      .map{x =>
        var v = "";
        if (x.category == 1)
          v = "科技会议";
        else if(x.category == 2)
          v = "学术会议"
        else if(x.category == 3)
          v = "行业峰会";
        else
          v = "新品发布";
        var k = "";
        if (x.meetingType == 1)
          k = "线下会议";
        else
          k = "线上会议";
        (x.meetingId.intValue(), x.title, v + " " + k)
      }
      .toDF("meetingId", "title", "category")
      .cache()

     // 1. 实例化一个分词器，用来做分词，默认按照空格分
    val tokenizer = new Tokenizer().setInputCol("category").setOutputCol("words")
    // 用分词器做转换，得到增加一个新列words的DF
    val wordsDataDF = tokenizer.transform(meetingTypeDF)

    // 2. 定义一个HashingTF工具，计算频次
    val hashingTF = new HashingTF().setInputCol("words").setOutputCol("rawFeatures").setNumFeatures(800)
    val featurizedDataDF = hashingTF.transform(wordsDataDF)

    // 3. 定义一个IDF工具，计算TF-IDF
    val idf = new IDF().setInputCol("rawFeatures").setOutputCol("features")
    // 训练一个idf模型
    val idfModel = idf.fit(featurizedDataDF)
    // 得到增加新列features的DF
    val rescaledDataDF = idfModel.transform(featurizedDataDF)
    // 对数据进行转换，得到RDD形式的features
    val meetingFeatures = rescaledDataDF.map{
      row => ( row.getAs[Int]("meetingId"), row.getAs[SparseVector]("features").toArray )
    }
      .rdd
      .map{
        case (meetingId, features) => ( meetingId, new DoubleMatrix(features) )
      }

    // 两两配对商品，计算余弦相似度
    val meetingRecs = meetingFeatures.cartesian(meetingFeatures)
      .filter{
        case (a, b) => a._1 != b._1
      }
      // 计算余弦相似度
      .map{
      case (a, b) =>
        val simScore = consinSim( a._2, b._2 )
        ( a._1, ( b._1, simScore ) )
    }
      .filter(_._2._2 > 0.4)
      .groupByKey()
      .map{
        case (meetingId, recs) =>
          MeetingRecs( meetingId, recs.toList.sortWith(_._2>_._2).map(x=>Recommendation(x._1,x._2)) )
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
  def consinSim(meeting1: DoubleMatrix, meeting2: DoubleMatrix): Double ={
    meeting1.dot(meeting2)/ ( meeting1.norm2() * meeting2.norm2() )
  }
}
