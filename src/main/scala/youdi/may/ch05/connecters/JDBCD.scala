package youdi.may.ch05.connecters

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.connector.jdbc.{JdbcConnectionOptions, JdbcSink, JdbcStatementBuilder}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import youdi.may.ch05.Student

import org.apache.flink.connector.jdbc.JdbcStatementBuilder

object JDBCD {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)

    val ds: DataStream[Student] = env.fromElements(Student(100, "nihao", 100))

//    ds.addSink(
//      JdbcSink.sink(
//        "insert into `student` (`id`,`name`,`age`) values (?,?,?);",
//        (ps, value: Student) => {
//          ps.setInt(1, value.id)
//          ps.setString(2, value.name)
//          ps.setInt(3, value.age)
//        }, new JdbcConnectionOptions
//        .JdbcConnectionOptionsBuilder()
//          .withUrl("jdbc:mysql://localhost:3306/bigdata")
//          .withUsername("root")
//          .withPassword("root")
//          .withDriverName("com.mysql.jdbc.Driver")
//          .build()
//      )
//    )


    env.execute("")
  }
}
