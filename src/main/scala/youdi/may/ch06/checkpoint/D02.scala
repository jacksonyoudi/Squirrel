package youdi.may.ch06.checkpoint

import org.apache.calcite.avatica.util.TimeUnit
import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.windowing.time.Time

object D02 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)

    env.enableCheckpointing(1000)
    env.setRestartStrategy(
      RestartStrategies.fixedDelayRestart(
        3,
        100,
      )
    )

    val ds: DataStream[String] = env.socketTextStream("localhost", 9876)

    ds.flatMap(_.split(" "))
      .map(
        (a) => {
          if (a.contains("bug")) {
            throw new Exception("bug")
          }

          (a, 1)
        }

      ).print("wc")


    env.execute("")


  }
}
