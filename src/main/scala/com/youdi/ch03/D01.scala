package com.youdi.ch03

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.dag.Transformation
import org.apache.flink.runtime.execution.Environment
import org.apache.flink.runtime.taskmanager.RuntimeEnvironment
import org.apache.flink.state.api.runtime.SavepointEnvironment
import org.apache.flink.streaming.api.environment.{LocalStreamEnvironment, RemoteStreamEnvironment, StreamContextEnvironment, StreamPlanEnvironment}
import org.apache.flink.streaming.api.functions.source.FileProcessingMode
import org.apache.flink.streaming.api.scala.{OutputTag, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.runtime.streamrecord.StreamElement

object D01 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)
    Environment

    RuntimeEnvironment
    SavepointEnvironment
    StreamElement

    /**
     * LocalStreamEnvironment
     * 本地执行环境，调试，开发
     *
     * main生成streamGraph, 转化为jobgraph,
     * 设置任务运行的配置信息
     * LocalFLinkMiniCluster
     * MiniClusterClient
     * jobgraph => minicluster
     */


    /**
     * RemoteStreamEnvironment
     * 在大规模数据中心中部署的Flink生成集群的执行环境
     * streamGraph => jobgraph
     * 设置任务运行的配置信息
     * 提交jobgraph到远程flink集群
     */


    //    StreamContextEnvironment

    /**
     * Cli命令行或者单元测试时候会被使用，执行步骤同上
     */


    //    StreamPlanEnvironment

    /**
     * 在Flink Web UI管理界面中可视化展现Job的时候，专门用来生成执行计划（实际上就是StreamGraph）
     */

    //    ScalaShellStreamEnvironment
    /*
    这是Scala Shell执行环境，可以在命令行中交互式开发Flink作业。其基本工作流程如下。
    1）校验部署模式，目前Scala Shell仅支持attached模式。
    2）上传每个作业需要的Jar文件。其余步骤与RemoteStreamEnvironment类似。
     */

    val ds: DataStream[String] = env.socketTextStream("", 1)
//    ds.map()

    env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC)

    env.execute("")
  }
}
