package org.tutorial.cep

import java.util

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.cep.functions.PatternProcessFunction
import org.apache.flink.cep.scala.{CEP, PatternStream}
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector


object D01 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)
    // 默认就是 事件事件
    val input: DataStream[Event] = env.socketTextStream("", 111).map(
      a => {
        val words: Array[String] = a.split(" ")
        Event(words(0), words(1).toInt, words(2))
      }
    )

    val pattern: Pattern[Event, Event] = Pattern.begin[Event]("start").where(_.id == "hello")
      .next("middle").subtype(classOf[Event]).where(_.volume >= 10)
      .followedBy("end").where(_.name == "end")

    val patternStream: PatternStream[Event] = CEP.pattern(input, pattern)
    patternStream.process(
      new PatternProcessFunction[Event, Alert] {
        override def processMatch(map: util.Map[String, util.List[Event]], context: PatternProcessFunction.Context, collector: Collector[Any]) = {
          collector.collect(Alert(pattern))
        }
      }
    )


    env.execute
  }
}

case class Event(
                  id: String,
                  volume: Int,
                  name: String,
                )

case class Alert(
                  pattern: Pattern[Event,Event],
                )