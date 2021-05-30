### watermark 
就是时间戳

watermark = 数据的事件时间 - 最大允许的延迟时间或乱序时间

当前窗口的最大事件时间 - 最大允许的延迟时间或乱序时间

watermark是用来触发窗口计算的



窗口计算的触发条件:
1. 窗口有数据
2. watermarker > 窗口的结束时间






### allowedLateness 
允许迟到，侧流输出机制

用来单独收集数据延迟严重的数据



### state

flink中状态是自动管理



manager state
1. flnk runtime 管理
2. 自动存储，自动恢复
3. 内存管理上有优化

- keystate
- operator state



raw state
1. 用户自己管理
2. 需要自己序列化






### checkPoint

1. state
维护存储的是某个operator的运行的状态和历史值，维护在内存中的
   
一般是指一个具体的operation的状态(operator的状态表示一些算子在运行的过程中会产生一些历史结果)


state数据默认保存在java的堆内存中的，taskmanager节点的内存中的
state可以被记录，在失败的情况下数据还可以恢复

2. checkpoint
在某一时刻，flink中所有的operator的当前state的全局快照，一般存在磁盘上
   表示了一个flink job在一个特定时刻下的全局状态的快照， 即包含了所有operator的状态，可以理解为
   checkpoint是把state数据定时持久化存储。
   



checkpoint coordinator 检查点协调器 (job manager创建)

barrier 
snapshot


checkpoint 其实就是将flink的某一时刻，所有的operator的全局快照保存下来。
快照保存的地方，就是状态后端。 

状态后端：
* MemstateBackend
* FsStateBackend
* RocksDBStateBackend



### 状态恢复和重启策略


重启策略分类
* 默认重启策略
* 无重启策略
 * 国定延迟重启策略 -- 开发中使用
* 失败率重启策略-- 


