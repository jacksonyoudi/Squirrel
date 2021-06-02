package youdi.may.ch05.connecters

import org.apache.flink.streaming.connectors.redis.common.mapper.{RedisCommand, RedisCommandDescription, RedisMapper}

object MyRedisMapper extends RedisMapper[(String, Int)] {
  override def getCommandDescription: RedisCommandDescription = {
    //
    new RedisCommandDescription(RedisCommand.HSET, "wcresult")
  }

  override def getKeyFromData(data: (String, Int)): String = {
    data._1
  }

  override def getValueFromData(data: (String, Int)): String = {
    data._2.toString
  }
}
