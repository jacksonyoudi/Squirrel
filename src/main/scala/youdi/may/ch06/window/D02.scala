package youdi.may.ch06.window

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.assigners.{ProcessingTimeSessionWindows, TumblingProcessingTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.scala._

object D02 {
  def main(args: Array[String]): Unit = {


    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)
    val ds: DataStream[String] = env.socketTextStream("localhost", 9876)

    val carDs: DataStream[CarInfo] = ds.map(
      (a) => {
        val ws: Array[String] = a.split(",")
        CarInfo(ws(0).toInt, ws(1).toInt)
      }
    )

    val result: DataStream[CarInfo] = carDs.keyBy(_.id)
      .window(
        ProcessingTimeSessionWindows.withGap(Time.seconds(5))
      ).sum("cnt")

    result.print("car session")


    env.execute("session")

  }
}
