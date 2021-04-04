package org.youdi.transformation

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}


object D01 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val ds: DataStream[String] = env.socketTextStream("localhost", 9876)

    val dSI: DataStream[(String, Int)] = ds.flatMap(_.split(" "))
      .map((_, 1))
      .keyBy(_._1)
      .sum(1)

    dSI.print()

    env.execute()
  }
}
