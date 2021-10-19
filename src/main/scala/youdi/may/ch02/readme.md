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


stateBackend 
窗口



架构模型
SQL
table api
Data stream /data state


基础模块 
stream 
transformation


source -> transformation -> sink




### 核心概念

streams
    有界 无界

state
   进行流式计算过程中的信息
一般作为容错和持久化，流式计算本质上就是 增量计算， 查询过去状态
状态在flink中有十分重要的作用，精准一次
fail 0ver  自动重启


time： 
    多种时间 

API：
    sql table stream

streams transformation 


算子链： 切换， 序列化


jobmanager
    调度任务， jobgraph-> excutionGraph, 协调checkpoint, 协调故障恢复，收集
job状态信息， 管理flink集群中的从节点 taskManager

taskManager: 实际负责执行机损

client： 


taskmanager JVM 

taskslot 内存隔离


架构 容错 语义

容错：
    

语义： 

反压： 
    spark streaming 速率

分布式阻塞队列


    

