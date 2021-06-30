package org.youdi.cep

import java.util

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.cep.functions.PatternProcessFunction
import org.apache.flink.cep.scala.{CEP, PatternStream}
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.scala.{CloseableIterator, DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.util.Collector
import org.tutorial.cep.Alert
import org.youdi.source.{MyOrderSource, Order}


object D01 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)

    val ds: DataStream[Order] = env.addSource[Order](new MyOrderSource())
    val pt: Pattern[Order, Order] = Pattern.begin[Order]("start").where(_.userId == 42)
      .next("middle").subtype(classOf[Order]).where(_.money >= 100)
      .followedBy("end").where(_.id == 10)

    val ptDs: PatternStream[Order] = CEP.pattern(ds, pt)

    ptDs.process(
      new PatternProcessFunction[Order, Alert]() {
        override def processMatch(map: util.Map[String, util.List[Order]], context: PatternProcessFunction.Context, collector: Collector[Alert]) = {
          collector.collect(Alert(pt))
        }
      }
    )


    env.execute("cep")
  }
}


case class Alert(
                  pattern: Pattern[Order, Order]
                )
