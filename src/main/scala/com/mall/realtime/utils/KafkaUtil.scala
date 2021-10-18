package com.mall.realtime.utils

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer, KafkaSerializationSchema}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig

import java.util.Properties


object KafkaUtil {
  val KAFKA_SEVER: String = "hadoop202:9092,hadoop203:9092,hadoop204:9092"
  val DEFUALT_TOPIC: String = "defalut_kafka"

  def getKakfaSouce(topic: String, groupId: String): FlinkKafkaConsumer[String] = {
    val properties: Properties = new Properties
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId)
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SEVER)

    new FlinkKafkaConsumer[String](topic, new SimpleStringSchema, properties)
  }


  def getKafkaSink(topic: String): FlinkKafkaProducer[String] = {
    new FlinkKafkaProducer[String](KAFKA_SEVER, topic, new SimpleStringSchema)
  }

  def getKafkaSinkBySchema(kafkaSerializationSchema: KafkaSerializationSchema[T]): Unit = {
    val properties: Properties = new Properties
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SEVER)
    //设置生产数据的超时时间
    properties.setProperty(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 15 * 60 * 1000 + "")
    new FlinkKafkaProducer[T](DEFUALT_TOPIC, kafkaSerializationSchema, properties, FlinkKafkaProducer.Semantic.EXACTLY_ONCE)

  }


}
