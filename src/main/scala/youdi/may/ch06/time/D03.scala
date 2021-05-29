package youdi.may.ch06.time

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import youdi.may.ch05.Order

import java.time.Duration

object D03 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)

    val tag: OutputTag[Order] = new OutputTag[Order]("order")


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

    val result: DataStream[Order] = orderDS.keyBy(_.userId)
      .window(
        TumblingProcessingTimeWindows.of(Time.seconds(5))
      )
      .allowedLateness(Time.seconds(5))
      .sideOutputLateData(tag)
      .sum("money")

    val result1: DataStream[Order] = result.getSideOutput(tag)

    result.print("1")
    result1.print("2")


    env.execute("")
  }
}
