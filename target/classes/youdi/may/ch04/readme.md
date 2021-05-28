### flink入门


SQL  high level language
Table API decalarative DSL
DataStream / DataSet API core API 
Stateful Stream Processing Low-level building block
                    streams state [event]time







### yarn运行
命令提交


```shell
/export/server/flink/bin/flink run -Dexecution.runtime-mode=BATCH -m yarn-cluster 
-yjm 1024 -ytm 1024 
-c cn.itcast.hello.WordCount4_Yarn /root/wc.jar --output hdfs://node1:8020/wordcount/output_xx


```


```shell
-c class 



```


### flink角色分工


client： 
1. datagraha
2. 优化
3. 提交任务   

jobmanager
1. 调度，资源
2. checkpoint


task manager:
1. 干活 
2. 管理节点




### flink streaming DataFlow

1. dataFlow
2. operator
    source transform sink  => dataflow
3. partition
    分区 上的一系列操作是 subtask     
4. subtask
    

5. Parallelism
    有几个并行的subtask，就是subtask


one-to-one  窄依赖
redistribution 重分布 shuffle

操作链
operatorChain

多个one-to-one可以合并  可以切断

每个operatorChain就是task。


#### taskslot taskslot sharing
任务槽
taskManager进程中可以最多运行多少个线程由taskslot决定

每个TaskManager是一个JVM的进程, 为了控制一个TaskManager(worker)能接收多少个task，
Flink通过Task Slot来进行控制。
TaskSlot数量是用来限制一个TaskManager工作进程中可以同时运行多少个工作线程，TaskSlot 是一个 TaskManager 中的最小资源分配单位，一个 TaskManager 中有多少个 TaskSlot 就意味着能支持多少并发的Task处理。


sharing：
1. 资源最大化
2. 

### flink 执行图 ExecutionGraph


流程化 -> 流程化合并-> 并行化 -> 将任务分配给具体的taskslot，落实执行线程化


streamingGraph: 最初的程序执行逻辑图，也就是算子之间的前后顺序 在client执行
jobgrapha: 合并
executionGraph: 将jobgrapha根据代码中的设置的并行度和请求的资源进行并行化规划 -- jobmanager上生成  并行化


物理执行图： 将executionGraph的并行计划，落实到具体的taskManager上，将具体的subtask落实到具体的taskslot上执行。 





### 流处理概念

1. 数据的时效性
2. 






