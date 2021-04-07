package org.youdi.senior.broadcaststate

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.common.state.MapStateDescriptor
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment


object D01 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)


//    new MapStateDescriptor("info")


    env.execute
  }

}


/**
 * 用户id
 */
//class MySource extends SourceFunction[]