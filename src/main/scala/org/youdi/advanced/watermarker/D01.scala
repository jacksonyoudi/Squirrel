package org.youdi.advanced.watermarker

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.youdi.source.{MyOrderSource, Order}

import java.time.Duration
import scala.collection.mutable.ArrayBuffer


/**
 *
 */
object D01 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)
    val ds: DataStream[Order] = env.addSource(new MyOrderSource())


    // 基于事件时间进行计算
    // 过期了， 默认就是基于事件时间的
    //    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    // 设置watermarker
    val orderDS: DataStream[Order] = ds.assignTimestampsAndWatermarks(
      WatermarkStrategy
        .forBoundedOutOfOrderness[Order](Duration.ofSeconds(5)) // 最大无序度 最大的延迟的时间
        .withTimestampAssigner(
          new SerializableTimestampAssigner[Order] { //
            override def extractTimestamp(element: Order, recordTimestamp: Long): Long = element.createTime // 指定事件时间
          }
        )
    )

    val result: DataStream[Order] = orderDS.keyBy(_.userId)
      .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
      .sum("money")
    //      .apply(
    //        new WindowFunction[Order, String, Int, TimeWindow]() {
    //          override def apply(key: Int, window: TimeWindow, input: Iterable[Order], out: Collector[String]): Unit = {
    //            val orders: ArrayBuffer[Order] = new ArrayBuffer[Order]
    //
    //
    //            val start: Long = window.getStart
    //            val end: Long = window.getEnd
    //
    //            out.collect("s")
    //          }
    //        }
    //
    //      )


    env.execute()
  }
}
