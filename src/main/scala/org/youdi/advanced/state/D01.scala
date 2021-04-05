package org.youdi.advanced.state

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}

/**
 *
 */
object D01 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)

    val ds: DataStream[(String, Int)] = env.fromElements(
      ("北京", 1),
      ("上海", 2),
      ("北京", 6),
      ("上海", 7),
      ("北京", 9),
      ("上海", 4)
    )

    //
    ds.keyBy(_._1).maxBy(1).print()


    //

    val res: DataStream[(String, Int, Int)] = ds.keyBy(_._1).map(
      new RichMapFunction[(String, Int), (String, Int, Int)] {
        var maxVaule: ValueState[Int] = null


        override def map(value: (String, Int)): (String, Int, Int) = {
          var currentValue: Int = value._2

          var hisValue: Int = maxVaule.value

          if (hisValue == null || currentValue > hisValue) {
            maxVaule.update(currentValue)
            hisValue = currentValue
          } else {
            maxVaule.update(hisValue)
          }

          (value._1, currentValue, hisValue)

        }

        override def open(parameters: Configuration) = {
          // 创建状态描述器
          val stateDescipt: ValueStateDescriptor[Int] = new ValueStateDescriptor("maxValueState", createTypeInformation[Int])

          // 获取上下文
          maxVaule = getRuntimeContext.getState(stateDescipt)

        }
      }

    )

    res.print("state manager")

    env.execute("state")
  }

}
