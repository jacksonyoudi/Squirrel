//package org.youdi.table.tutorial
//
//import java.time.Duration
//
//import org.apache.flink.api.common.RuntimeExecutionMode
//import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
//import org.apache.flink.streaming.api.scala._
//import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
//import org.apache.flink.api.scala._
//import org.apache.flink.streaming.api.CheckpointingMode
//import org.apache.flink.streaming.api.environment.ExecutionCheckpointingOptions
//import org.apache.flink.table.api._
//import org.apache.flink.table.api.bridge.scala._
//import org.apache.flink.table.descriptors.{FileSystem, Schema}
//
//
//object D01 {
//  def main(args: Array[String]): Unit = {
//    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
//    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)
//    val tabEnv: StreamTableEnvironment = StreamTableEnvironment.create(env)
//
//
//    val ds: DataStream[(Long, String, Int)] = env.socketTextStream("", 9876).flatMap(_.split(" ")).map(a => (
//      a.toLong, a, a.toInt))
//
//
//    // 这里有隐式转换
//    val table: Table = ds.toTable(tabEnv, $"user", $"product", $"amount")
//
//    val sql: String =
//      """
//       select
//             sum(amount)
//       from
//          $table
//       where
//          product like 'Rubber'
//       """
//
//    val result: Table = tabEnv.sqlQuery(sql)
//    tabEnv.createTemporaryView("orders", ds, $"user", $"product", $"amount")
//
//    val result2: Table = tabEnv.sqlQuery(
//      "SELECT product, amount FROM Orders WHERE product LIKE '%Rubber%' "
//    )
//
//    // 创建并注册一个 TableSink
//    val schema: Schema = new Schema()
//      .field("product", DataTypes.STRING())
//      .field("amount", DataTypes.INT())
//
//    tabEnv.connect(new FileSystem().path("orders"))
//      .withSchema(schema)
//      //        .withFormat()
//      .createTemporaryTable("sinkOrders")
//
//    // 在表上执行插入操作，并将结果发出到tabSink上
//    tabEnv.execute("INSERT INTO RubberOrders SELECT product, amount FROM Orders WHERE product LIKE '%Rubber%'")
//
//    // 设置 checkpoint 模式
//    tabEnv.getConfig.getConfiguration.set(ExecutionCheckpointingOptions.CHECKPOINTING_MODE, CheckpointingMode.EXACTLY_ONCE)
//
//    tabEnv.getConfig.getConfiguration.set(
//      ExecutionCheckpointingOptions.CHECKPOINTING_INTERVAL, Duration.ofSeconds(10))
//
//    // 建表
//    tabEnv.executeSql("CREATE TABLE Orders (`user` BIGINT, product STRING, amount INT) WITH (...)")
//
//
//    val result1: TableResult = tabEnv.sqlQuery("SELECT * FROM Orders").execute()
//    result1.print()
//
//
//    val result3: Table = tabEnv.sqlQuery(
//      """
//        |SELECT *
//        |FROM (
//        |   SELECT *,
//        |       ROW_NUMBER() OVER (PARTITION BY category ORDER BY sales DESC) as row_num
//        |   FROM ShopSales)
//        |WHERE row_num <= 5
//    """.stripMargin)
//
//
//    val table1: Table = tabEnv.sqlQuery(
//      """
//        |SELECT
//        |  user,
//        |  TUMBLE_START(rowtime, INTERVAL '1' DAY) as wStart,
//        |  SUM(amount)
//        | FROM Orders
//        | GROUP BY TUMBLE(rowtime, INTERVAL '1' DAY), user
//    """.stripMargin)
//
//    // 计算每日的 SUM(amount) （使用处理时间）
//    val table2: Table = tabEnv.sqlQuery(
//      "SELECT user, SUM(amount) FROM Orders GROUP BY TUMBLE(proctime, INTERVAL '1' DAY), user")
//
//    // 使用事件时间计算过去24小时中每小时的 SUM(amount)
//    val table3: Table = tabEnv.sqlQuery(
//      "SELECT product, SUM(amount) FROM Orders GROUP BY HOP(rowtime, INTERVAL '1' HOUR, INTERVAL '1' DAY), product")
//
//    // 计算每个以12小时（事件时间）作为不活动时间的会话的 SUM(amount)
//    val result4 = tabEnv.sqlQuery(
//      """
//        |SELECT
//        |  user,
//        |  SESSION_START(rowtime, INTERVAL '12' HOUR) AS sStart,
//        |  SESSION_END(rowtime, INTERVAL '12' HOUR) AS sEnd,
//        |  SUM(amount)
//        | FROM Orders
//        | GROUP BY SESSION(rowtime(), INTERVAL '12' HOUR), user
//    """.stripMargin)
//
//
//
//
//    env.execute
//  }
//}
