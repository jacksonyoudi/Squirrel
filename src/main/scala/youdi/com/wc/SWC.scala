package youdi.com.wc

//import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

import org.apache.flink.streaming.api.scala._

object SWC {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    env.setParallelism(8)

    val ds: DataStream[String] = env.socketTextStream("localhost", 9988)

    val dstream: DataStream[(String, Int)] = ds.flatMap(_.split(" "))
      .map((_, 1))
      .keyBy(_._1)
      .sum(1)

    dstream.print()

    // 启动任务运行
    env.execute("wc")

  }

}
