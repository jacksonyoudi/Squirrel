package youdi.may.ch05


import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}

import java.util.{Random, UUID}

class OrderSource extends RichParallelSourceFunction[Order] {
  var flag: Boolean = true

  override def run(ctx: SourceFunction.SourceContext[Order]): Unit = {
    val random: Random = new Random
    while (flag) {
      val oid: String = UUID.randomUUID().toString
      val userId: Int = random.nextInt(3)
      val money: Double = random.nextInt(4).toDouble
      val createTime: Long = System.currentTimeMillis()
      ctx.collect(Order(oid, userId, money, createTime))
      Thread.sleep(1000)
    }
  }

  override def cancel(): Unit = {
    flag = false
  }
}
