### Table API & SQL




#### 动态表， 连续查询
Dynamic table && continue queries


动态表 /无界流 
连续查询 / 需要借助state


将dataStream注册成table或view，




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

