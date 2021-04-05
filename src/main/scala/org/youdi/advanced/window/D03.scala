package org.youdi.advanced.window

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.streaming.api.windowing.assigners.ProcessingTimeSessionWindows
import org.apache.flink.streaming.api.windowing.time.Time

object D03 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)

    val ds: DataStream[String] = env.socketTextStream("localhost", 9876)
    val ds1: DataStream[Array[String]] = ds.map(_.split(","))
    val mapDS: DataStream[(String, Int)] = ds1.map(a => (a(0), a(1).toInt))
    val result: DataStream[(String, Int)] = mapDS.keyBy(_._1).window(ProcessingTimeSessionWindows.withGap(Time.seconds(10)))
      .sum(1)

    result.print()


    env.execute("window")
  }
}
