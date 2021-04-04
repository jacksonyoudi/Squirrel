### 基本操作

* map
* Flatmap
* keyby 分组
* filter
* reduce
* sum
* union和connect

        union不改变，可以多个流，只能同类型
        connect改变，只能2个流，可以不同类型，connect后，需要做其他操作
* split，select side outputs

  split, select过期了，使用outputtag和process

* rebalance

    出现数据倾斜，解决方案就是rebalance内部使用 roundrobin方法将数据均匀打散

### 分区

|类型|描述|
|------|------|
|dataStream.global()|全部发往第1个task|
|dataStream.broadcast()|广播|
|dataStream.forward()|上下游并发度一样时一对一发送|
|dataStream.shuffle()|随机均分分配|
|dataStream.rebalance()|rr轮流分配|
|dataStream.recale()|local roundrobin 本地轮流分配|
|dataStream.partitionCustom()|自定义单播|



  
