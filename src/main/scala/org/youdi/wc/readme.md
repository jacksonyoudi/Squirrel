### flink


提交
 ```bash
 flink run -Dexecution.runtime-mode=BATCH -m yarn-cluster 
 -yjm 1024
 -c org.youdi.WC 
 /xx.jar
 --output xxx
 ```




### flink角色分工

#### client
1. 提交任务
2. 代码生成 dataflow
3. 优化 graphabuilder
4. dataflow grapha
5. actor System


#### jobManager

 
#### taskManager



### Streaming DataFlow


source operator
trasformationOperator
sinkOperator

condensed view


one-to-one
redistributing


parallelized view


分区的一系列操作叫做 subtask
有几个并行的， 叫做并行度
任务链

1. dataflow 
flink程序在执行的时候会被映射成一个数据流模型
   
2. operator
   数据流模型中的每一个操作被称作operator，operator分为source、transform, sink

3. partition
    数据流模型是分布式的和并行的， 执行中会形成1-n个分区
   
4. subtask
多个分区任务可以并行，每一个都是独立运行在一个线程中的，也就是一个subtask子任务
   
5. parallism：并行度； 就是可以同时真正运行的子任务数/分区数




### 传递方式
one-to-one
redistribution


one-to-one operator可以合并一个执行链 

每个operatorChain或者无法合并的单个operator叫做Task



### taskSLot

taskManager是进程
taskslot是线程

线程slot

taskmanager进程中可以同时运行多少个线程有taskslot决定的

taskSlot是资源分配的最小单位


### slot Sharing
taskslot跑完一些线程，可以重复


### 执行图

streamGraph：
最初的程序执行流程图，也就是算子之间的前后顺序  在client上生成的

JobGraph： 将onetoone的operator合并为operatorchain 在client上生成的
ExecutionGraph:
将jobgraph根据代码中设置的并行度和请求的资源进行并行规划， 在jobmanager上生成的

物理执行图：
将excutionGraph的并行计划，落实到具体的taskManager上， 将具体的subtask落实到具体的taskslot内进行运行。






### 流处理相关概念

流： 
    - 有界 boundedstreaming
    - 无界 unboundedstreaming


时效性：
特征不同
应用场景不同
运行方式不同



### 流批一体API




