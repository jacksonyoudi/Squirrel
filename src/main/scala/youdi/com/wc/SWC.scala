package youdi.com.wc

//import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

import org.apache.flink.streaming.api.scala._

/**
 * 每一步都可以设置并行度
 * 比如写文件，可以设置为1
 */

object SWC {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //    StreamExecutionEnvironment.createLocalEnvironment(1)
//    StreamExecutionEnvironment.createRemoteEnvironment("vv", 6123, "jar")


    env.setParallelism(8)

    val ds: DataStream[String] = env.socketTextStream("localhost", 9988)

    val dstream: DataStream[(String, Int)] = ds.flatMap(_.split(" "))
      .map((_, 1))
      .startNewChain()
      // 和前面的共享组分开
      .slotSharingGroup("a")
      .disableChaining()
      .keyBy(_._1)
      //      .setParallelism(1).var
      .sum(1)

    dstream.print()

    // 启动任务运行
    env.execute("wc")

  }

}
