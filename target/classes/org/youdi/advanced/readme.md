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

