### flink 四大基石

- checkpoint
- state
- Time
- Window



### Window
1. 分类
    
    * time-window
        
            根据时间划分窗口    
    
    * count-window
            
            根据数量划分窗口
    
    
* 滑动
* 滚动

flink还支持一个特殊的窗口， session会话窗口， 需要设置一个会话超时时间， 如 30s， 
则表示30s内没有数据到来，则触发上一个窗口的计算。



### window API

使用 keyby的流，应该使用window方法

未使用keyby的流， 应该使用windowAll


![1ZAt8I](https://raw.githubusercontent.com/jacksonyoudi/images/main/uPic/1ZAt8I.png)


### Time

分类

   * 事件时间 event time
   * 摄入时间 ingester time
   * 处理时间 process time

事件时间

### watermarker
就是给数据再额外加一个时间列

也就是watermarker是一个时间戳


watermarker = 数据的事件时间 - 最大允许的延迟时间或乱序时间

watermarker = 当前窗口的最大的事件时间 - 最大允许的延迟时间或乱序时间
这样就可以保证watermarker水位线一直在上升，不会下降， 


作用：
   
1. 解决乱序问题


窗口触发计算的

1. 窗口有数据
2. watermarker > 窗口的结束时间

![4sYyXx](https://raw.githubusercontent.com/jacksonyoudi/images/main/uPic/4sYyXx.png)



### Allowed Lateness

单独收集迟到严重的数据




### 状态管理

flink已经做好了状态的自动管理
在绝大多数情况下都是使用自动管理， 很少手动管理


分类：

* 无状态
   
   不需要考虑历史
   map filter flatmap
   相同的输入，相同的输出
  

* 有状态
   
   需要考虑历史
   相同的输入，可能得到不同的输出
   
场景：
   去重
   窗口计算
   访问历史数据
   机器学习和深度学习


![Ll1ADx](https://raw.githubusercontent.com/jacksonyoudi/images/main/uPic/Ll1ADx.png)

![jyyPtT](https://raw.githubusercontent.com/jacksonyoudi/images/main/uPic/jyyPtT.png)


### checkpoint

所有状态


![0yXWV1](https://raw.githubusercontent.com/jacksonyoudi/images/main/uPic/0yXWV1.png)



### 状态后端

存储介质


- memoryStateBackend
- FsStateBackend
- RocksDBStateBackend (内存+磁盘)

###  状态恢复和重启策略

重启策略分类
* 默认重启 无限重启
* 无重启
* 固定延迟重启
* 失败率重启


1. 如果配置了checkpoint情况下， 默认是无限重启并自动恢复，可以解决小问题， 但是可以隐藏真正的问题



#### 手动重启



#### SavePoint
手动的checkpoint



```bash
# 启动yarn session
/export/server/flink/bin/yarn-session.sh -n 2 -tm 800 -s 1 -d

# 运行job-会自动执行Checkpoint
/export/server/flink/bin/flink run --class cn.itcast.checkpoint.CheckpointDemo01 /root/ckp.jar

# 手动创建savepoint--相当于手动做了一次Checkpoint
/export/server/flink/bin/flink savepoint 702b872ef80f08854c946a544f2ee1a5 hdfs://node1:8020/flink-checkpoint/savepoint/

# 停止job
/export/server/flink/bin/flink cancel 702b872ef80f08854c946a544f2ee1a5

# 重新启动job,手动加载savepoint数据
/export/server/flink/bin/flink run -s hdfs://node1:8020/flink-checkpoint/savepoint/savepoint-702b87-0a11b997fa70 --class cn.itcast.checkpoint.CheckpointDemo01 /root/ckp.jar 

# 停止yarn session
yarn application -kill application_1607782486484_0014

```






