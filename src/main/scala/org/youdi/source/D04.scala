package org.youdi.source

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}


/**
 * 加载mysql数据
 */
object D04 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)


    val source: MysqlSource = new MysqlSource()
    val ds: DataStream[Student] = env.addSource(source)
    source.cancel()
    source.close()

    ds.print()

    env.execute("mysql source")
  }
}
