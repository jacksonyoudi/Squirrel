package org.youdi.table.sql

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.table.api.{EnvironmentSettings, Table}
import org.apache.flink.table.api.Expressions.$
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment


object D02 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val bsSettings: EnvironmentSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build()
    val tenv: StreamTableEnvironment = StreamTableEnvironment.create(env, bsSettings)
    val ds: DataStream[String] = env.socketTextStream("localhost", 9876)
    val wDS: DataStream[WordCount] = ds.flatMap(_.split(" ")).map(WordCount(_, 1))


    tenv.createTemporaryView("word_tab", wDS, $("word"), $("cnt"))

    val sql: String = "select word, sum(cnt) as c from word_tab group by word"
    val resultTable: Table = tenv.sqlQuery(sql)
    resultTable.printSchema()


    val rDS: DataStream[(Boolean, WordCount)] = tenv.toRetractStream[WordCount](resultTable)
    rDS.print("data")


    env.execute()
  }
}

case class WordCount(word: String, cnt: Int)
