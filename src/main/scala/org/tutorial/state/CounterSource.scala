package org.tutorial.state

import java.util

import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.runtime.state.{FunctionInitializationContext, FunctionSnapshotContext}
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction
import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}

class CounterSource extends RichParallelSourceFunction[Long] with CheckpointedFunction {
  @volatile
  private var isRunning: Boolean = true

  private var offset: Long = 0L
  private var state: ListState[Long] = _

  override def snapshotState(context: FunctionSnapshotContext): Unit = {
    state.clear()
    state.add(offset)
  }

  override def initializeState(context: FunctionInitializationContext): Unit = {
    val state: ListState[Long] = context.getOperatorStateStore.getListState(
      new ListStateDescriptor[Long]("state", classOf[Long])
    )
    val iterator: util.Iterator[Long] = state.get.iterator()
    while (iterator.hasNext) {
      offset = iterator.next()
    }

  }

  override def run(ctx: SourceFunction.SourceContext[Long]): Unit = {
    val lock: AnyRef = ctx.getCheckpointLock
    while (isRunning) {
      // 原子操作
      lock.synchronized(
        {
          ctx.collect(offset)
          offset += 1
        }
      )
    }
  }

  override def cancel(): Unit = {
    isRunning = false
  }
}
