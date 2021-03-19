package youdi.com.datasource

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

object Source {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 基于集合
    //    val values: DataStream[List[Int]] = env.fromElements(
    //      List(1, 2, 3, 4)
    //    )

    val txtStream: DataStream[String] = env.readTextFile("hello.txt")

    val s: DataStream[String] = env.addSource(new HbaseSource[String]())


    env.execute("data source example")


  }

}
