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

