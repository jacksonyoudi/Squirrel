## flink的API

###  source
1. 基于文件
    可以是文件或文件夹，也可以是压缩文件
2. 基于内存
3. 




### 自定义Source

1. sourceFunction: 非并行数据源 并行度只能=1
2. RichSourceFunction: 多功能非并行数据
3. ParallelSourceFucntion: 并行数据源
4. RichParallelSourceFucntion 并行的sourceFunction kafka就是这个


