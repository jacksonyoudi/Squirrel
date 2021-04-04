package org.youdi.sink

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.youdi.source.Student

object D02 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)


    val ds: DataStream[Student] = env.fromElements(Student(10, "one", 20), Student(20, "two", 30))

    ds.addSink(new MySink())


    env.execute()
  }

}
