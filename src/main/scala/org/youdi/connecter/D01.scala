package org.youdi.connecter

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.connector.jdbc.{JdbcConnectionOptions, JdbcSink, JdbcStatementBuilder}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.youdi.source.Student

import java.sql.PreparedStatement

object D01 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)


    val ds: DataStream[Student] = env.fromElements(Student(100, "three", 20), Student(200, "four", 30))

    ds.addSink(
      JdbcSink.sink(
        "insert into student (id, name, age) values (?, ?, ?);",
        new JdbcStatementBuilder[Student]() {
          override def accept(ps: PreparedStatement, v: Student) = {
            ps.setInt(1, v.id)
            ps.setString(2, v.name)
            ps.setInt(3, v.age)
          }
        },

        new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
          .withDriverName("com.mysql.jdbc.Driver")
          .withUrl("jdbc:mysql://localhost:3306/bigdata?&useSSL=false")
          .withUsername("root")
          .withPassword("root")
          .build()
      )
    )


    env.execute()
  }
}
