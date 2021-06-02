package youdi.may.ch05.connecters

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.datastream.DataStreamSource
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}

import java.util.Properties


/**
 *
 * 使用kafka source
 */
object KafkaD {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)
    val properties: Properties = new Properties

    properties.setProperty("bootstrap.servers", "localhost:9092")
    properties.setProperty("group.id", "flink")
    properties.setProperty("auto.offset.reset", "latest")
    properties.setProperty("flink.partition-discovery.interval-millis", "5000") // 动态分区检查
    properties.setProperty("enable.auto.commit", "true") // 自动提交，后续学习了 checkponint

    val ds: DataStreamSource[String] = env.addSource(
      new FlinkKafkaConsumer[String]("flink_kafka", new SimpleStringSchema(), properties)
    )


    // 实现了RichSinkFunction， 支持两阶段提交
    ds.addSink(new FlinkKafkaProducer[String]("flink_producer", new SimpleStringSchema, properties))
    env.execute("kafka")
  }
}
