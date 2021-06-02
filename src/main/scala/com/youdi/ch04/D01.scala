package com.youdi.ch04

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

object D01 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)

//    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

//    env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime)


    env.execute("hello")
  }

}
