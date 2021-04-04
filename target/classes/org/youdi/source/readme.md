### 自定义source

* API


SourceFunction: 非并行数据源(并行度只能是1)
RichSourceFunction 多功能非并行数据源 并行度只能是1

ParallelSourceFucntion并行数据源 并行度能够 > 1
RichParallelSourceFucntion:多功能并行数据源
(kafka也是实现了这个)
