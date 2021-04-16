package org.youdi.advanced.customfunctions

import org.apache.flink.api.common.JobExecutionResult
import org.apache.flink.api.common.accumulators.LongCounter
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.datastream.DataStreamSource
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment


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