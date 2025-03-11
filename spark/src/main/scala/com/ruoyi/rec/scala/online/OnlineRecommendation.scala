package com.ruoyi.rec.scala.online

import com.mongodb.casbah.Imports.{MongoClient, MongoClientURI}
import com.mongodb.casbah.commons.MongoDBObject
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.Jedis


// 定义一个连接助手对象，建立到redis和mongodb的连接
object ConnHelper extends Serializable {
  // 懒变量定义，使用的时候才初始化
  lazy val jedis = new Jedis("192.168.31.76")
  lazy val mongoClient = MongoClient(MongoClientURI("mongodb://root:123456@192.168.31.94:27017/meeting?authSource=admin&authMechanism=SCRAM-SHA-1"))
}


case class MeetingRating(userId: Long, meetingId: Long, score: Double, timestamp: Int)

case class MongoConfig(uri: String, db: String)

// 定义推荐对象
case class Recommendation(meetingId: Long, score: Double)

// 定义用户的推荐列表
case class UserRecs(userId: Long, recs: Seq[Recommendation])

// 定义meeting相似度列表
case class MeetingRecs(meetingId: Long, recs: Seq[Recommendation])

object OnlineRecommendation {
  val MONGODB_RATING_COLLECTION = "user_ratings"
  val USER_REC = "user_rec"
  val MEETING_REC = "meeting_rec"
  val STREAM_RECS = "stream rec"
  val USER_MAX_LENGTH = 20
  val MAX_SIM_PRODUCTS_NUM = 20
  def main(args: Array[String]) = {
    val config = Map(
      "spark.cores" -> "local[*]",
      "mongo.uri" -> "mongodb://root:123456@192.168.31.94:27017/meeting?authSource=admin&authMechanism=SCRAM-SHA-1",
      "mongo.db" -> "meeting",
      "kafka.topic" -> "recommendation"
    );

    // 创建spark config
    val sparkConf = new SparkConf().setMaster(config("spark.cores")).setAppName("OnlineRecommendation");
    // 创建spark session
    val spark = SparkSession.builder().config(sparkConf).getOrCreate();
    val sc = spark.sparkContext
    val ssc = new StreamingContext(sc, Seconds(2))
    import spark.implicits._
    implicit val mongoConfig = MongoConfig(config("mongo.uri"), config("mongo.db"));
    val simMeetingMatrix = spark.read
      .option("uri", mongoConfig.uri)
      .option("collection", MEETING_REC)
      .format("com.mongodb.spark.sql")
      .load()
      .as[MeetingRecs]
      .rdd
      .map { res =>
        (res.meetingId.intValue(), res.recs.map(x => (x.meetingId.intValue(), x.score)).toMap)
      }
      .collectAsMap()
    // 定义广播变量
    val simMeetingsMatrixBC = sc.broadcast(simMeetingMatrix)

    // 创建kafka配置参数
    val kafkaParam = Map(
      "bootstrap.servers" -> "192.168.31.94:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "recommendation",
      "auto.offset.reset" -> "latest"
    );
    val kafkaStream = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](Array(config("kafka.topic")), kafkaParam)
    );
    // 对kafka stream进行处理，产生评分流
    // 封装userId|meetingId|score|timestamp
    val ratingStream = kafkaStream.map { msg =>
      val attr = msg.value().split("\\|")
      (attr(0).toInt, attr(1).toInt, attr(2).toDouble, attr(3).toInt)
    }
    // 定义评分流
    ratingStream.foreachRDD {
      rdds =>
        rdds.foreach {
          case (userId, meetingId, score, timestamp) =>
            print("rating data coming... ", userId);
            // 1. 拿出用户最近的评分，组装成一个数组Array[(meetingId, score)]
            val userRecentlyRatings = getUserRecentlyRatings(USER_MAX_LENGTH, userId, ConnHelper.jedis)
            // 2. 从相似度矩阵中拿到当前会议相似度列表，作为备选
            val candidateMeetings = getTopMeetings(MAX_SIM_PRODUCTS_NUM, meetingId, userId, simMeetingsMatrixBC.value)
            // 3. 计算每个备选的优先级，得到实时推荐列表
            val streamRecs = computeMeetingScore(candidateMeetings, userRecentlyRatings, simMeetingsMatrixBC.value)
            // 4. 保存实时推荐列表到mongodb中
            saveDataToMongoDB(userId, streamRecs)
        }
    }
    ssc.start()
    print("spark streaming started....")
    ssc.awaitTermination()
  }

  import scala.collection.JavaConversions._

  def getUserRecentlyRatings(num: Int, userId: Int, jedis: Jedis): Array[(Int, Double)] = {
    // 从redis中用户的评分队列里获取评分数据，list键名为uid:USERID，值格式是 PRODUCTID:SCORE
    jedis.lrange("rec:rating:userId:" + userId.toString, 0, num)
      .map {item =>
        // print(item)
        val attr = item.replaceAll("^\"|\"$", "").split("\\:")
        (attr(0).trim.toInt, attr(1).trim.toDouble)
      }
      .toArray
  }

  def getTopMeetings(
                      num: Int,
                      meetingId: Int,
                      userId: Int,
                      simMeetings: scala.collection.Map[Int, scala.collection.immutable.Map[Int, Double]]
                    )(implicit mongoConfig: MongoConfig): Array[Int] = {
    // 从广播变量相似度矩阵中拿到当前会议的相似度列表
    val allSimMeetings = simMeetings(meetingId).toArray

    // 获得用户已经打过分的会议，并且过滤掉
    val ratingCollection = ConnHelper.mongoClient(mongoConfig.db)(MONGODB_RATING_COLLECTION)
    val ratingExist = ratingCollection.find(MongoDBObject("userId" -> userId))
      .toArray
      .map { item =>
        item.get("meetingId").toString.toInt
      }
    // 从所有相似的会议里面进行过滤
    allSimMeetings.filter(x => !ratingExist.contains(x._1))
      .sortWith(_._2 > _._2)
      .take(num)
      .map(x => x._1)
  }

  def computeMeetingScore(
                           candidateMeetings: Array[Int],
                           userRecentlyRatings: Array[(Int, Double)],
                           simMeetings: scala.collection.Map[Int, scala.collection.immutable.Map[Int, Double]]
                         ): Array[(Int, Double)] = {
    // 定义一个长度可变数组ArrayBuffer，用于保存每一个备选会议的基础得分，(meetingId, score)
    val scores = scala.collection.mutable.ArrayBuffer[(Int, Double)]()
    // 定义两个map，用于保存每个会议的高分和低分的计数器，productId -> count
    val increMap = scala.collection.mutable.HashMap[Int, Int]()
    val decreMap = scala.collection.mutable.HashMap[Int, Int]()

    // 遍历每个备选会议，计算和已评分会议的相似度
    for (candidateMeeting <- candidateMeetings; userRecentlyRating <- userRecentlyRatings) {
      // 从相似度矩阵中获取当前备选商品和当前已评分商品间的相似度
      val simScore = getMeetingSimScore(candidateMeeting, userRecentlyRating._1, simMeetings)
      if (simScore > 0.4) {
        // 按照公式进行加权计算，得到基础评分
        scores += ((candidateMeeting, simScore * userRecentlyRating._2))
        if (userRecentlyRating._2 > 3) {
          increMap(candidateMeeting) = increMap.getOrDefault(candidateMeeting, 0) + 1
        } else {
          decreMap(candidateMeeting) = decreMap.getOrDefault(candidateMeeting, 0) + 1
        }
      }
    }
    // 根据公式计算所有的推荐优先级，首先以productId做groupby
    scores.groupBy(_._1).map {
        case (meetingId, scoreList) =>
          (meetingId, scoreList.map(_._2).sum / scoreList.length + log(increMap.getOrDefault(meetingId, 1)) - log(decreMap.getOrDefault(meetingId, 1)))
      }
      // 返回推荐列表，按照得分排序
      .toArray
      .sortWith(_._2 > _._2)
  }

  def getMeetingSimScore(meeting1: Int, meeting2: Int,
                         simMeetings: scala.collection.Map[Int, scala.collection.immutable.Map[Int, Double]]): Double = {
    simMeetings.get(meeting1) match {
      case Some(sims) => sims.get(meeting2) match {
        case Some(score) => score
        case None => 0.0
      }
      case None => 0.0
    }
  }

  // 自定义log函数，以N为底
  def log(m: Int): Double = {
    val N = 10
    math.log(m) / math.log(N)
  }

  def saveDataToMongoDB(userId: Int, streamRecs: Array[(Int, Double)])
                       (implicit mongoConfig: MongoConfig): Unit = {
    val streamRecsCollection = ConnHelper.mongoClient(mongoConfig.db)(STREAM_RECS)
    // 按照userId查询并更新
    streamRecsCollection.findAndRemove(MongoDBObject("userId" -> userId))
    streamRecsCollection.insert(MongoDBObject(
      "userId" -> userId,
      "recs" -> streamRecs.map(x => MongoDBObject("meetingId" -> x._1, "score" -> x._2))
    ))
  }
}
