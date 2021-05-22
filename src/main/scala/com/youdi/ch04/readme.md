### 4.1 时间类型

事件时间 event time
处理时间 process time
摄取时间 ingestion time


### 4.2 窗口类型
1. 计算窗口  滚动和滑动
2. 时间窗口  滚动和滑动
3. 会话窗口

Tumble Count Window
Sliding Count Window


Tumble Time Window
Sliding Time Window
Session Window

Session Window是一种特殊的窗口，当超过一段时间，该窗口没有收到新的数据元素，则视为该窗口结束，所以无法事先确定窗口的长度、元数个数，窗口之间也不会相互重叠。


### 4.3窗口原理与机制
窗口算子负责处理窗口，数据流源源不断地进入算子，每一个数据元素进入算子时，首先会被交给WindowAssigner。
WindowAssigner决定元素被放到哪个或哪些窗口，在这个过程中可能会创建新窗口或者合并旧的窗口。在Window Operator中可能同时存在多个窗口，一个元素可以被放入多个窗口中


### 4.3.1 windowAssigner

### 4.3.2 windowTrigger 
trigger触发器决定了一个窗口何时能被计算或清除，每一个窗口都拥有一个属于自己的trigger


![QdSe6H](https://raw.githubusercontent.com/jacksonyoudi/images/main/uPic/QdSe6H.png)

### 4.3.1 windowEvictor
![Zk0dVs](https://raw.githubusercontent.com/jacksonyoudi/images/main/uPic/Zk0dVs.png)


### 4.3.4window函数
1. 增量计算函数
2. 全量计算函数


## 4.4 水印

4.4.1  dataStream WaterMark生成

1. Source Function中生成Watermark

Source Function可以直接为数据元素分配时间戳，同时也会向下游发送Watermark。在Source Function中为数据分配了时间戳和Watermark就不必在DataStream API中使用了。
需要注意的是:如果一个timestamp分配器被使用的话，由源提供的任何Timestamp和Watermark都会被重写。

2. DataStream API中生成Watermark
AssignerWithWatermark