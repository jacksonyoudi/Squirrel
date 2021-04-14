package org.tutorial.time

import java.time.Duration

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

object EventTimeDemo {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)
    // 默认就是 事件事件
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val ds: DataStream[MyEvent] = env.socketTextStream("", 111).map(
      a => {
        val words: Array[String] = a.split(" ")
        MyEvent(words(0).toLong, words(1), words(2), words(3).toInt)
      }
    )

    // 在流开始的时候指定水印
    val dsEvent: DataStream[MyEvent] = ds.assignTimestampsAndWatermarks(WatermarkStrategy
      .forBoundedOutOfOrderness[MyEvent](Duration.ofSeconds(20))
      .withTimestampAssigner(new SerializableTimestampAssigner[MyEvent] {
        override def extractTimestamp(element: MyEvent, recordTimestamp: Long): Long = element.createTime
      })
    )

    dsEvent.keyBy(_.userId).window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
    //    ds.keyBy(_.userId).window(TumblingProcessingTimeWindows.of(Time.seconds(100)))
    //      .reduce((a, b) => a.add(b))
    //        .assignTimestampsAndWatermarks()

    // 注意：时间戳和 watermark 都是从 1970-01-01T00:00:00Z 起的 Java 纪元开始，并以毫秒为单位。


    env.execute()
  }
}


