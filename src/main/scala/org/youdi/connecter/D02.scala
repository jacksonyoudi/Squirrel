package org.youdi.connecter

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}

import java.util.Properties

object D02 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)


    // 消费者
    val props: Properties = new Properties()
    props.setProperty("bootstrap.servers", "localhost:9092")
    props.setProperty("group.id", "flink")
    props.setProperty(" auto.offset.reset", "latest")
    props.setProperty("flink.partition-discovery.interval-millis", "5000")
    props.setProperty("enable.auto.commit", "true")
    props.setProperty("auto.commit.interval.ms", "2000")


    val consumer: FlinkKafkaConsumer[String] = new FlinkKafkaConsumer("flink_kafka", new SimpleStringSchema(), props)
    consumer.setStartFromEarliest() // 尽可能从最早的记录开始
    //    consumer.setStartFromLatest()        // 从最新的记录开始
    //    consumer.setStartFromTimestamp(...)  // 从指定的时间开始（毫秒）
    //    consumer.setStartFromGroupOffsets()  // 默认的方法

    val ds: DataStream[String] = env.addSource(consumer)
    ds.print()

    val props2: Properties = new Properties()
    props2.setProperty("bootstrap.servers", "localhost:9092")

    //  生产者
    val producer: FlinkKafkaProducer[String] = new FlinkKafkaProducer("flink_kafka_v2", new SimpleStringSchema(), props2)

    ds.filter(_.contains("hello")).addSink(producer)

    env.execute("kafka connect")
  }

}
