### 实时数仓 

为了数据复用， 减少重复计算和减少时间


### 分层
ODS
    原始数据，日志和业务数据

DWD
    根据数据对象为单位进行分流， 比如订单，页面访问等等。

DIM
    维度数据   grpc， 异步请求

DWM
    对于部分数据对象进行进一步加工，比如独立访问，跳出行为，也可以和维度进行关联
形成宽表，依旧是明细数据。 


DWS
 根据某个主题将多个事实表轻度聚合，形成主题宽边


ADS： 
    把clickhouse中的数据根据可视化需要进行筛选聚合


![dVym9S](https://raw.githubusercontent.com/jacksonyoudi/images/main/uPic/2021/10/17/dVym9S.png)




