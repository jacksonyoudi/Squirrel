package org.youdi.source

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.streaming.api.scala.{CloseableIterator, DataStream, StreamExecutionEnvironment, createTypeInformation}

object D03 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)

    val ds: DataStream[Order] = env.addSource[Order](new MyOrderSource())
    ds.print()

    env.execute("cutom source")
  }
}
