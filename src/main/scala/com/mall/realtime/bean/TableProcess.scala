package com.mall.realtime.bean

object TableProcess {
  val SINK_TYPE_HBASE: String = "hbase"
  val SINK_TYPE_KAFKA: String = "kafka"
  val SINK_TYPE_CK: String = "ck"

}


class TableProcess {
  var souceTable: String = _
  var operateType: String = _
  var sinkType: String = _
  var sinkTable: String = _
  var sinkColumns: String = _
  var sinkPk: String = _
  var sinkExtend: String = _
}