package com.mall.realtime.app.dwd

import com.alibaba.fastjson.{JSON, JSONObject}
import com.mall.realtime.utils.KafkaUtil
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

import java.util.concurrent.TimeUnit

object BaseLogApp {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(8)


    env.enableCheckpointing(500, CheckpointingMode.EXACTLY_ONCE)
    env.getCheckpointConfig.setCheckpointTimeout(60000)
    env.setStateBackend(new FsStateBackend(""))

    env.setRestartStrategy(RestartStrategies.failureRateRestart(10, 100 * TimeUnit.MILLISECONDS, 5 * TimeUnit.MICROSECONDS))


    val topic :String = ""
    val groupId :String = "base_log_app_group"
    val source: FlinkKafkaConsumer[String] = KafkaUtil.getKakfaSouce(topic, groupId)

    val ds: DataStream[String] = env.addSource(source)

    val jsonDS: DataStream[JSONObject] = ds.map(JSON.parseObject(_))

    val keyedStream: KeyedStream[JSONObject, String] = jsonDS.keyBy(_.getJSONObject("common").getString("mid"))


    keyedStream.map(
      new RichMapFunction[JSONObject, JSONObject]() {
        
      }
    )




  }
}
