package org.github.mbmll;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.ververica.cdc.connectors.mysql.MySQLSource;
import com.alibaba.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.alibaba.ververica.cdc.debezium.DebeziumDeserializationSchema;
import com.alibaba.ververica.cdc.debezium.DebeziumSourceFunction;
import io.debezium.data.Envelope;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception { //1. 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //2. 创建 Flink-MySQL-CDC 的 Source
        DebeziumSourceFunction<String> mysqlSource = MySQLSource.<String>builder()
            .hostname("node101")
            .port(3306)
            .username("root")
            .password("000000")
            .databaseList("gmall2021")
            .startupOptions(StartupOptions.latest())
            .deserializer(new DebeziumDeserializationSchema<String>() {
                @Override
                public TypeInformation<String> getProducedType() {
                    return null;
                }

                // 自定义数据解析器
                @Override
                public void deserialize(SourceRecord sourceRecord, Collector<String> collector) throws Exception {
                    // 获取主题信息 包含着数据库和表名
                    String topic = sourceRecord.topic();
                    String[] arr = topic.split("\\.");
                    String db = arr[1];
                    String tableName = arr[2];
                    // 获取操作类型
                    Envelope.Operation operation = Envelope.operationFor(sourceRecord);

                    // 获取值信息并转换为 Struct 类型
                    Struct value = (Struct) sourceRecord.value();

                    // 获取变化后的数据
                    Struct after = value.getStruct("after");

                    // 创建 JSON 对象用于存储数据信息
                    JSONObject data = new JSONObject();
                    if (after != null) {
                        Schema schema = after.schema();
                        for (Field field : schema.fields()) {
                            data.put(field.name(), after.get(field.name()));
                        }
                    }

                    // 创建 JSON 对象用于封装最终返回值数据信息
                    JSONObject result = new JSONObject();
                    result.put("operation", operation.toString().toLowerCase());
                    result.put("data", data);
                    result.put("database", db);
                    result.put("table", tableName);

                    // 发送数据到下游
                    collector.collect(result.toJSONString());
                }
            }).build();

        // 3. 使用 CDC Source 从 MySQL 读取数据
        DataStreamSource<String> mysqlDS = env.addSource(mysqlSource);

        // 4. 打印数据
        mysqlDS.addSink(MyKafkaUtil.getKafkaSink("ods_base_db"));

        // 5. 执行任务
        env.execute();
    }
}

