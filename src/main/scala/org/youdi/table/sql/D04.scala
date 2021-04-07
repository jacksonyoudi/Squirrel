package org.youdi.table.sql

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.table.api.{EnvironmentSettings, Table, TableResult}
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.types.Row

object D04 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val settings: EnvironmentSettings = EnvironmentSettings.newInstance.useBlinkPlanner.inStreamingMode.build
    val tabEnv: StreamTableEnvironment = StreamTableEnvironment.create(env, settings)

    val outTableSql: String =
      """
        |
        |CREATE TABLE output_table (
        |  `user_id` BIGINT,
        |  `page_id` BIGINT,
        |  `status` STRING
        |) WITH (
        |  'connector' = 'kafka',
        |  'topic' = 'output_kafka',
        |  'properties.bootstrap.servers' = 'localhost:9092',
        |  'format' = 'json',
        |  'sink.partitioner' = 'round-robin'
        |)
        |""".stripMargin

    tabEnv.executeSql(outTableSql)


    // kafka注册表
    val inputTabSql: String =
      """
        |CREATE TABLE input_table (
        |  `user_id` BIGINT,
        |  `page_id` BIGINT,
        |  `status` STRING
        |) WITH (
        |  'connector' = 'kafka',
        |  'topic' = 'input_kafka',
        |  'properties.bootstrap.servers' = 'localhost:9092',
        |  'properties.group.id' = 'testGroup',
        |  'scan.startup.mode' = 'latest-offset',
        |  'format' = 'json'
        |)
        |""".stripMargin

    tabEnv.executeSql(inputTabSql)


    val selectSql: String =
      """
        |
        |select * from input_table where status = 'sucess'
        |
        |""".stripMargin


    val resultTable: Table = tabEnv.sqlQuery(selectSql)

    val rds: DataStream[(Boolean, Row)] = tabEnv.toRetractStream[Row](resultTable)
    rds.print()


    val insertSql: String = """insert into output_table select * from """ + resultTable
    tabEnv.executeSql(insertSql)

    env.execute()

  }

}
