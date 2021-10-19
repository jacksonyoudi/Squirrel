package com.mall.realtime.app.func

import com.alibaba.fastjson.JSONObject
import com.mall.realtime.common.MallConfig
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}

import java.sql.{Connection, DriverManager}


class DimSink extends RichSinkFunction[JSONObject] {
  private var conn: Connection = null


  override def open(parameters: Configuration): Unit = {
    //对连接对象进行初始化
    // 使用池化
    Class.forName("org.apache.phoenix.jdbc.PhoenixDriver")
    conn = DriverManager.getConnection(MallConfig.PHOENIX_SERVER)
  }

  override def invoke(value: JSONObject, context: SinkFunction.Context): Unit = {
    val tableName: String = value.getString("sink_table")
    val jSONObject: JSONObject = value.getJSONObject("data")
    // 向 //注意：执行完Phoenix插入操作之后，需要手动提交事务


    //如果当前做的是更新操作，需要将Redis中缓存的数据清除掉


  }


}
