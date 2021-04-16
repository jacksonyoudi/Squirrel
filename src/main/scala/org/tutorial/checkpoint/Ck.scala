package org.tutorial.checkpoint

import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment


object Ck {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    // 每 1000ms 开始一次 checkpoint
    env.enableCheckpointing(1000)

    // 设置模式为精确一次 (这是默认值)
    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)


    // 确认 checkpoints 之间的时间会进行 500 ms
    env.getCheckpointConfig.setMinPauseBetweenCheckpoints(500)

    // Checkpoint 必须在一分钟内完成，否则就会被抛弃
    env.getCheckpointConfig.setCheckpointTimeout(600000)

    // 如果 task 的 checkpoint 发生错误，会阻止 task 失败，checkpoint 仅仅会被抛弃
    env.getCheckpointConfig.setFailOnCheckpointingErrors(false)
    // 默认就是 0次
    env.getCheckpointConfig.setTolerableCheckpointFailureNumber(0)


    // 同一时间只允许一个 checkpoint 进行
    env.getCheckpointConfig.setMaxConcurrentCheckpoints(1)


    /**
     * MemoryStateBackend
     * FsStateBackend
     * RocksDBStateBackend
     */

    env.setStateBackend(FsStateBackend)

    env.execute
  }
}
