package youdi.may.ch07

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.common.state.{KeyedStateStore, StateTtlConfig, ValueStateDescriptor}
import org.apache.flink.core.execution.PipelineExecutor
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.{EnvironmentSettings, Table}
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import youdi.may.ch05.{Order, OrderSource}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api._
import org.apache.flink.table.data.binary.NestedRowData


object D01 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)

    new StateTtlConfig

    new PipelineExecutor {}

    // setting使用 blink
    val settings: EnvironmentSettings = EnvironmentSettings.newInstance.useBlinkPlanner.inStreamingMode.build

    val tabEnv: StreamTableEnvironment = StreamTableEnvironment.create(env, settings)

    val ds: DataStream[Order] = env.addSource(new OrderSource)


//    val tableA: Table = tabEnv.fromDataStream(ds, $("id"), $("userId"), $("money"), $("createTime"))
    tabEnv.createTemporaryView("tableB", ds, $("id"), $("userId"), $("money"), $("createTime"))


    val result: Table = tabEnv.sqlQuery("select * from tableB")
    result.printSchema()


    val ds1: DataStream[Order] = tabEnv.toAppendStream[Order](result)
    ds1.print("order")

    env.execute("")
  }
}
