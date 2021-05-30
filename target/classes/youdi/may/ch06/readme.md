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




