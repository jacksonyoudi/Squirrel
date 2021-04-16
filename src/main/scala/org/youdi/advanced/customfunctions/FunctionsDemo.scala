package org.youdi.advanced.customfunctions

import org.apache.flink.api.common.JobExecutionResult
import org.apache.flink.api.common.accumulators.LongCounter
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.datastream.DataStreamSource
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment


/**
 *
 * 算子chain
 *
 * 1. startNewChain 切断
 * 2. disableChaining  任何算子不能和当前算子进行链接
 * 3. 配置算子的资源组。
 *  Flink 会将相同资源组的算子放置到同一个 slot 槽中执行，并将不同资源组的算子分配到不同的 slot 槽中，从而实现 slot 槽隔离。
 *  如果所有输入操作都在同一个资源组, 资源组将从输入算子开始继承。
 *  Flink 默认的资源组名称为 "default"，算子可以显式调用 slotSharingGroup("default") 加入到这个资源组中。
 *  slotSharingGroup("name")
 *
 *
 *  物理分区
 *  1. partitionCustom
 *  2. shuffle
 *  3. rebalance
 *  4. rescale
 *  5. broadcast
 *
 *
 */
object FunctionsDemo {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val ds: DataStreamSource[String] = env.socketTextStream("", 1999)
    ds.map(_.toInt)
    ds.map(new MyMapFunction)

    val execute: JobExecutionResult = env.execute
    val counter: LongCounter = execute.getAccumulatorResult[LongCounter]("map_counter")
    println(counter.getLocalValue)
  }
}


class MyMapFunction extends RichMapFunction[String, Int] {
  private val counter = new LongCounter

  override def map(value: String): Int = {
    counter.add(1)
    value.toInt
  }

  override def open(parameters: Configuration): Unit = {
    getRuntimeContext.addAccumulator("map_counter", counter)
  }


}