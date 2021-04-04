package org.youdi.sink

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.youdi.source.Student

import java.sql.{Connection, DriverManager, PreparedStatement}

class MySink extends RichSinkFunction[Student] {
  var conn: Connection = null
  var ps: PreparedStatement = null

  override def invoke(value: Student, context: SinkFunction.Context): Unit = {
    ps.setInt(1, value.id)
    ps.setString(2, value.name)
    ps.setInt(3, value.age)

    ps.executeUpdate()
  }

  override def open(parameters: Configuration): Unit = {
    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bigdata?&useSSL=false", "root", "root")
    val sql: String = "insert into student (id, name, age) values (?, ?, ?);"
    ps = conn.prepareStatement(sql)
  }

  override def close(): Unit = {
    if (ps != null) ps.close()
    if (conn != null) conn.close()
  }


}
