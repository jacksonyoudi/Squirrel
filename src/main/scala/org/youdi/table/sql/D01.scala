package org.youdi.table.sql

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.table.api.{EnvironmentSettings, Table}
import org.apache.flink.table.api.Expressions.$
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.types.Row
import org.youdi.source.Student


/**
 *
 */
object D01 {
  def main(args: Array[String]): Unit = {
    val bsEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val bsSettings: EnvironmentSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build()
    val tenv: StreamTableEnvironment = StreamTableEnvironment.create(bsEnv, bsSettings)


    val dsA: DataStream[Student] = bsEnv.fromElements(Student(11, "one11", 20), Student(21, "two22", 30))
    val dsB: DataStream[Student] = bsEnv.fromElements(Student(1, "one", 20), Student(2, "two", 30))

    val tableA: Table = tenv.fromDataStream(dsA, $("id"), $("name"), $("age"))

    tenv.createTemporaryView("tableB", dsB, $("id"), $("name"), $("age"))

    // 查询
    /*
    select * from tableA where id > 10
    union
    select * from tableB where id < 10
     */

    val sql: String =
      "select * from " + tableA + " union select * from tableB"

    val resultTable: Table = tenv.sqlQuery(sql)

    resultTable.printSchema()

    System.out.println(resultTable)


    // table转成table

    val dsStudent: DataStream[Student] = tenv.toAppendStream[Student](resultTable)

    dsStudent.print()

    bsEnv.execute()
  }

}
