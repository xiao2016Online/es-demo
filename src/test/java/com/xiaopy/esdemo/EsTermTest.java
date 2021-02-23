package com.xiaopy.esdemo;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author xiaopeiyu
 * @since 2021/2/5
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class EsTermTest {

    @Autowired
    private RestHighLevelClient client;

    @Test
    public void testTerms() throws IOException {
        SearchRequest searchRequest = new SearchRequest("log-2021.01.09");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        TermsAggregationBuilder testTerms = AggregationBuilders.terms("test")
                .field("namespace_name.keyword");
        builder.aggregation(testTerms);
        searchRequest.source(builder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();
        Terms terms = aggregations.get("test");
        terms.getBuckets().forEach(bucket -> {
            String key = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();
            log.info("key={},value={}", key, docCount);
        });
    }
}
