package org.youdi.advanced.watermarker

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkGenerator, WatermarkGeneratorSupplier, WatermarkOutput, WatermarkStrategy}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.api.common.eventtime.Watermark
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.youdi.source.{MyOrderSource, Order}

/**
 *
 */
object D02 {
  def main(args: Array[String]): Unit = {
//    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
//    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)
//    val ds: DataStream[Order] = env.addSource(new MyOrderSource())
//
//
//    val orderDS: DataStream[Order] = ds.assignTimestampsAndWatermarks(
//      new WatermarkStrategy[Order]() {
//        override def createWatermarkGenerator(context: WatermarkGeneratorSupplier.Context): WatermarkGenerator[Order] = {
//
//          new WatermarkGenerator[Order]() {
//            var userId: Int = 0
//            var eventTime: Long = 0L
//            var outOfOrdernessMillis: Long = 3000
//            var maxTimeMillis: Long = Long.MinValue + outOfOrdernessMillis + 1
//
//
//            override def onEvent(event: Order, eventTimestamp: Long, output: WatermarkOutput) = {
//              userId = event.userId
//              eventTime = event.createTime
//              maxTimeMillis = Math.max(maxTimeMillis, eventTimestamp)
//            }
//
//            override def onPeriodicEmit(output: WatermarkOutput) = {
//              val watermark: Watermark = new Watermark(maxTimeMillis - outOfOrdernessMillis - 1)
//              System.out.println("key" + userId)
//              output.emitWatermark(watermark)
//
//            }
//          }
//        }
//      }.withTimestampAssigner(
//        (event, timestamp) => {
//          event.createTime
//        }
//      )
//    )
//    // 基于事件时间进行计算
//    // 过期了， 默认就是基于事件时间的
//    //    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
//    // 设置watermarker
//    val result: DataStream[Order] = orderDS.keyBy(_.userId)
//      .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
//      .sum("money")
//
//    result.print("watermark")
//
//    env.execute()
  }
}
