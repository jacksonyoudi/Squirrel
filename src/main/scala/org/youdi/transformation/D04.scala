package org.youdi.transformation

import org.apache.flink.api.common.functions.{Partitioner, RichMapFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}


object D04 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val ds: DataStream[Long] = env.fromSequence(1, 10)

    val mapDS: DataStream[(Int, Int)] = ds.map(
      new RichMapFunction[Long, (Int, Int)]() {
        override def map(value: Long) = {
          val subtaskId: Int = getRuntimeContext().getIndexOfThisSubtask() //  子任务id 分区编号id
          (subtaskId, 1)
        }
      }
    )

    val r1: DataStream[(Int, Int)] = mapDS.global
    val r2: DataStream[(Int, Int)] = mapDS.broadcast
    val r3: DataStream[(Int, Int)] = mapDS.forward
    val r4: DataStream[(Int, Int)] = mapDS.shuffle
    val r5: DataStream[(Int, Int)] = mapDS.rebalance
    val r6: DataStream[(Int, Int)] = mapDS.rescale
    val r7: DataStream[(Int, Int)] = mapDS.partitionCustom(new MyPartitioner(), _._1)

    r1.print("r1")
    r2.print("r2")
    r3.print("r3")
    r4.print("r4")
    r5.print("r5")
    r6.print("r6")
    r7.print("r7")


    env.execute()
  }

}


class MyPartitioner extends Partitioner[Int] {
  override def partition(key: Int, numPartitions: Int): Int = {
    if (key % 2 == 0) {
      0
    } else {
      1
    }
  }
}