package youdi.may.ch04

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}

object WC {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)
    val ds: DataStream[String] = env.readTextFile("hello.txt")

    val words: DataStream[String] = ds.flatMap(_.split(""))
    val result: DataStream[(String, Int)] = words.map((_, 1)).keyBy(_._1).sum(0)


    result.print("hello")
    env.execute("wc")
  }
}
