### Flink特点
1. 基于事件驱动的
2. 基于流的世界观
3. 分层API
4. 多种时间
5. exactly-once
6. 低延迟高吞吐
7. 高可用
8. 与众多存储系统的连接



### jobmanager
    管理调度，


### taskmanager
    worker


### 提交任务

./bin/flink run -c youdi.com.wc.SWC -p 2 /.../jar

flink list -a
flink cancel job_id


### on yarn

session-cluster
    共享

yarn-session.sh -n 2 -jm 1024 -tm  -nm -d 
-n taskmanager
-s slots个数
-jm jobmanager内存
-tm  taskmanager内存
-nm 名称
-d  后台运行




per-job-cluster
    job是独占的


flink run -m yarn-cluster -c com.atguigu.wc.StreamWordCount

-m: 模式



### flink运行时的组件
1. 作业管理器
jobmanager 
   分配管理调度， checkpoint
   生成执行图， 生成task
   
2. taskmanager
    slot最小化执行单元
   
3. resourcemanager 
    资源管理 slots
   
4. Dispacher 分发器
   restful接口
   web UI
   
### jobmanager
* 控制一个应用程序执行的主进程，每个应用程序都会被一个不同的jobmanager所控制
* jobgranph logical dataflow graph 打包所有的类，库，以及其他资源jar包
* jobGraph转换成一个物理层面的数据流图， 执行图 ExecutionGraph 包含所有可以并发执行的任务。
* 


### taskmanager
* 工作进程
* slots * tm数量
* 管理slot

### Dispacher




### 任务提交流程

![k391fT](https://cdn.jsdelivr.net/gh/jacksonyoudi/images@main/uPic/k391fT.png)

![3vDzWN](https://cdn.jsdelivr.net/gh/jacksonyoudi/images@main/uPic/3vDzWN.png)



### 任务调度原理

![FmqlwC](https://raw.githubusercontent.com/jacksonyoudi/images/main/uPic/FmqlwC.png)


#### 问题
* 怎样实现并行计算
* 并行的任务，需要占用多少slot？
* 一个流处理程序，到底包含多少个任务？



### 并行度 (parallelism)

![Av2KcW](https://raw.githubusercontent.com/jacksonyoudi/images/main/uPic/Av2KcW.png)



### taskmanager和slots
slots内存划分的单位

![h14JQd](https://raw.githubusercontent.com/jacksonyoudi/images/main/uPic/h14JQd.png)
默认情况下， flink允许任务共享slot是，即使它们是不同任务的子任务

task slot是静态的概念， 是指taskmanager具有的并发执行能力

pipeline 一个slot可以保存作业的整个管道


