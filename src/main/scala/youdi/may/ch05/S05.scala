package youdi.may.ch05

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.util.Collector

/**
 *
 */
object S05 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)
    val ds: DataStream[Long] = env.generateSequence(1, 100)
    ds.map(
      new RichMapFunction[Long, Tuple2[Long, Int]]() {

        //  RichFunction 中接口定义了 getRuntimeContext
        override def map(value: Long): Tuple2[Long, Int] = {
          // 子任务id
          val subtask: Int = getRuntimeContext.getIndexOfThisSubtask
          (value, subtask)
        }
      }
    ).rebalance


      .keyBy(_._2)
      .sum(1)
      .print("n")


    env.execute("")
  }
}
