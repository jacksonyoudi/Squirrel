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



