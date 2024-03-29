package org.github.mbmll.example.es.service;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Author xlc
 * @Description
 * @Date 2023/11/17 12:41
 */
@Slf4j
@Service
public class IndexService2 {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 创建索引
     */
    public void createIndex() {
        // 创建 Mapping
        try (XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().field("dynamic", true)
                .startObject("properties")
                .startObject("name").field("type", "text").startObject("fields").startObject("keyword")
                .field("type", "keyword").endObject().endObject().endObject().startObject("address")
                .field("type", "text").startObject("fields").startObject("keyword").field("type", "keyword")
                .endObject().endObject().endObject().startObject("remark").field("type", "text")
                .startObject("fields").startObject("keyword").field("type", "keyword").endObject()
                .endObject().endObject().startObject("age").field("type", "integer").endObject()
                .startObject("salary").field("type", "float").endObject().startObject("birthDate")
                .field("type", "date").field("format", "yyyy-MM-dd").endObject().startObject("createTime")
                .field("type", "date").endObject().endObject().endObject()) {
            // 创建索引配置信息，配置
            Settings settings =
                    Settings.builder().put("index.number_of_shards", 1).put("index.number_of_replicas", 0).build();
            // 新建创建索引请求对象，然后设置索引类型（ES 7.0 将不存在索引类型）和 mapping 与 index 配置
            CreateIndexRequest request = new CreateIndexRequest("mydlq-user", settings);
            request.mapping("doc", mapping);
            // RestHighLevelClient 执行创建索引
            CreateIndexResponse createIndexResponse =
                    restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            // 判断是否创建成功
            boolean isCreated = createIndexResponse.isAcknowledged();
            log.info("是否创建成功：{}", isCreated);
        } catch (IOException e) {
            log.error("", e);
        }
    }

    /**
     * 删除索引
     */
    public void deleteIndex() {
        try {
            // 新建删除索引请求对象
            DeleteIndexRequest request = new DeleteIndexRequest("mydlq-user");
            // 执行删除索引
            AcknowledgedResponse acknowledgedResponse =
                    restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
            // 判断是否删除成功
            boolean siDeleted = acknowledgedResponse.isAcknowledged();
            log.info("是否删除成功：{}", siDeleted);
        } catch (IOException e) {
            log.error("", e);
        }
    }

}
