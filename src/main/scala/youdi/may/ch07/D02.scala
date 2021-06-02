package youdi.may.ch07

import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

object D02 {
  def main(args: Array[String]): Unit = {


    val settings: EnvironmentSettings = EnvironmentSettings.newInstance.inStreamingMode.build
    StreamTableEnvironment.create(settings)
  
  }
}
