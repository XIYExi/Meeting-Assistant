package com.ruoyi.rec.scala

import java.sql.{Connection, DriverManager, ResultSet}

case class Rating(meetingId: Long, score: Double);

object Test {
  def main(args: Array[String]): Unit = {
    val url = "jdbc:mysql://localhost:3306/ry-cloud";
    val username = "root";
    val password = "123456";

    Class.forName("com.mysql.cj.jdbc.Driver");
    val connection: Connection = DriverManager.getConnection(url, username, password);

    try {
      val statement = connection.createStatement();
      val resultSet: ResultSet = statement.executeQuery("select meeting_id, rate from meeting_rate");
      while (resultSet.next()) {
        val id: Long = resultSet.getLong("meeting_id");
        val rate: Double =resultSet.getDouble("rate");
        println(s"ID: $id, Rate: $rate");
      }

    }catch {
      case e: Exception => e.printStackTrace();
    }finally {
      connection.close();
    }
  }
}
