package org.youdi.wc

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}

object D01 {
  def main(args: Array[String]): Unit = {
    val params: ParameterTool = ParameterTool.fromArgs(args)
    if (params.has("output")) {
      val str: String = params.get("output", "hello")
    }


    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.BATCH)

    val ds: DataStream[String] = env.fromElements("hello spark", "hello flink", "text")
    val sumDs: DataStream[(String, Int)] = ds.flatMap(_.split(" ")).map((_, 1)).keyBy(_._1).sum(2)


    sumDs.writeAsText("hello")

    env.execute()

  }
}
