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



默认重启策略
如果配置了Checkpoint,而没有配置重启策略,那么代码中出现了非致命错误时,程序会无限重启

无重启策略
设置方式1:
restart-strategy: none

设置方式2:
无重启策略也可以在程序中设置
val env = ExecutionEnvironment.getExecutionEnvironment()
env.setRestartStrategy(RestartStrategies.noRestart())

重启策略可以配置flink-conf.yaml的下面配置参数来启用，作为默认的重启策略:
例子:
restart-strategy: fixed-delay
restart-strategy.fixed-delay.attempts: 3
restart-strategy.fixed-delay.delay: 10 s

设置方式2:
也可以在程序中设置:
val env = ExecutionEnvironment.getExecutionEnvironment()
env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
3, // 最多重启3次数
Time.of(10, TimeUnit.SECONDS) // 重启时间间隔
))
上面的设置表示:如果job失败,重启3次, 每次间隔10

设置方式1:
失败率重启策略可以在flink-conf.yaml中设置下面的配置参数来启用:
例子:
restart-strategy:failure-rate
restart-strategy.failure-rate.max-failures-per-interval: 3
restart-strategy.failure-rate.failure-rate-interval: 5 min
restart-strategy.failure-rate.delay: 10 s

设置方式2:
失败率重启策略也可以在程序中设置:
val env = ExecutionEnvironment.getExecutionEnvironment()
env.setRestartStrategy(RestartStrategies.failureRateRestart(
3, // 每个测量时间间隔最大失败次数
Time.of(5, TimeUnit.MINUTES), //失败率测量的时间间隔
Time.of(10, TimeUnit.SECONDS) // 两次连续重启的时间间隔
))
上面的设置表示:如果5分钟内job失败不超过三次,自动重启, 每次间隔10s (如果5分钟内程序失败超过3次,则程序退出)



### 手动重启




### savepoint 
手动重启

```shell
/export/server/flink/bin/flink savepoint 702b872ef80f08854c946a544f2ee1a5 hdfs://node1:8020/flink-checkpoint/savepoint/

/export/server/flink/bin/flink cancel 702b872ef80f08854c946a544f2ee1a5


/export/server/flink/bin/flink run 
-s hdfs://node1:8020/flink-checkpoint/savepoint/savepoint-702b87-0a11b997fa70
 --class cn.itcast.checkpoint.CheckpointDemo01 /root/ckp.jar 

```



