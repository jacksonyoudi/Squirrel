package youdi.may.ch05

import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

/**
 *
 */
object S02 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //    env.fromElements()

    //    env.generateSequence(1,100)
    val ds: DataStream[Student] = env.addSource(MySQLSource).setParallelism(1)
    ds.print("mysql")
    val rebalance: DataStream[Student] = ds.rebalance

    env.execute("mysql")
  }
}
