package org.tutorial.state

import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.{StateTtlConfig, ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.util.Collector
import org.apache.flink.api.scala._
import org.apache.flink.api.common.time.Time

object ExampleCountWindowAverage {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.fromElements(
      (1L, 3L),
      (1L, 5L),
      (1L, 7L),
      (1L, 4L),
      (1L, 2L)
    )
      .keyBy(_ (1))
      .flatMap(new CountWindowAverage())
      .print()

    env.execute("ExampleKeyedState")

  }
}


class CountWindowAverage extends RichFlatMapFunction[(Long, Long), (Long, Long)] {

  // 定义一个保存数据的  ValueState
  private var sum: ValueState[(Long, Long)] = _

  override def flatMap(input: (Long, Long), out: Collector[(Long, Long)]): Unit = {

    // access the state value
    val tmpCurrentSum = sum.value

    // If it hasn't been used before, it will be null
    val currentSum = if (tmpCurrentSum != null) {
      tmpCurrentSum
    } else {
      (0L, 0L)
    }

    // update the count
    val newSum = (currentSum._1 + 1, currentSum._2 + input._2)

    // update the state
    sum.update(newSum)

    // if the count reaches 2, emit the average and clear the state
    if (newSum._1 >= 2) {
      out.collect((input._1, newSum._2 / newSum._1))
      sum.clear()
    }
  }

  override def open(parameters: Configuration): Unit = {

    // 设置过期时间
    val ttlConfig: StateTtlConfig = StateTtlConfig
      .newBuilder(Time.seconds(1))
      .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
      .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
      .build

    val descriptor: ValueStateDescriptor[(Long, Long)] = new ValueStateDescriptor[(Long, Long)]("average", createTypeInformation[(Long, Long)])
    descriptor.enableTimeToLive(ttlConfig)


    sum = getRuntimeContext.getState(descriptor)
  }
}
