package org.youdi.advanced.checkpoint

import com.typesafe.sslconfig.ssl.FakeKeyStore
import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.api.common.time.Time
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}

import java.util.concurrent.TimeUnit

object D01 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)

    // 每 1000ms 开始一次 checkpoint
    env.enableCheckpointing(1000)

    env.setStateBackend(new FsStateBackend("file:///tmp/flink-state"))

    //    env.setRestartStrategy(RestartStrategies.noRestart())

    // 固定延迟无重启
    //    env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
    //      3,
    //      Time.of(5, TimeUnit.SECONDS)
    //    ))


    env.setRestartStrategy(RestartStrategies.failureRateRestart(
      3, // 每个测量阶段内的最大失败次数
      Time.of(5, TimeUnit.MINUTES), // 失败率测量的时间间隔
      Time.of(3, TimeUnit.SECONDS), // 两次连续重启的时间间隔
    ))



    // 失败率重启

    // 高级选项：

    // 设置模式为精确一次 (这是默认值)
    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)

    // 确认 checkpoints 之间的时间会进行 500 ms
    env.getCheckpointConfig.setMinPauseBetweenCheckpoints(500)

    // Checkpoint 必须在一分钟内完成，否则就会被抛弃
    env.getCheckpointConfig.setCheckpointTimeout(60000)

    // 设置失败次数
    env.getCheckpointConfig.setTolerableCheckpointFailureNumber(100)
    // 如果 task 的 checkpoint 发生错误，会阻止 task 失败，checkpoint 仅仅会被抛弃
    //    env.getCheckpointConfig.setFailTasksOnCheckpointingErrors(false)


    //    env.getCheckpointConfig.enableExternalizedCheckpoints()

    // 同一时间只允许一个 checkpoint 进行
    env.getCheckpointConfig.setMaxConcurrentCheckpoints(1)


    val ds: DataStream[String] = env.socketTextStream("localhost", 9876)


    ds.flatMap(_.split(" "))
      .map(
        a => {
          if (a.contains("bug")) {
            throw new Exception("bug")
          } else {
            (a, 1)
          }
        }
      ).keyBy(_._1)
      .sum(1).print()


    env.execute("ckeckpoint")
  }

}
