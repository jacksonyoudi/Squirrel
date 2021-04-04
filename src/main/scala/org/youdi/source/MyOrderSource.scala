package org.youdi.source

import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}

import java.util.UUID
import scala.util.Random

class MyOrderSource extends RichParallelSourceFunction[Order] {
  var flag: Boolean = true

  // 执行并生成数据
  override def run(ctx: SourceFunction.SourceContext[Order]): Unit = {
    while (flag) {
      val oid: String = UUID.randomUUID().toString
      val userId: Int = Random.nextInt(10)
      val money: Double = Random.nextDouble()
      val createTime: Long = System.currentTimeMillis()
      ctx.collect(Order(oid, userId, money, createTime))

      Thread.sleep(1000)
    }
  }

  //
  override def cancel(): Unit = {
    this.flag = false
  }
}
