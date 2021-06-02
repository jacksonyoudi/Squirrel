package youdi.may.ch05

import org.apache.flink.api.common.functions.Partitioner

object MyPartitioner extends Partitioner[Order] {

  // 返回分区id
  override def partition(key: Order, numPartitions: Int): Int = {
    0
  }
}
