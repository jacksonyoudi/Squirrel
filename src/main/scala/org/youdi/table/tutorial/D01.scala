package org.youdi.table.tutorial

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.api.scala._
import org.apache.flink.table.api._
import org.apache.flink.table.api.bridge.scala._
import org.apache.flink.table.descriptors.{FileSystem, Schema}


object D01 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)
    val tabEnv: StreamTableEnvironment = StreamTableEnvironment.create(env)


    val ds: DataStream[(Long, String, Int)] = env.socketTextStream("", 9876).flatMap(_.split(" ")).map(a => (
      a.toLong, a, a.toInt))


    // 这里有隐式转换
    val table: Table = ds.toTable(tabEnv, $"user", $"product", $"amount")

    val sql: String =
      """
       select
             sum(amount)
       from
          $table
       where
          product like 'Rubber'
       """

    val result: Table = tabEnv.sqlQuery(sql)
    tabEnv.createTemporaryView("orders", ds, $"user", $"product", $"amount")

    val result2: Table = tabEnv.sqlQuery(
      "SELECT product, amount FROM Orders WHERE product LIKE '%Rubber%' "
    )

    // 创建并注册一个 TableSink
    val schema: Schema = new Schema()
      .field("product", DataTypes.STRING())
      .field("amount", DataTypes.INT())

    tabEnv.connect(new FileSystem().path("orders"))
      .withSchema(schema)
      //        .withFormat()
      .createTemporaryTable("sinkOrders")

    // 在表上执行插入操作，并将结果发出到tabSink上
    tabEnv.execute("INSERT INTO RubberOrders SELECT product, amount FROM Orders WHERE product LIKE '%Rubber%'")

    env.execute
  }
}
