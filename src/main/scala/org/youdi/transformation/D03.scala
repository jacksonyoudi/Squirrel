package org.youdi.transformation

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

object D03 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)


    val ds: DataStream[Long] = env.fromSequence(0, 1000)
    val filteDs: DataStream[Long] = ds.filter(_ < 100)


    val mapDS: DataStream[(Int, Int)] = filteDs.map(
      new RichMapFunction[Long, (Int, Int)]() {
        override def map(value: Long) = {
          val subtaskId: Int = getRuntimeContext().getIndexOfThisSubtask() //  子任务id 分区编号id
          (subtaskId, 1)
        }
      }
    )

    val result1: DataStream[(Int, Int)] = mapDS.keyBy(_._1).sum(1)
    result1.print("result1")


    val mapDS2: DataStream[(Int, Int)] = filteDs
      .rebalance
      .map(
        new RichMapFunction[Long, (Int, Int)]() {
          override def map(value: Long) = {
            val subtaskId: Int = getRuntimeContext().getIndexOfThisSubtask() //  子任务id 分区编号id
            (subtaskId, 1)
          }
        }
      )

    val result2: DataStream[(Int, Int)] = mapDS2.keyBy(_._1).sum(1)
    result2.print("result2")


    env.execute("rebalance")

  }

}
