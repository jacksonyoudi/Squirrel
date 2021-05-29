package youdi.may.ch06.time

import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}
import youdi.may.ch05.Order

import java.util.{Random, UUID}

object GenOrderSource extends RichParallelSourceFunction[Order] {
  var flag: Boolean = true

  override def run(ctx: SourceFunction.SourceContext[Order]): Unit = {
    val random: Random = new Random
    while (flag) {
      val oid: String = UUID.randomUUID().toString
      val userId: Int = random.nextInt(3)
      val money: Double = random.nextInt(4).toDouble
      // 模拟延迟
      val createTime: Long = System.currentTimeMillis() - random.nextInt(50) * 1000
      ctx.collect(Order(oid, userId, money, createTime))
      Thread.sleep(1000)
    }
  }

  override def cancel(): Unit = {
    flag = false
  }
}
