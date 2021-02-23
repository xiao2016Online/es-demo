package com.xiaopy.esdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaopeiyu
 * @since 2021/2/5
 */
@Slf4j
@Component
public class TermsService {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    public void testTerms(int count) {
        String prefix = "log-2020.12.25";
        List<String> list = clientService.getIndexList(prefix);
//        list.forEach(s -> {
//            taskExecutor.submit(()->{
//                Map<String, Long> query = getTermsQuery(s);
//                log.info(String.valueOf(query));
//            });
//        });
        for (int i = 0; i < count; i++) {
            taskExecutor.submit(() -> {
                Map<String, Long> query = getTermsQuery(list.get(0));
                log.info(String.valueOf(query));
                Map<String, Long> dateQuery = getDateQuery(list.get(0));
                log.info(String.valueOf(dateQuery));
            });
        }
    }

    /**
     * 桶聚合
     *
     * @param index
     * @return
     */
    public Map<String, Long> getTermsQuery(String index) {
        Map<String, Long> result = new HashMap<>();
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        TermsAggregationBuilder testTerms = AggregationBuilders.terms("test")
                .field("namespace_name.keyword");
        builder.aggregation(testTerms);
        searchRequest.source(builder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null == searchResponse) {
            return result;
        }
        Aggregations aggregations = searchResponse.getAggregations();
        Terms terms = aggregations.get("test");
        terms.getBuckets().forEach(bucket -> {
            String key = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();
            result.put(key, docCount);
            log.info("key={},value={}", key, docCount);
        });
        return result;
    }

    public Map<String, Long> getDateQuery(String index){
        Map<String, Long> result = new HashMap<>();
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        DateHistogramAggregationBuilder dateHistogram = AggregationBuilders.dateHistogram("date")
                .field("timestamp")
                .calendarInterval(DateHistogramInterval.HOUR)
                .format("yyyy-MM-dd HH");
        builder.aggregation(dateHistogram);
        searchRequest.source(builder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null == searchResponse) {
            return result;
        }
        Aggregations aggregations = searchResponse.getAggregations();
        Histogram histogram  = aggregations.get("date");
        histogram.getBuckets().forEach(bucket -> {
            String key = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();
            result.put(key, docCount);
            log.info("key={},value={}", key, docCount);
        });
        return result;
    }
}
