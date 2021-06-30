## 资源管理



### 9.1 资源抽象

集群资源 flink自身资源

task slot


### 9.3 slot管理器 

slotmanager，是resourcemanager的组件，从全局角度维护当前有多少个taskManager，每个taskManager
有多少个空闲的slot和slot等资源的使用情况。当flink作业调度执行的时，
根据slot分配策略为task分配执行位置。 


slotmanager提供不同的功能：
1. 对taskManager提供注册，取消注册，空闲退出等管理动作，注册则集群可用的slot变多，取消注册，空闲
退出则释放资源，还给资源管理集群。
   
2. 对flink作业，接收slot的请求和释放，资源汇报。 


### slotprovider

1. 立即响应模式: slot请求会立即执行
2. 排队模式： 排队等待可用slot， 当资源可用时分配资源


### slot选择策略
SlotselectionStrategy 


1. 位置优先的选择策略 LocationPerferenceSlotSelectionStrategy

    默认策略
    均衡策略
   
2. 已经分配的slot优先策略， previousAllocationSlotSelectionStrategy


### slot资源池



### 调度

1. 调度器
2. 调度策略
3. 调度模式
