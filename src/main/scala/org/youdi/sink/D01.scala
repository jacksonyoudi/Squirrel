package org.youdi.sink

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}


/**
 * sink 基于控制台和文件的
 */
object D01 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)
    val ds: DataStream[Long] = env.fromSequence(1, 100)

//    ds.windowAll()

    ds.print()
    ds.printToErr()

    ds.writeAsText("one.txt").setParallelism(1)
//    ds.writeUsingOutputFormat(OutputFormat)

    env.execute("sink")
  }

}
