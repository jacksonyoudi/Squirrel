package org.youdi.transformation

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala.{DataStream, OutputTag, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.util.Collector


object D02 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)

    val ds: DataStream[Int] = env.fromElements(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    val odd: OutputTag[Int] = new OutputTag[Int]("odd")
    val even: OutputTag[Int] = new OutputTag[Int]("even")


    val result: DataStream[Int] = ds.process[Int](
      new ProcessFunction[Int, Int]() {
        override def processElement(value: Int, ctx: ProcessFunction[Int, Int]#Context, out: Collector[Int]) = {
          if (value % 2 == 0) {
            ctx.output(odd, value)
          } else {
            ctx.output(even, value)
          }
        }
      }
    )

    val oddDs: DataStream[Int] = result.getSideOutput(odd)
    val evenDs: DataStream[Int] = result.getSideOutput(even)


    oddDs.print("odd")
    evenDs.print("even")


    env.execute("output tag")


  }

}
