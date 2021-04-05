package org.youdi.advanced.window

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment, WindowedStream, createTypeInformation}
import org.apache.flink.streaming.api.windowing.assigners.{SlidingProcessingTimeWindows, TumblingProcessingTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow

/**
 * 演示基于数量的滚动和滑动窗口
 */
object D02 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)

    val ds: DataStream[String] = env.socketTextStream("localhost", 9876)
    val ds1: DataStream[Array[String]] = ds.map(_.split(","))
    val keyDS: KeyedStream[(String, Int), String] = ds1.map(a => (a(0), a(1).toInt)).
      keyBy(_._1)

    //    keyDS.timeWindow(Time.seconds(20))
    // 分组后的5条消息的 进行触发的
    val tumb: DataStream[(String, Int)] = keyDS.countWindow(5).sum(1)
    tumb.print()

    val slide: DataStream[(String, Int)] = keyDS.countWindow(5, 3).sum(1)

    slide.print()


    env.execute("window")


  }
}


