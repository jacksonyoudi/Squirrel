//package youdi.may.ch06.time
//
//import org.apache.flink.api.common.RuntimeExecutionMode
//import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, Watermark, WatermarkGenerator, WatermarkGeneratorSupplier, WatermarkOutput, WatermarkStrategy}
//import org.apache.flink.streaming.api.functions.windowing.WindowFunction
//import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
//import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
//import org.apache.flink.streaming.api.windowing.time.Time
//import org.apache.flink.streaming.api.windowing.windows.TimeWindow
//import org.apache.flink.util.Collector
//import youdi.may.ch05.Order
//
//import java.{lang, util}
//import java.time.Duration
//import scala.collection.mutable.{ArrayBuffer, ListBuffer}
//
//object D02 {
//  def main(args: Array[String]): Unit = {
//    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
//
//    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)
//    val ds: DataStream[Order] = env.addSource(GenOrderSource)
//    val orderDS: DataStream[Order] = ds.assignTimestampsAndWatermarks(
//      new WatermarkStrategy[Order] {
//        override def createWatermarkGenerator(context: WatermarkGeneratorSupplier.Context) = {
//          new WatermarkGenerator[Order]() {
//            private var userId: Int = 0
//            private var eventTime: Long = 0
//            private var outOfOrdernessMillis: Long = 3000
//            private var maxTimeMillis: Long = Long.MinValue + outOfOrdernessMillis + 1;
//
//
//            override def onEvent(event: Order, eventTimestamp: Long, output: WatermarkOutput) = {
//              userId = event.userId
//              eventTime = event.createTime
//              maxTimeMillis = Math.max(maxTimeMillis, event.createTime)
//            }
//
//            override def onPeriodicEmit(output: WatermarkOutput) = {
//              val watermark: Watermark = new Watermark[Order](maxTimeMillis - outOfOrdernessMillis - 1)
//              output.emitWatermark(watermark)
//            }
//          }
//        }
//      }.withTimestampAssigner((event, timestamp) => event.createTime)
//    )
//
//    orderDS.keyBy(_.userId)
//      .window(
//        TumblingProcessingTimeWindows.of(Time.seconds(5))
//      )
////      .apply(
////        new WindowFunction[Order]() {
////          override def apply(key: Order, window: TimeWindow, input: lang.Iterable[Order], out: Collector[String]) = {
////            val orders: ListBuffer[Order] = new ListBuffer[Order]
////
////            input.forEach(
////              order => {
////                val createTime: Long = order.createTime
////                orders += order
////              }
////            )
////            val start: Long = window.getStart()
////            val end: Long = window.getEnd()
////            out.collect("")
////          }
////        }
////      )
////
////    env.execute("")
//  }
//}
//package youdi.may.ch06.time
//
//import org.apache.flink.api.common.RuntimeExecutionMode
//import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, Watermark, WatermarkGenerator, WatermarkGeneratorSupplier, WatermarkOutput, WatermarkStrategy}
//import org.apache.flink.streaming.api.functions.windowing.WindowFunction
//import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
//import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
//import org.apache.flink.streaming.api.windowing.time.Time
//import org.apache.flink.streaming.api.windowing.windows.TimeWindow
//import org.apache.flink.util.Collector
//import youdi.may.ch05.Order
//
//import java.{lang, util}
//import java.time.Duration
//import scala.collection.mutable.{ArrayBuffer, ListBuffer}
//
//object D02 {
//  def main(args: Array[String]): Unit = {
//    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
//
//    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)
//    val ds: DataStream[Order] = env.addSource(GenOrderSource)
//    val orderDS: DataStream[Order] = ds.assignTimestampsAndWatermarks(
//      new WatermarkStrategy[Order] {
//        override def createWatermarkGenerator(context: WatermarkGeneratorSupplier.Context) = {
//          new WatermarkGenerator[Order]() {
//            private var userId: Int = 0
//            private var eventTime: Long = 0
//            private var outOfOrdernessMillis: Long = 3000
//            private var maxTimeMillis: Long = Long.MinValue + outOfOrdernessMillis + 1;
//
//
//            override def onEvent(event: Order, eventTimestamp: Long, output: WatermarkOutput) = {
//              userId = event.userId
//              eventTime = event.createTime
//              maxTimeMillis = Math.max(maxTimeMillis, event.createTime)
//            }
//
//            override def onPeriodicEmit(output: WatermarkOutput) = {
//              val watermark: Watermark = new Watermark[Order](maxTimeMillis - outOfOrdernessMillis - 1)
//              output.emitWatermark(watermark)
//            }
//          }
//        }
//      }.withTimestampAssigner((event, timestamp) => event.createTime)
//    )
//
//    orderDS.keyBy(_.userId)
//      .window(
//        TumblingProcessingTimeWindows.of(Time.seconds(5))
//      )
////      .apply(
////        new WindowFunction[Order]() {
////          override def apply(key: Order, window: TimeWindow, input: lang.Iterable[Order], out: Collector[String]) = {
////            val orders: ListBuffer[Order] = new ListBuffer[Order]
////
////            input.forEach(
////              order => {
////                val createTime: Long = order.createTime
////                orders += order
////              }
////            )
////            val start: Long = window.getStart()
////            val end: Long = window.getEnd()
////            out.collect("")
////          }
////        }
////      )
////
////    env.execute("")
//  }
//}
