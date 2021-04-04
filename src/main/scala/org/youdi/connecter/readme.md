### connector




### kafka


1. 启动zk
```bash
bin/zkServer.sh start
```

2. 启动kafka
```nashorn js
bin/kafka-server-start.sh -daemon  config/server.properties
```

3. 创建topic

```shell
bin/kafka-topics.sh --zookeeper localhost:2181/kafka --create --topic flink_kafka --replication-factor 1 --partitions 4
```

4. 查看topic

```bash
bin/kafka-topics.sh --zookeeper localhost:2181/kafka --list
```

5. 生产消息
```bash 
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic flink_kafka
```

6. 消费消息

```bash 
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092  --topic topic-demo

./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092  --topic topic-demo --from-beginning
```