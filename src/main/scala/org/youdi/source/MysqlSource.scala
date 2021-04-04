package org.youdi.source

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}

class MysqlSource extends RichSourceFunction[Student] {
  var flag: Boolean = true
  var conn: Connection = null
  var ps: PreparedStatement = null

  override def run(ctx: SourceFunction.SourceContext[Student]): Unit = {
    while (flag) {
      val resultSet: ResultSet = ps.executeQuery()
      while (resultSet.next()) {
        val id: Int = resultSet.getInt("id")
        val name: String = resultSet.getString("name")
        val age: Int = resultSet.getInt("age")

        ctx.collect(Student(id, name, age))
      }

      //        ctx.collect()
      Thread.sleep(1000)

    }
  }

  override def cancel(): Unit = {
    flag = false
  }

  // open只执行一次， 适合开启资源
  override def open(parameters: Configuration): Unit = {
    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bigdata", "root", "root")
    val sql: String = "select id, name, age from student"
    ps = conn.prepareStatement(sql)

  }

  // 关闭资源
  override def close(): Unit = {
    if (conn != null) conn.close()
    if (ps != null) ps.close()


  }
}
