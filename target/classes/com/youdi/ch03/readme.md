### 3.1 环境对象

StreamExecutionEnvironment是flink应用开发时的概念， 表示流计算作业的执行环境，是作业开发的入口。数据源接口，
生成和转换ds的接口，数据sink的接口， 作业配置接口，作业启动的执行入口。 

Environment是运行时作业级别的概念，从StreamExecutionEnvironment中的配置信息衍生而来。
进入到Flink作业执行的时刻，作业需要的是相关的配置信息，
如作业的名称、并行度、作业编号Job ID、监控的Metric、容错的配置信息、IO等，
用StreamExecutionRuntime对象就不合适了，很多API是不需要的，所以在Flink中抽象出了Environment作为运行时刻的上下文信息


RuntimeContext是运行时Task实例级别的概念。Environment本身仍然是比较粗粒度作业级别的配置，
对于每一个Task而言，其本身有更细节的配置信息，所以Flink又抽象了RuntimeContext，
每一个Task实例有自己的RuntimeContext，
RuntimeContext的信息实际上是StreamExecutionEnvironment中配置信息和算子级别信息的综合。


![XrlTeH](https://raw.githubusercontent.com/jacksonyoudi/images/main/uPic/XrlTeH.png)



### 3.1.1 执行环境


    /**
     * LocalStreamEnvironment
     * 本地执行环境，调试，开发
     *
     *main生成streamGraph, 转化为jobgraph,
     * 设置任务运行的配置信息
     * LocalFLinkMiniCluster
     * MiniClusterClient
     * jobgraph => minicluster
     */


    /**
     * RemoteStreamEnvironment
     *在大规模数据中心中部署的Flink生成集群的执行环境
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



### 3.1.2 运行时环境
Environment是flink运行时的概念，该接口定义了在运行时刻task所需要的所有配置信息，包括静态配置和调度之后生成的
动态配置信息等。 

```java
	public RuntimeEnvironment(
			JobID jobId,
			JobVertexID jobVertexId,
			ExecutionAttemptID executionId,
			ExecutionConfig executionConfig,
			TaskInfo taskInfo,
			Configuration jobConfiguration,
			Configuration taskConfiguration,
			UserCodeClassLoader userCodeClassLoader,
			MemoryManager memManager,
			IOManager ioManager,
			BroadcastVariableManager bcVarManager,
			TaskStateManager taskStateManager,
			GlobalAggregateManager aggregateManager,
			AccumulatorRegistry accumulatorRegistry,
			TaskKvStateRegistry kvStateRegistry,
			InputSplitProvider splitProvider,
			Map<String, Future<Path>> distCacheEntries,
			ResultPartitionWriter[] writers,
			IndexedInputGate[] inputGates,
			TaskEventDispatcher taskEventDispatcher,
			CheckpointResponder checkpointResponder,
			TaskOperatorEventGateway operatorEventGateway,
			TaskManagerRuntimeInfo taskManagerInfo,
			TaskMetricGroup metrics,
			Task containingTask,
			ExternalResourceInfoProvider externalResourceInfoProvider) {
```

2. SavepointEnvironmentSavepointEnvironment是Environment的最小化实现，在状态处理器的API中使用。
   Flink1.9版本引入的状态处理器（State Processor）API真正改变了这一现状，实现了对应用程序状态的操作。
   该功能借助DataSet API扩展了输入和输出格式以读写保存点或检查点数据。由于DataSet和Table API的互通性，用户甚至可以使用关系表API或SQL查询来分析和处理状态数据。


```xml
<!-- https://mvnrepository.com/artifact/org.apache.flink/flink-state-processor-api -->
<dependency>
    <groupId>org.apache.flink</groupId>
    <artifactId>flink-state-processor-api_2.12</artifactId>
    <version>1.12.0</version>
    <scope>provided</scope>
</dependency>

```

### 3.1.3 运行时上下文


RuntimeContext是Function运行时的上下文，封装了Function运行时可能需要的所有信息，让Function在运行时能够获取到作业级别的信息，如并行度相关信息、Task名称、执行配置信息（ExecutionConfig）、State等。
Function的每个实例都有一个RuntimeContext对象，在RichFunction中通过getRunctionContext（）可以访问该对象。

不同的使用场景中有不同的RuntimeContext，具体如下。
1）StreamingRuntimeContext：在流计算UDF中使用的上下文，用来访问作业信息、状态等。
2）DistributedRuntimeUDFContext：由运行时UDF所在的批处理算子创建，在DataSet批处理中使用。
3）RuntimeUDFContext：在批处理应用的UDF中使用。
4）SavepointRuntimeContext：Flink1.9版本引入了一个很重要的状态处理API，这个框架支持对检查点和保存点进行操作，包括读取、变更、写入等
5) CepRuntimeContext：CEP复杂事件处理中使用的上下文。




## 3.2 数据流元素
StreamElement

有数据记录的 StreamRecord
延迟标记 Latecy Marker
WaterMark
流状态标记 StreamStatus


```java
	@SuppressWarnings("unchecked")
	public final <E> StreamRecord<E> asRecord() {
		return (StreamRecord<E>) this;
	}

	/**
	 * Casts this element into a Watermark.
	 * @return This element as a Watermark.
	 * @throws java.lang.ClassCastException Thrown, if this element is actually not a Watermark.
	 */
	public final Watermark asWatermark() {
		return (Watermark) this;
	}

	/**
	 * Casts this element into a StreamStatus.
	 * @return This element as a StreamStatus.
	 * @throws java.lang.ClassCastException Thrown, if this element is actually not a Stream Status.
	 */
	public final StreamStatus asStreamStatus() {
		return (StreamStatus) this;
	}

	/**
	 * Casts this element into a LatencyMarker.
	 * @return This element as a LatencyMarker.
	 * @throws java.lang.ClassCastException Thrown, if this element is actually not a LatencyMarker.
	 */
	public final LatencyMarker asLatencyMarker() {
		return (LatencyMarker) this;
	}


```


1. StreamReocord

value
timestamp

```java
public final class StreamRecord<T> extends StreamElement {

	/** The actual value held by this record. */
	private T value;

	/** The timestamp of the record. */
	private long timestamp;

	/** Flag whether the timestamp is actually set. */
	private boolean hasTimestamp;

	/**
	 * Creates a new StreamRecord. The record does not have a timestamp.
	 */
	public StreamRecord(T value) {
		this.value = value;
	}
```

2. LatencyMarker
   LatencyMarker用来近似评估延迟，LatencyMarker在Source中创建，并向下游发送，绕过业务处理逻辑，
   在Sink节点中使用LatencyMarker估计数据在整个DAG图中流转花费的时间，用来近似地评估总体上的处理延迟。

```java
public final class LatencyMarker extends StreamElement {

	// ------------------------------------------------------------------------

	/** The time the latency mark is denoting. */
	private final long markedTime;

	private final OperatorID operatorId;

	private final int subtaskIndex;


```


3. Watermark
   Watermark是一个时间戳，用来告诉算子所有时间早于等于Watermark的事件或记录都已经到达，
   不会再有比Watermark更早的记录，算子可以根据Watermark触发窗口的计算、清理资源等。

```java
@PublicEvolving
public final class Watermark extends StreamElement {

	/** The watermark that signifies end-of-event-time. */
	public static final Watermark MAX_WATERMARK = new Watermark(Long.MAX_VALUE);

	// ------------------------------------------------------------------------

	/** The timestamp of the watermark in milliseconds. */
	private final long timestamp;

```


4. StreamStatus

用来通知Task是否会继续接收到上游的记录或者Watermark。
StreamStatus在数据源算子中生成，向下游沿着Dataflow传播。
StreamStatus可以表示两种状态：
1）空闲状态（IDLE）。
2）活动状态（ACTIVE）


```java
	public static final int IDLE_STATUS = -1;
	public static final int ACTIVE_STATUS = 0;

	public static final StreamStatus IDLE = new StreamStatus(IDLE_STATUS);
	public static final StreamStatus ACTIVE = new StreamStatus(ACTIVE_STATUS);
```




## 3.3 数据转换
数据转换在Flink中叫作Transformation，是衔接DataStream API和Flink内核的逻辑结构。
DataStream面向开发者，Transformation面向Flink内核
，调用DataStream API的数据处理流水线，最终会转换为Transformation流水线，Flink从Transformation流水线开始执行

![xguhQQ](https://raw.githubusercontent.com/jacksonyoudi/images/main/uPic/xguhQQ.png)

![SE50SQ](https://raw.githubusercontent.com/jacksonyoudi/images/main/uPic/SE50SQ.png)

1. 物理Transformation

(1) SourceTransformation
![liqNnh](https://raw.githubusercontent.com/jacksonyoudi/images/main/uPic/liqNnh.png)

(2) SinkTransformation
(3) OneInputTransformation
(4) TwoInputTransformation

2. 虚拟Transformation
   (1) sideOutputTransformation
   (2) SplitTransformation
   (3) SelectTransformation
   (4) PartitionTransformation
   
## 3.4 算子

算子在Flink中叫作StreamOperator。
StreamOperator是流计算的算子。
Flink作业运行时由Task组成一个Dataflow，每个Task中包含一个或者多个算子，
1个算子就是1个计算步骤，具体的计算由算子中包装的Function来执行。
除了业务逻辑的执行算子外，还提供了生命周期的管理。


### 3.4.1 算子行为
所有的算子都包含了生命周期管理、状态与容错管理、数据处理3个方面的关键行为。

1.生命周期管理所有的算子都有共同的生命周期管理，其核心生命周期阶段如下。
1）setup：初始化环境、时间服务、注册监控等。
2）open：该行为由各个具体的算子负责实现，包含了算子的初始化逻辑，如状态初始化等。算子执行该方法之后，才会执行Function进行数据的处理。
3）close：所有的数据处理完毕之后关闭算子，此时需要确保将所有的缓存数据向下游发送。
4）dispose：该方法在算子生命周期的最后阶段执行，此时算子已经关闭，停止处理数据，进行资源的释放。StreamTask作为算子的容器，负责管理算子的生命周期。


2.状态和容错管理
算子负责状态管理，提供状态存储，触发检查点的时候，保存状态快照，并且将快照异步保存到外部的分布式存储。
当作业失败的时候算子负责从保存的快照中恢复状态。

3. 数据处理
   算子对数据的处理，不仅会进行数据记录的处理，同时也会提供对Watermark和LatencyMarker的处理。
   算子按照单流输入和双流输入，定义了不同的行为接口
   

### 3.4.2FLink 算子

Flink流计算算子体系最开始是围绕DataStream API设计的，同时也是Flink SQL流计算的底层执行框架，

1）数据处理。主要是OneInputStreamOperator、TwoInputStreamOperator接口。
2）生命周期、状态与容错。主要是AbstractStreamOperator抽象类及其子实现类。AbstractUdfStreamOperator是主要的实现类，目前所有的算子都继承自此类。SourceReaderOperator是抽象类，目前还没有实现类，其在Flip-27中引入，未来要做Source接口的重构，实现流和批Source在实现层面上的统一。



### 3.4.2 Blink算子

Blink Runtime中的算子是阿里巴巴Blink引入的流批统一的算子，在当前阶段用来支撑Blink的Table & SQL的运行。
正因为要实现流批统一、动态代码生成等高级能力特性，所以在目前的Blink Runtime中重新实现了很多与SQL相关的算子，但是仍然使用了Flink-Streaming-java中定义的Transformation来包装这些算子。
Blink算子体系如图3-18和图3-19所示。
Blink Runtime中用到的算子大概可以分为3类：
   1）Blink Runtime内置算子。
   2）其他模块内置的算子，如CEP算子（CepOperator）、ProcessOperator等。
   3）通过动态代码生成的算子。


### 3.4.4 异步算子
异步算子的目的是解决与外部系统交互时网络延迟所导致的系统瓶颈问题。


## 3.5 函数体系


### 3.5.1 函数层次
![XgbVA5](https://raw.githubusercontent.com/jacksonyoudi/images/main/uPic/XgbVA5.png)


RichFunction相比无状态Function，有两方面的增强：

1）增加了open和close方法来管理Function的生命周期，在作业启动时，Function在open方法中执行初始化，在Function停止时，在close方法中执行清理，释放占用的资源等。无状态Function不具备此能力。

2）增加了getRuntimeContext和setRuntimeContext。通过RuntimeContext，RichFunction能够获取到执行时作业级别的参数信息，而无状态Function不具备此能力。

无状态Function天然是容错的，作业失败之后，重新执行即可，但是有状态的Function（RichFunction）需要处理中间结果的保存和恢复，待有了状态的访问能力，也就意味着Function是可以容错的，执行过程中，状态会进行快照然后备份，在作业失败，Function能够从快照中恢复回来。


### 3.5.2 处理函数
处理函数（ProcessFunction）可以访问流应用程序所有（非循环）基本构建块
1)  事件 (数据流元素)
2)  状态 (容错和一致性)
3)  定时器 (事件时间和处理时间)

### 3.5.3 广播函数

### 3.5.4 异步函数

### 3.5.5 数据源函数

SourceFunction
![obsRfH](https://raw.githubusercontent.com/jacksonyoudi/images/main/uPic/obsRfH.png)

### 3.5.6 输出函数
SinkFunction


### 3.5.7 检查点函数


## 3.6 数据分区

1. 自定义分区
2. ForwardPartitioner
3. shufflePartitioner
4. rebalancePartitioner
5. RescalingPartitioner
6. BroadcastPartitioner
7. KeyGroupStreamPartitioner


### 3.7 连接器


### 3.8 分布式ID
