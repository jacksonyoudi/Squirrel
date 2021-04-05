package org.youdi.advanced.state

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.runtime.executiongraph.restart.RestartStrategy
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.environment.CheckpointConfig
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.runtime.state.{FunctionInitializationContext, FunctionSnapshotContext}
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction
import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}

import java.util


/**
 *
 */
object D02 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)

    env.setParallelism(1)
    env.setStateBackend(new FsStateBackend("/tmp/ckp"))
    env.enableCheckpointing(1000)

    env.getCheckpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)

    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)

    env.setRestartStrategy(RestartStrategies.fixedDelayRestart(2, 3000))

    val ds: DataStream[String] = env.addSource(new MyKafkaSource())
    ds.print()


    env.execute("kafka commit")
  }
}


class MyKafkaSource extends RichParallelSourceFunction[String] with CheckpointedFunction[String] {
  // 声明liststate
  var offsetState: ListState[Long] = null
  var offset: Long = 0L


  override def run(ctx: SourceFunction.SourceContext[String]): Unit = {

    while (true) {
      val iterator: util.Iterator[Long] = offsetState.get.iterator
      if (iterator.hasNext) {
        offset = iterator.next
      }
      offset += 1L

      if (offset % 5 == 0) {
        throw new Exception("bug出现了....")
      }

      Thread.sleep(1000)
      val subtask: Int = getRuntimeContext.getIndexOfThisSubtask

      ctx.collect("subTaskId" + subtask + "当前offset值是" + offset)

    }
  }

  override def cancel(): Unit = {

  }

  // 定时执行， 快照
  override def snapshotState(context: FunctionSnapshotContext): Unit = {
    offsetState.clear() // 清空内容，将数据保存到磁盘目录中
    offsetState.add(offset) //  添加新数据

  }

  override def initializeState(context: FunctionInitializationContext): Unit = {
    val stateDescipt: ListStateDescriptor[Long] = new ListStateDescriptor[Long]("state", classOf[Long])
    offsetState = context.getOperatorStateStore.getListState(stateDescipt)
  }
}