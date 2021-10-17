package youdi.com.datasource

import org.apache.flink.streaming.api.scala._
import org.apache.flink
import org.apache.flink.streaming.api.datastream.{DataStream, SingleOutputStreamOperator}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

import java.util.Properties


object D01 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    // 1. 从集合中读取数据
    val readings: List[SensorReading] = List(
      SensorReading("a", 1616213079, 39.8),
      SensorReading("a", 1616213089, 35.8),
      SensorReading("b", 1616213279, 36.8),
      SensorReading("b", 1616213379, 35.8),
      SensorReading("d", 1616213779, 37.8))

//    val values: DataStream[SensorReading] = env.fromCollection(readings)
//    values.print()
//    // 任意类型
//    env.fromElements(1,2,4,"hello")
//
//    // 读取文件
//    val textStream: DataStream[String] = env.readTextFile("hello.txt")
//
//    val value: SingleOutputStreamOperator[String] = textStream.map(_.concat(" "))
    
    // 从kafka中读取数据
    val pro: Properties = new Properties()
    pro.setProperty("bootstrap.servers", "localhost:9092")
    pro.setProperty("group.id", "wc")

//    env.addSource(new FlinkKafkaConsumer[String]())



    env.execute("list")



  }

}


