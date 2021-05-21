#### 处理空闲数据源

如果数据源中的某一个分区/分片在一段时间内未发送事件数据，
则意味着 WatermarkGenerator 也不会获得任何新数据去生成 watermark
。我们称这类数据源为空闲输入或空闲源。在这种情况下，当某些其他分区仍然发送事件数据的时候就会出现问题。
由于下游算子 watermark 的计算方式是取所有不同的上游并行数据源 watermark 的最小值，则其 watermark 将不会发生变化。

为了解决这个问题，你可以使用 WatermarkStrategy 来检测空闲输入并将其标记为空闲状态。WatermarkStrategy 为此提供了一个工具接口：


```scala
WatermarkStrategy
  .forBoundedOutOfOrderness[(Long, String)](Duration.ofSeconds(20))
  .withIdleness(Duration.ofMinutes(1))
```



