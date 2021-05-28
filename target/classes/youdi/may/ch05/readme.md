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


需要实现的接口就是 run 和cancel
可以重写 open close方法






### transform 
1. map 类型转换
2. flatMap



3. keyby  按key分组
4. reduce 
5. filter


#### union
相同类型的数据流进行合并
可能合并多个流


#### connect
connect 只能合并两个流， 
可以数据类型不同


#### split, select , side output


### 分区

rebalance重平衡分区

轮询的方式

![n1z66w](https://raw.githubusercontent.com/jacksonyoudi/images/main/uPic/n1z66w.png)




