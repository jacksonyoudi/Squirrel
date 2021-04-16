package org.youdi.advanced.sql


import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.annotation.DataTypeHint
import org.apache.flink.table.api._
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.table.functions.ScalarFunction
import scala.annotation.varargs
import org.apache.flink.table.annotation.InputGroup
import org.apache.flink.types.Row

object UDFDemo {
  def main(args: Array[String]): Unit = {
    val bsEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val bsSettings: EnvironmentSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build()
    val tenv: StreamTableEnvironment = StreamTableEnvironment.create(bsEnv, bsSettings)


    // 在 Table API 里不经注册直接“内联”调用函数
    tenv.from("test").select(call(classOf[SubstringFunction], $"myfield", 5, 12))

    // 注册函数
    tenv.createTemporarySystemFunction("SubstringFunction", classOf[SubstringFunction])
    // 在 Table API 里调用注册好的函数
    tenv.from("MyTable").select(call("SubstringFunction", $"myField", 5, 12))

    // 在 SQL 里调用注册好的函数
    tenv.sqlQuery("SELECT SubstringFunction(myField, 5, 12) FROM MyTable")

  }
}


class SubstringFunction extends ScalarFunction {
  def eval(s: String, begin: Int, end: Int): String = {
    s.substring(begin, end)
  }
}


// 定义可参数化的函数逻辑
class SubstringFunctionA(val endInclusive: Boolean) extends ScalarFunction {
  def eval(s: String, begin: Integer, end: Integer): String = {
    //    s.substring(endInclusive ? end + 1: stat, end)
    if (endInclusive) {
      s
    } else {
      s
    }
  }
}



// 有多个重载求值方法的函数
class SumFunction extends ScalarFunction {

  def eval(a: Integer, b: Integer): Integer = {
    a + b
  }

  def eval(a: String, b: String): Integer = {
    Integer.valueOf(a) + Integer.valueOf(b)
  }

  @varargs // generate var-args like Java
  def eval(d: Double*): Integer = {
    d.sum.toInt
  }
}