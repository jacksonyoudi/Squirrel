package youdi.may.ch05.sink

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.streaming.api.datastream.DataStreamSource
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment

import java.lang


object D01 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)
    val ds: DataStreamSource[lang.Long] = env.generateSequence(1, 100)



    // slink 底层调用的是 addSource
    // ds.print("")


    ds.writeAsText("xx.txt")



    env.execute("hello")
  }
}
