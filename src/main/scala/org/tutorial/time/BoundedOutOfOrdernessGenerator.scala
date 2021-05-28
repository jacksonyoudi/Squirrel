//package org.tutorial.time
//
//import org.apache.flink.api.common.eventtime.{WatermarkGenerator, WatermarkOutput}
//import org.apache.flink.streaming.api.watermark.Watermark
//
//
///**
// * 该 watermark 生成器可以覆盖的场景是：数据源在一定程度上乱序。
// * 即某个最新到达的时间戳为 t 的元素将在最早到达的时间戳为 t 的元素之后最多 n 毫秒到达。
// */
//class BoundedOutOfOrdernessGenerator extends WatermarkGenerator[MyEvent] {
//
//  val maxOutOfOrderness = 3500L // 3.5 秒
//
//  var currentMaxTimestamp: Long = _
//
//  override def onEvent(event: MyEvent, eventTimestamp: Long, output: WatermarkOutput): Unit = {
//    if (event.createTime > currentMaxTimestamp) {
//      currentMaxTimestamp = event.createTime
//    }
//  }
//
//  override def onPeriodicEmit(output: WatermarkOutput): Unit = {
//    // 发出的 watermark = 当前最大时间戳 - 最大乱序时间
//
//    output.emitWatermark(new Watermark(currentMaxTimestamp - maxOutOfOrderness - 1))
//  }
//}
//
///**
// * 该生成器生成的 watermark 滞后于处理时间固定量。它假定元素会在有限延迟后到达 Flink。
// */
//class TimeLagWatermarkGenerator extends WatermarkGenerator[MyEvent] {
//  val maxTimeLag = 5000L // 5 秒
//
//
//  override def onEvent(event: MyEvent, eventTimestamp: Long, output: WatermarkOutput): Unit = {
//    // 处理时间场景下不需要实现
//  }
//
//  override def onPeriodicEmit(output: WatermarkOutput): Unit = {
//    // 只处理当前事件的5秒内的数据
//    output.emitWatermark(new Nothing(System.currentTimeMillis - maxTimeLag))
//  }
//}
//
//
//class PunctuatedAssigner extends WatermarkGenerator[MyEvent] {
//
//  // 标记 watermark 生成器观察流事件数据并在获取到带有 watermark 信息的特殊事件元素时发出 watermark。
//  // 注意：可以针对每个事件去生成 watermark。但是由于每个 watermark 都会在下游做一些计算，因此过多的 watermark 会降低程序性能。
//  override def onEvent(event: MyEvent, eventTimestamp: Long, output: WatermarkOutput): Unit = {
//    //    if (event.hasWatermarkMarker()) {
//    //      output.emitWatermark(new Watermark(event.getWatermarkTimestamp()))
//    //    }
//  }
//
//  override def onPeriodicEmit(output: WatermarkOutput): Unit = {
//    // onEvent 中已经实现
//  }
//}
