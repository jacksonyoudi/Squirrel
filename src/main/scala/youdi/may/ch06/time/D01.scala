package youdi.may.ch06.time

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import youdi.may.ch05.Order

import java.time.Duration

object D01 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)
    val ds: DataStream[Order] = env.addSource(GenOrderSource)
    val orderDS: DataStream[Order] = ds.assignTimestampsAndWatermarks(
      WatermarkStrategy
        // 延迟时间 最大无序度
        .forBoundedOutOfOrderness[Order](Duration.ofSeconds(5))
        // 指定事件时间
        .withTimestampAssigner(
          new SerializableTimestampAssigner[Order]() {
            override def extractTimestamp(element: Order, recordTimestamp: Long) = {
              element.createTime
            }
          }
        ))

        orderDS.keyBy(_.userId)
      .window(
        TumblingProcessingTimeWindows.of(Time.seconds(5))
      )
      .sum("money")
      .print("order")


    env.execute("")
  }
}
