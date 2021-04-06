package org.youdi.table.sql

import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.table.api.{EnvironmentSettings, Table}
import org.apache.flink.table.api.Expressions.$
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.types.Row
import org.youdi.source.{MyOrderSource, Order}

import java.time.Duration

/**
 *
 */
object D03 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val settings: EnvironmentSettings = EnvironmentSettings.newInstance.useBlinkPlanner.inStreamingMode.build

    val tabEnv: StreamTableEnvironment = StreamTableEnvironment.create(env, settings)

    val ds: DataStream[Order] = env.addSource(new MyOrderSource())
    val windowDS: DataStream[Order] = ds.assignTimestampsAndWatermarks(
      WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(5))
        .withTimestampAssigner(
          new SerializableTimestampAssigner[Order] { //
            override def extractTimestamp(element: Order, recordTimestamp: Long): Long = element.createTime // 指定事件时间
          }
        )
    )


    tabEnv.createTemporaryView("orders", windowDS, $("id"), $("userId"), $("money"), $("createTime").rowtime)


    val sql: String = "select " +
      "userId," +
      "count(*) as totalCount," +
      "max(money) as maxMoney," +
      "min(money) as minMoney " +
      "from orders " +
      "group by userId," +
      "tumble(createTime, interval '5' second)";

//    val sql: String = "select userid,count(id) as order_cnt, max(money) as max_money, min(money) as min_money from orders group by userId,tumble(createTime, INTERVAL '5' SECOND)"

    val table: Table = tabEnv.sqlQuery(sql)

    val resultDS: DataStream[(Boolean, Row)] = tabEnv.toRetractStream[Row](table)
    resultDS.print("result")


    env.execute
  }
}


//case class Order(
//                  id: String,
//                  userId: Int,
//                  money: Double,
//                  createTime: Long
//                )