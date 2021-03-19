package youdi.com.datasource

import org.apache.flink.streaming.api.functions.source.SourceFunction

class HbaseSource[String] extends SourceFunction[String] {
  val Vesion: Int = 1
  var isRunning: Boolean = true

  override def run(ctx: SourceFunction.SourceContext[String]): Unit = {
    while (isRunning) {
      ctx.collect(new String("hello"))
    }

  }

  override def cancel(): Unit = {
    isRunning = false
  }
}
