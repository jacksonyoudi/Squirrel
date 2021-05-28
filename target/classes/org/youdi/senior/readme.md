### 广播状态
BroadCastState
配置流 信息流 规则流



```scala
val des = userDS.broadcast(desriptor)


eventDs.connect(des)


```


### 双流join

#### window join    
    * Tubmling Join 
    * slide Join
    * session Join

#### interval  join 





### end-toend


### exactly-once 


- at most one
- at least one
- exactly once

end-to-end exactly-once  source to sink exactly once



#### 如何保证exactly-once
1. 可以使用去重
   
    at least one + 去重
    储存 
   
2. 幂等
    
   at least one + 幂等
    存储和数据特征

3. 分布式快照/checkpoint
    
    


![aiLpK6](https://raw.githubusercontent.com/jacksonyoudi/images/main/uPic/aiLpK6.png)


### end to end exactly-once
source:
    
    如kafka的offset支持数据的reply、重放，重新传输
    kafka 0.11+


transform:
    
    借助checkpoints

sink:
    
    checkpoint + 两阶段事务提交
    flink 1.4+

#### 两阶段事务提交

TwoPhaseCommitSinkFunction
1. 开启事务
2. 各个operator执行barrier的checkpoint，成功则进行预提交
3. 所有operator执行完预提交则执行真正的提交
4. 如果有任何一个预提交失败则回滚到最近的checkpoint

#### File Sink


### flink 整合hive


### flink监控


### 性能分析
1. 历史服务器

2. 复用对象

3. 数据倾斜

4. 异步IO 
5. 合理调整并行度


### flink内存管理



