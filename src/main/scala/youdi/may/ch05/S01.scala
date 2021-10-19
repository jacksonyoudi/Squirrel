package youdi.may.ch05

import org.apache.flink.streaming.api.operators.StreamOperator
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._

/**
 *
 */
object S01 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //    env.fromElements()

    //    env.generateSequence(1,100)

    val ds: DataStream[Order] = env.addSource(new OrderSource).setParallelism(2)
    ds.print("order")

    env.execute("")
  }
}
