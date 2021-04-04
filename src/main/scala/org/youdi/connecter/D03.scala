package org.youdi.connecter

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.connectors.redis.RedisSink
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig
import org.apache.flink.streaming.connectors.redis.common.mapper.{RedisCommand, RedisCommandDescription, RedisMapper}

import java.util.Properties

object D03 {
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
    val result: DataStream[(String, Int)] = ds.flatMap(_.split(" ")).map((_, 1))
      .keyBy(_._1)
      .sum(1)

    result.print()
    val config: FlinkJedisPoolConfig = new FlinkJedisPoolConfig.Builder().setHost("127.0.0.1").build()
    val rs: RedisSink[(String, Int)] = new RedisSink(config, new MyRedisConnect())
    result.addSink(rs)


    env.execute("redis")
  }

}

class MyRedisConnect extends RedisMapper[(String, Int)] {
  override def getCommandDescription: RedisCommandDescription = {
    // 告诉使用命令
    //    new RedisCommandDescription(RedisCommand.HSET, "HASH_NAME")
    new RedisCommandDescription(RedisCommand.SET, "HASH_NAME")
  }

  override def getKeyFromData(data: (String, Int)): String = {
    data._1
  }

  override def getValueFromData(data: (String, Int)): String = {
    data._2.toString
  }
}