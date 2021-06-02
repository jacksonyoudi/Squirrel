package youdi.may.ch05

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.util.Collector

/**
 *
 */
object S04 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)
    val ds: DataStream[Long] = env.generateSequence(1, 100)

    val odd: OutputTag[Long] = new OutputTag[Long]("odd")
    val even: OutputTag[Long] = new OutputTag[Long]("even")

    val result: DataStream[Long] = ds.process(
      new ProcessFunction[Long, Long]() {
        override def processElement(value: Long, ctx: ProcessFunction[Long, Long]#Context, out: Collector[Long]) = {
          if (value % 2 == 0) {
            ctx.output(even, value)
          } else {
            ctx.output(odd, value)
          }
        }
      }
    )

    // 选择流
    val ds1: DataStream[Long] = result.getSideOutput(even)
    val ds2: DataStream[Long] = result.getSideOutput(odd)

    ds1.print("even")
    ds2.print("odd")

    env.execute("mysql")
  }
}
