package youdi.com.datasource

import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala._


class MySource[T] extends SourceFunction[T] {
  override def run(ctx: SourceFunction.SourceContext[T]): Unit = {
      
  }

  override def cancel(): Unit = {

  }
}
