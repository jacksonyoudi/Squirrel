package org.youdi.source

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.streaming.api.scala.{CloseableIterator, DataStream, StreamExecutionEnvironment, createTypeInformation}

/**
 * 基于文件的source
 *
 */
object D02 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.BATCH)
    val ds: DataStream[String] = env.readTextFile("/Users/youdi/project/javaproject/Squirrel/src/main/resources/hello.txt")
    val result: DataStream[(String, Int)] = ds.flatMap(_.split(" ")).map((_, 1)).keyBy(_._1).sum(1)
    result.print()


    env.execute()
  }

}



