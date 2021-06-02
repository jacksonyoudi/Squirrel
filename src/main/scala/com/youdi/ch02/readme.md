## 数据流API

### 2.4.1 数据读取
1. 从内存中读取


2. 从文件读取数据

设置文件读取模式
```java
public enum FileProcessingMode {

	/** Processes the current contents of the path and exits. */
	PROCESS_ONCE,

	/** Periodically scans the path for new data. */
	PROCESS_CONTINUOUSLY
}
```

3. socket接入数据

```java


//  需要提供hostname（主机名）、port（端口号）、delimiter（分隔符）和maxRetry（最大重试次数）
  def socketTextStream(hostname: String, port: Int, delimiter: Char = '\n', maxRetry: Long = 0):
        DataStream[String] =
    asScalaStream(javaEnv.socketTextStream(hostname, port))
```


4. 自定义读取

env.createInput()
env.addSource()

addSource（）方法本质上来说依赖于Flink的SourceFunction体系，与外部的存储进行交互。
createInput（）方法底层调用的是addSource（）方法，封装为InputFormatSourceFunction，
所以自定义读取方式的本质就是实现自定义的SourceFunction

### 2.4.2 处理数据

![cZ4PJE](https://raw.githubusercontent.com/jacksonyoudi/images/main/uPic/cZ4PJE.png)

* map
* flatmap
* filter
* keyby hash Partitioner实现
* reduce 
* fold
* aggregation
* window
* windowAll


### 2.4.3 数据写出
数据读取的api是绑定在StreamExecutionEnvironment上的，数据写出的API绑定在DataStream对象上，在现在的版本中，
只有写到console, socket 自定义的，

addSink

#### 2.4.4旁路输出
![CGLl9V](https://raw.githubusercontent.com/jacksonyoudi/images/main/uPic/CGLl9V.png)

Table & SQL的语义中多条Insert语句一起执行，使用不同的Where条件输出到不同的目的地，
这就是SideOutput旁路输出的适用场景。
