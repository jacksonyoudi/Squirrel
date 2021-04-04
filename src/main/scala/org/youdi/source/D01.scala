package org.youdi.source

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
 * 基于集合的source
 */
object D01 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.BATCH)

    val ds: DataStream[String] = env.fromElements("hadoop spark flink", "flink spark hbase")

    //    val ds1: DataStream[util.List[String]] = env.fromElements(util.Arrays.asList("hadoop", "spark"))

    //    val dsSeq: DataStream[Long] = env.fromSequence(1, 100)

    ds.print()

    // sink
    env.execute()
  }

}
