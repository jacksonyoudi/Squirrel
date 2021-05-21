package com.youdi.ch02

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.streaming.api.functions.source.FileProcessingMode
import org.apache.flink.streaming.api.scala.{OutputTag, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._

object D01 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)


    // 1. 从内存中读取数据
    //    env.fromElements()
    //    env.fromElements()
    //    env.fromCollection()
    //    env.fromCollection()
    //env.fromParallelCollection()

    // 文件读取数据
    //    env.readTextFile()
    //    env.readTextFile()
    //
    //
    //FileProcessingMode

    //


    //    env.socketTextStream()

    //      env.createInput()


    // 旁路
    val one: OutputTag[String] = new OutputTag[String]("one")




    env.execute("api source")
  }
}
