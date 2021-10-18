package com.mall.realtime.app.dwd

import com.mall.realtime.utils.KafkaUtil
import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, KafkaSerializationSchema}
import com.alibaba.fastjson.{JSON, JSONObject}
import com.mall.realtime.app.func.{DimSink, TableProcessFunction}
import com.mall.realtime.bean.TableProcess
import org.apache.flink.api.common.serialization.SerializationSchema
import org.apache.kafka.clients.producer.ProducerRecord

import java.lang


object BaseDBApp {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(10)


    // 开启Checkpoint，并设置相关的参数
    env.enableCheckpointing(5000, CheckpointingMode.EXACTLY_ONCE);
    env.getCheckpointConfig.setCheckpointTimeout(60000)
    env.setStateBackend(new FsStateBackend("/tmp"))

    //重启策略
    // 如果说没有开启重启Checkpoint，那么重启策略就是noRestart
    // 如果说没有开Checkpoint，那么重启策略会尝试自动帮你进行重启   重启Integer.MaxValue
    env.setRestartStrategy(RestartStrategies.noRestart)

    val topic: String = "ods_base_db_m"
    val groupId: String = "base_db_app_group"


    val kafkaSource: FlinkKafkaConsumer[String] = KafkaUtil.getKakfaSouce(topic, groupId)

    val jsonStrDS: DataStream[String] = env.addSource(kafkaSource)

    // 3.对DS中数据进行结构的转换      String-->Json
    val jsonDS: DataStream[JSONObject] = jsonStrDS.map(JSON.parseObject(_))

    //  ETL
    val MDS: DataStream[JSONObject] = jsonDS.filter(
      value => {
        val bool: Boolean = value.getString("table") != null && value.getJSONObject("data") != null && value.getString("data") != null
        bool
      }
    )

    //动态分流  事实表放到主流，写回到kafka的DWD层；如果维度表，通过侧输出流，写入到Hbase
    val hbasTag: OutputTag[JSONObject] = new OutputTag[JSONObject](TableProcess.SINK_TYPE_HBASE)

    val selDS: DataStream[JSONObject] = MDS.process(new TableProcessFunction(hbasTag))
    val hbaseDS: DataStream[JSONObject] = selDS.getSideOutput(hbasTag)

    hbaseDS.addSink(new DimSink())

    //    KafkaUtil.getKafkaSinkBySchema(
    //      new KafkaSerializationSchema[JSONObject]() {
    //
    //
    //        override def open(context: SerializationSchema.InitializationContext) = {
    //
    //        }
    //
    //        override def serialize(element: JSONObject, timestamp: lang.Long) = {
    //          null
    //        }
    //      }
    //    )

    //    selDS.addSink()
    env.execute()


  }

}
