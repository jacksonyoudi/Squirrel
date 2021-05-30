package youdi.may.ch06.state

import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.runtime.state.{FunctionInitializationContext, FunctionSnapshotContext}
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction
import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.createTypeInformation

import java.util


object MyKafkaSource extends RichParallelSourceFunction with CheckpointedFunction {
  private var offsetState: ListState[Long] = null
  private var offset: Long = 0L
  private var flag: Boolean = true

  override def snapshotState(context: FunctionSnapshotContext): Unit = {
    offsetState.clear()
    offsetState.add(offset)
  }

  override def initializeState(context: FunctionInitializationContext): Unit = {
    val desc: ListStateDescriptor[Long] = new ListStateDescriptor("offsetState", createTypeInformation[Long])
    offsetState = context.getOperatorStateStore.getListState(desc)
  }

  override def run(ctx: SourceFunction.SourceContext[String]): Unit = {
    val it: util.Iterator[Long] = offsetState.get.iterator()
    if (it.hasNext) {
      offset = it.next()
    }

    while (flag) {
      offset += 1
      val subtask: Int = getRuntimeContext.getIndexOfThisSubtask
      ctx.collect("subtask" + subtask + " offset:" + offset)
    }


  }

  override def cancel(): Unit = {
    flag = false

  }
}
