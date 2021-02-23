package com.xiaopy.esdemo;

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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author xiaopeiyu
 * @since 2021/1/30
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ESAggregationsTest {

    @Autowired
    private RestHighLevelClient client;

    @Test
    public void testTerms1() throws IOException {

        // 创建SearchRequest对象, 设置索引名=order
        SearchRequest searchRequest = new SearchRequest("ila-demo-2021.02");
        // 通过SearchSourceBuilder构建搜索参数
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 通过QueryBuilders构建ES查询条件，这里查询所有文档，复杂的查询语句设置请参考前面的章节。
        builder.query(QueryBuilders.matchAllQuery());

        TermsAggregationBuilder terms = AggregationBuilders.terms("test").field("service.type");
        TermsAggregationBuilder terms1 = AggregationBuilders.terms("test2").field("fields.ip");
        terms.subAggregation(terms1);
        builder.size(0);
        builder.aggregation(terms);
        // 设置搜索条件
        searchRequest.source(builder);

        // 执行ES请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();
        Terms termsTmp= aggregations.get("test");
        termsTmp.getBuckets().forEach(bucket -> {
            String keyAsString = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();
            log.info("keyAsString={},docCount={}",keyAsString,docCount);
            Aggregations sub = bucket.getAggregations();
            Terms termsTmp2 = sub.get("test2");
            for(Terms.Bucket bucket1  : termsTmp2.getBuckets()){
                String keyAsString1 = bucket1.getKeyAsString();
                long docCount1 = bucket1.getDocCount();
                log.info("keyAsString1={},docCount1={}",keyAsString1,docCount1);
            }
        });
    }

    @Test
    public void testTerms() throws IOException {
        // 创建SearchRequest对象, 设置索引名=order
        SearchRequest searchRequest = new SearchRequest("ila-demo-*");
        // 通过SearchSourceBuilder构建搜索参数
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 通过QueryBuilders构建ES查询条件，这里查询所有文档，复杂的查询语句设置请参考前面的章节。
        builder.query(QueryBuilders.matchAllQuery());
        // 创建时间聚合
        DateHistogramAggregationBuilder time = AggregationBuilders.dateHistogram("sales_over_time")
                .field("@timestamp")
                .calendarInterval(DateHistogramInterval.DAY)
                .format("yyyy-MM-dd");

        // 设置聚合条件
        builder.aggregation(time);

        // 设置搜索条件
        searchRequest.source(builder);

        // 执行ES请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        // 处理聚合查询结果
        Aggregations aggregations = searchResponse.getAggregations();
        // 根据by_shop名字查询terms聚合结果
        Histogram histogram = aggregations.get("sales_over_time");

        // 遍历terms聚合结果
        for (Histogram.Bucket bucket : histogram.getBuckets()) {
            // 获取桶的Key值
            String key = bucket.getKeyAsString();
            // 获取文档总数
            long count = bucket.getDocCount();
            log.info("key={},count={}", key, count);
        }
    }
}
