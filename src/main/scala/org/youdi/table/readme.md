### Table & SQL


#### 动态表和连续查询

动态表/无界表
连续查询/需要借助state





| 辅助函数                                                     | 描述                                                         |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| `TUMBLE_START(time_attr, interval)` `HOP_START(time_attr, interval, interval)` `SESSION_START(time_attr, interval)` | 返回相对应的滚动、滑动和会话窗口范围内的下界时间戳。         |
| `TUMBLE_END(time_attr, interval)` `HOP_END(time_attr, interval, interval)` `SESSION_END(time_attr, interval)` | 返回相对应的滚动、滑动和会话窗口*范围以外*的上界时间戳。**注意：** 范围以外的上界时间戳*不可以* 在随后基于时间的操作中，作为 [行时间属性](https://ci.apache.org/projects/flink/flink-docs-release-1.12/zh/dev/table/streaming/time_attributes.html) 使用，比如 [interval join](https://ci.apache.org/projects/flink/flink-docs-release-1.12/zh/dev/table/sql/queries.html#joins) 以及 [分组窗口或分组窗口上的聚合](https://ci.apache.org/projects/flink/flink-docs-release-1.12/zh/dev/table/sql/queries.html#aggregations)。 |
| `TUMBLE_ROWTIME(time_attr, interval)` `HOP_ROWTIME(time_attr, interval, interval)` `SESSION_ROWTIME(time_attr, interval)` | 返回相对应的滚动、滑动和会话窗口*范围以内*的上界时间戳。返回的是一个可用于后续需要基于时间的操作的[时间属性（rowtime attribute）](https://ci.apache.org/projects/flink/flink-docs-release-1.12/zh/dev/table/streaming/time_attributes.html)，比如[interval join](https://ci.apache.org/projects/flink/flink-docs-release-1.12/zh/dev/table/sql/queries.html#joins) 以及 [分组窗口或分组窗口上的聚合](https://ci.apache.org/projects/flink/flink-docs-release-1.12/zh/dev/table/sql/queries.html#aggregations)。 |
| `TUMBLE_PROCTIME(time_attr, interval)` `HOP_PROCTIME(time_attr, interval, interval)` `SESSION_PROCTIME(time_attr, interval)` | 返回一个可用于后续需要基于时间的操作的 [处理时间参数](https://ci.apache.org/projects/flink/flink-docs-release-1.12/zh/dev/table/streaming/time_attributes.html#processing-time)，比如[interval join](https://ci.apache.org/projects/flink/flink-docs-release-1.12/zh/dev/table/sql/queries.html#joins) 以及 [分组窗口或分组窗口上的聚合](https://ci.apache.org/projects/flink/flink-docs-release-1.12/zh/dev/table/sql/queries.html#aggregations). |
