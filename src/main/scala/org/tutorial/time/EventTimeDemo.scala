package org.tutorial.time

import org.apache.flink.api.common.RuntimeExecutionMode
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

    ds.keyBy(_.userId).window(TumblingProcessingTimeWindows.of(Time.seconds(100)))
      .reduce((a, b) => a.add(b)).print()


    env.execute()
  }
}


