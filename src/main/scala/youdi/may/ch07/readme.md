### Table API & SQL




#### 动态表， 连续查询
Dynamic table && continue queries


动态表 /无界流 
连续查询 / 需要借助state


将dataStream注册成table或view，




<<<<<<< HEAD
### 类型系统

1. sql和table是使用的 logicalType
2. datastream使用的是 TypeInfomation
3. row 

flink row本身不是强类型，需要row提供ROWtypeinfo来描述row中的数据类型，在序列化和反序列化使用


Blink
二进制 内存行式存储 列式存储


Typeinfomation#createSerializer


datastream类型系统分为 物理类型和逻辑类型 

kryo进行序列化， 



### 自主内存管理
1. 内存管理
2. 定制的序列化工具
3. 缓存友好的数据结构和算法
4. 堆外内存


memorySegment 是内存分配的最小单元  32KB

1. 堆外内存，不会触发GC和OOM
2. 堆外内存  zero-copy
3. 进程共享




1. 状态数据的存储和访问
2. 状态数据的备份和恢复
3. 状态数据的划分和动态扩容
4. 状态数据的清理



### 7.1 状态类型
ValueState
ListState
ReducingState
AggregatingState
MapState
FoldingState

#### 状态
RawState
ManagerState



#### 状态描述器

通过  RuntimeContext.getState 获取 state



#### 状态接口

1. 状态操作接口
2. 状态访问接口



#### 7.5 状态存储
1. state读写能力
2. 能够将state持久化到外部存储，提供容错能力



memorystateBackend

1. state保存在 jobmnanagerstate内存中， 受限jobmanager的内存大小
2. 默认 5mb 


### 状态重分布
调整并行度的关键是处理state



### 7.8 状态过期

StateTtlConfig 

stateDesciptor.enableTimeToLive(ttlconfig)



### 作业提交

1. flink client反射 main， streamgrapha -> jobgraph -> 提交集群
2. 集群收到 jobgraph 就开始调度执行了， 启动成功后开始消费数据

streamgraph -> jobgraph -> excutionGraph -> 物理执行拓扑 (task dag)



### stream graph

node
edge: 旁路 分区器 。。。
=======




### 流图

StreamExecutionEnvironment.execute() -> streamGraphgenerator()
-> transform -> addoperation() -> addstreamNode -> streamEdge



virtualStreamNode()

1. 存储 transformation的上游的id，进行递归进行转化
2. 确定共享的slot组
3. 添加算子到streamingGraph
4. 设置 state Selector
5. 设置并行度和最大并行度
6. 构造streamEdge的边，关联上下游streamnode


虚拟的transformation生成的时候，不会转化为streamNode, 而是添加为虚拟节点


jobgraph可以由 streamggraph和批处理optimizedplan转化得到



### 8.4 jobgraph

jobVertex, JobEdge InternalDiateDataSet

1. jobVertex 顶点
经过算子融合优化后符合条件的多个streamNode可能融合在一起生成一个JobVertex,
   
一个jobVertex包含一个或多个算子， jobVertex的输入是jobEdge， 输出是InternaldiateDataSet 


2. JobEdge

连接 jobvertex 和 internaldiateDataSet的边， 表示jobgraph中的一个数据流转通道，

上游是 internaldiateDataSet, 下游是 jobvertex 

jobEdge中的数据分发模式会直接影响执行时task之间的数据连接关系，是点对点的连接还是全连接

3. internalDiateDataSet
中间数据集，是一种逻辑结构， 用来表示jobvertex的输出，


### JobGraph生成过程

StreamingJobGraphGenerator.createJobGraph
-> StreamingJobGraphGenerator(streamGraph, jobID).createJobGraph()

1. 设置调度模式
   setChaining(hashes, legacyHashes);

   	setPhysicalEdges();

   	setSlotSharingAndCoLocation();

预处理完成后， 开始构建 点和边， 从source向下遍历 streamGraph, 逐步创建jobgraph, 
在创建的过程中同时完成算子融合 operatorchain优化

执行具体的chain和jobvertex生成，jobEdge的关联， internaldiateDataSet。

构建jobedge的时候，很重要的一点确定上游jobVertex和下游jobVertex的数据交换方式。



#### 8.4.3算子融合
1.融合条件
下游节点的入边为1
streamEdge下游节点对应算子不为null
StreamEdge的上下游节点拥有相同的slotSharingGroup，默认都是default。
下游算子的连接策略为ALWAYS。
StreamEdge的分区类型为ForwardPartitioner。
上下游节点的并行度一致。
当前StreamGraph允许chain。



### executionGraph


StreamGraph、JobGraph在Flink客户端中生成，然后提交给Flink集群。
JobGraph到ExecutionGraph的转换在JobMaster中完成，转换过程中的重要变化如下。

1. 加入并行度的概念，成为真正可调用的图结构
2. 生成了与JobVertex对应的ExecutionJobVertex和ExecutionVertex，
   与IntermediateDataSet对应的IntermediateResul
   t和IntermediateResultPartition等，并行将通过这些类实现。
   


### 8.5 执行图 
作业中所有的并行执行的task信息， task之间的关联关系，数据流转关系
>>>>>>> 38eaf3a621fb994b3a2989248b6e153ced01a2ec

