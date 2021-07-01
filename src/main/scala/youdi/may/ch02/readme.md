### flink安装部署

### 1. local本地模式



```shell
./start-cluster.sh


jps
52876 StandaloneSessionClusterEntrypoint
53119 TaskManagerRunner

```



### 2. standalone独立集群模式

`flink-conf.yaml`



```shell
1. 修改配置 历史服务器
2. 启动
   ./start-cluster
   或者

./jobmanager.sh
./taskmanager.sh
```




jobmanager
taskmanager


问题：
1. 单点问题


### HA

```shell
修改flink配置
high-availability: zookeeper
high-availability.storageDir: hdfs:///flink/ha/


修改masters
/flink/conf/masters 加上master


```



### flink on yarn模式
在实际开发中，使用Flink时，更多的使用方式是Flink On Yarn模式，原因如下：
1.Yarn的资源可以按需使用，提高集群的资源利用率
2.Yarn的任务有优先级，根据优先级运行作业
3.基于Yarn调度系统，能够自动化地处理各个角色的 Failover(容错)
* JobManager 进程和 TaskManager 进程都由 Yarn NodeManager 监控
* 如果 JobManager 进程异常退出，则 Yarn ResourceManager 会重新调度 JobManager 到其他机器
* 如果 TaskManager 进程异常退出，JobManager 会收到消息并重新向 Yarn ResourceManager 申请资源，重新启动 TaskManager






### flink 实际应用场景

1. 实时大屏
2. 实时采集 实时计算
3. 实时仓库， ETL， data pipeline



state manager
表达能力

生态完善
批流一体



