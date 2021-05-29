package youdi.may.ch04

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}


/**
 *
 * 后续的
 */
object WC {
  def main(args: Array[String]): Unit = {

    val pars: ParameterTool = ParameterTool.fromArgs(args)

    // 参数是 --output xxx --input xx



    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)
    val ds: DataStream[String] = env.readTextFile("/Users/youdi/project/javaproject/Squirrel/src/main/resources/hello.txt")

    // 测试发现， 会出现空的情况
    val words: DataStream[String] = ds.flatMap(_.split(" "))
    val result: DataStream[(String, Int)] = words.map((_, 1)).keyBy(_._1).sum(1)


    result.print("hello")

    // 写到hdfs
    result.writeAsText("xx.txt").setParallelism(1)

    env.execute("wc")
  }
}
