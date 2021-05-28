package youdi.may.ch05

import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

/**
 *
 */
object S03 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val ds: DataStream[String] = env.readTextFile("/Users/youdi/project/javaproject/Squirrel/src/main/resources/hello.txt")
    ds
      .flatMap(_.split(" "))
      .map((_, 1))
      .keyBy(_._1)
      .reduce(
        (a, b) => {
          (a._1, a._2 + b._2)
        }
      ).print("hello")
    //      .filter(_._1.contains("hello"))


    env.execute("mysql")
  }
}
