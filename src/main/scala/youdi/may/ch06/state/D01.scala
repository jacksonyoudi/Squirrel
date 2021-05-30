package youdi.may.ch06.state

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._
import youdi.may.ch05.Order
import youdi.may.ch06.time.GenOrderSource


object D01 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)
    val ds: DataStream[Order] = env.addSource(GenOrderSource)

    //    ds.keyBy(_.userId).maxBy("money").print()


    ds
      .keyBy(_.userId)
      .map(
        new RichMapFunction[Order, Double]() {
          private var maxState: ValueState[Double] = null


          // 状态初始化
          override def open(parameters: Configuration) = {
            // 创建状态描述器
            val desc = new ValueStateDescriptor("maxMoney", Double.getClass)
            maxState = getRuntimeContext.getState(desc)


          }

          override def map(value: Order): Double = {
            val hisValue: Double = maxState.value


            if (hisValue == null || hisValue < value.money) {
              maxState.update(value.money)
              value.money
            } else {
              hisValue
            }


          }
        }
      ).print("")


    env.execute("")


  }

}
