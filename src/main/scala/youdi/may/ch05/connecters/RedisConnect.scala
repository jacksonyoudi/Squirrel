package youdi.may.ch05.connecters

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.redis.RedisSink
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig

object RedisConnect {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)


    val ds: DataStream[String] = env.socketTextStream("localhost", 9876)
    val result: DataStream[(String, Int)] = ds
      .flatMap(_.split(" "))
      .map((_, 1))
      .keyBy(_._1)
      .sum(1)


    val config: FlinkJedisPoolConfig = new FlinkJedisPoolConfig.Builder().setHost("127.0.0.1").build()

    result.addSink(new RedisSink(config, MyRedisMapper))

    env.execute("redis")
  }

}
