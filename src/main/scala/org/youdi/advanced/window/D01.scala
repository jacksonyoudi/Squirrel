package org.youdi.advanced.window

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.streaming.api.windowing.assigners.{SlidingProcessingTimeWindows, TumblingProcessingTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time

/**
 * 演示基于时间的滚动和滑动窗口
 */
object D01 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)

    val ds: DataStream[String] = env.socketTextStream("localhost", 9876)
    val ds1: DataStream[Array[String]] = ds.map(_.split(","))
    val keyDS: KeyedStream[(String, Int), String] = ds1.map(a => (a(0), a(1).toInt)).
      keyBy(_._1)

    //    keyDS.timeWindow(Time.seconds(20))


    // 滚动 5秒
    val tumblingDS: DataStream[(String, Int)] = keyDS.window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
      .sum(1)

    tumblingDS.print("滚动")


    // 滑动， 每5秒计算10s的内容
    val slidingDS: DataStream[(String, Int)] = keyDS.window(SlidingProcessingTimeWindows.of(Time.seconds(10), Time.seconds(5)))
      .sum(1)

    slidingDS.print("滑动")

    env.execute("window")


  }
}


