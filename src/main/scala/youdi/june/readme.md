### JVM

1. 类加载器子系统
    
2. 运行时数据区
    方法区
    本地方法区
    虚拟机栈(jvm stack 栈帧)
    虚拟机堆(jvm heap)
    pc(程序计算器)


3. 执行引擎
   即时编译器
   GC

4. 本地接口库 -> 本地方法库




### JVM后台运行的线程
    
*  jvm thread： 安全点
*  周期性任务线程： 通过定时器调度线程来实现周期性操作
* GC线程
* 编译器线程： 
* 信号分发线程： 接收发送到JVM的信号并调用jvm方法



### jvm内存区域




### JAVA中的锁

乐观锁
悲观锁

自旋锁


### 3.6.4 synchronized

作用于一个代码块：锁住的是所有代码块中的配置对象


原理：
    
    ContentionList, EntryList, WaitSet, OnDeck, Owner, !Owner

contentionList: 锁竞争队列，所有请求锁的线程都放在这个队列中
EntryList: 候选列表
WaitSet: 等待集合， 调用wait方法后被阻塞的线程都被放在waitSet中。
onDeck： 竞选候选者，
Owner： 竞选上的 
!Owner: 释放后的


先自旋一下， 放到 ContentionList ——> EntryList -> OnDeck -> 


### ReentrantLock
可重入锁

AQS: 自定义队列同步器(Abstract Queued Synchronized)

获取锁和释放锁的次数要相同，如果释放锁的次数多于获取锁的次数，Java就会抛出java.lang.IllegalMonitorStateException异常；如果释放锁的次数少于获取锁的次数，该线程就会一直持有该锁，其他线程将无法获取锁资源。

响应中断、可轮询锁、定时锁




### 公平锁和非公平锁
公平的， 先来先到， 
非公平： JVM遵循随机，就近原则分配锁的机制

ReentrantLock是API级别的，synchronized是JVM级别的。



### Semaphore

aquire
release
二元信号量


### AtomicInteger
    

### ReadWriteLock

    

### 3.7 线程上下文切换


任务状态保存和恢复现场，叫做线程的上下文切换


### CountDownLatch

### CyclicBarrier

### Semaphore




```json
{
"shopid": 601295157,
"country": "ID",
"shop_pv": 788389,
"pv": 645275,
"shop_uv": 116712,
"uv": 116156,
"add_to_cart_units": 80821,
"add_to_cart_buyers": 37603,
"items_visited": 187,
"bounce_visitors": 20879,
"likes": 6431,
"add_to_cart_or_liked_visitors": 40213,
"placed_sales": 4412189285.98,
"confirmed_sales": 4148994611.98,
"paid_sales": 4148994611.98,
"placed_orders": 16280,
"confirmed_orders": 15448,
"paid_orders": 15448,
"placed_buyers": 14199,
"confirmed_buyers": 13857,
"paid_buyers": 13857,
"placed_units": 23659,
"confirmed_units": 22440,
"paid_units": 22440,
"items_paid": 136,
"items_confirmed": 136,
"repeated_paid_buyers": 1327,
"repeated_placed_buyers": 1709,
"repeated_confirmed_buyers": 1327,
"existing_paid_buyers": 2909,
"existing_placed_buyers": 2990,
"existing_confirmed_buyers": 2909,
"dt": "2021-06-30",
"settlement_data":'[{"placed_settlement_sales":1000,"placed_none_settlement_sales":4412.18928598E9,"paid_settlement_sales":8000,"paid_none_settlement_sales":4148.99461198E9,"confirmed_settlement_sales":600,"confirmed_none_settlement_sales":4.14899461198E9,"currency":2}]'
}



```



