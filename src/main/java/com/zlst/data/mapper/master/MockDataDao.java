package com.zlst.data.mapper.master;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zlst.data.config.ElasticsearchRestClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @description: mock data dao
 * @author: Quentin Zhang
 * @create: 2020-10-14 11:32
 **/
@Component
@Slf4j
public class MockDataDao {
    private static final String INDEX="product_live_2020";

    private static final String TYPE="_doc";

    @Resource
    ElasticsearchRestClientConfig elasticsearchRestClientConfig;

    @Autowired
    private ObjectMapper objectMapper;

    public void bulkInsert(List mockList){
        RestHighLevelClient restClient =  new RestHighLevelClient(elasticsearchRestClientConfig.restClientBuilder());
        BulkRequest bulkRequest = new BulkRequest();
        mockList.forEach(mock -> {
            IndexRequest indexRequest = new IndexRequest(INDEX).
                    source(objectMapper.convertValue(mock, Map.class));
            bulkRequest.add(indexRequest);
        });

        try {
            BulkResponse bulkResponse = restClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            if(bulkResponse.hasFailures())
            {
                log.error(bulkResponse.buildFailureMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
