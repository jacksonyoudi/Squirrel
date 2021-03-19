package youdi.com.wc


import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
// 隐式转换

import org.apache.flink.api.scala._


object WC {
  def main(args: Array[String]): Unit = {
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    val ds: DataSet[String] = env.readTextFile("/Users/youdi/project/javaproject/Squirrel/src/main/resources/hello.txt")
    val result: AggregateDataSet[(String, Int)] = ds.flatMap(_.split(" "))
      .map((_, 1))
      .groupBy(0)
      .sum(1)

    result.print()
  }
}
