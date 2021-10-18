package com.mall.realtime.app.func

import com.alibaba.fastjson.JSONObject
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala.OutputTag
import org.apache.flink.util.Collector
import com.mall.realtime.bean.TableProcess
import com.mall.realtime.common.MallConfig
import com.mall.realtime.utils.MySQLUtil
import org.apache.flink.configuration.Configuration

import java.sql.{Connection, DriverManager}
import java.util.{Timer, TimerTask}
import scala.collection.mutable

class TableProcessFunction extends ProcessFunction[JSONObject, JSONObject] {
  private var outputTag: OutputTag[JSONObject] = _

  private var tableProcessMap: mutable.HashMap[String, TableProcess] = new mutable.HashMap()

  private var existsTables: mutable.HashSet[String] = new mutable.HashSet[String]()

  private var conn: Connection = null

  def this(out: OutputTag[JSONObject]) {
    this()
    outputTag = out
  }


  def refreshMeta(): Unit = {
    println("refreshMeta")
    val list: List[Any] = MySQLUtil.QueryList("select * from tab", TableProcess.getClass, true)
    //========2.将从配置表中查询到配置信息，保存到内存的map集合中=============

    //========3.如果当前配置项是维度配置，需要向Hbase表中保存数据，那么我们需要判断phoenix中是否存在这张表=====================


  }


  //在函数被调用的时候执行的方法，执行一次
  override def open(parameters: Configuration): Unit = {
    Class.forName("org.apache.phoenix.jdbc.PhoenixDriver")
    conn = DriverManager.getConnection(MallConfig.PHOENIX_SERVER)

    refreshMeta()

    //开启一个定时任务
    // 因为配置表的数据可能会发生变化，每隔一段时间就从配置表中查询一次数据，更新到map，并检查建表
    //从现在起过delay毫秒后，每隔period执行一次

    val timer: Timer = new Timer()
    timer.schedule(
      new TimerTask {
        override def run(): Unit = {
          refreshMeta()
        }
      }, 5000, 50000
    )
  }

  //每过来一个元素，方法执行一次，主要任务是根据内存中配置表Map对当前进来的元素进行分流处理
  override def processElement(value: JSONObject, ctx: ProcessFunction[JSONObject, JSONObject]#Context, out: Collector[JSONObject]): Unit = {
    val table: String = value.getString("table")
    var typ: String = value.getString("type")

    //注意：问题修复  如果使用Maxwell的Bootstrap同步历史数据  ，这个时候它的操作类型叫bootstrap-insert

    if ("bootstrap-insert".equals(typ)) {
      typ = "insert"
      value.put("type", typ)
    }

    if (tableProcessMap != null && tableProcessMap.size > 0) {
      //从内存的配置Map中获取当前key对象的配置信息

      //获取sinkTable，指明当前这条数据应该发往何处  如果是维度数据，那么对应的是phoenix中的表名；如果是事实数据，对应的是kafka的主题名

      //如果sinkType = hbase ，说明是维度数据，通过侧输出流输出
      ctx.output(outputTag, value)

    } else {
      //如果sinkType = kafka ，说明是事实数据，通过主流输出

      out.collect(value)
    }

  }
}
