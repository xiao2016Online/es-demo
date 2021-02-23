package com.xiaopy.esdemo;

import com.alibaba.fastjson.JSONObject;
import com.xiaopy.esdemo.util.Result;
import com.xiaopy.esdemo.util.SearchHitUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author xiaopeiyu
 * @since 2021/1/29
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class ESQueryTest {

    @Autowired
    private RestHighLevelClient client;


    /**
     * 分词匹配查询：在执行查询时，搜索的词会被分词器分词
     *
     * @throws IOException
     */
    @Test
    public void testMatchQuery() throws IOException {
        // 创建SearchRequest对象
        SearchRequest searchRequest = new SearchRequest("ila-demo-*");
        // 通过SearchSourceBuilder构建搜索参数
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 通过QueryBuilders构建ES查询条件
        // matchQuery 分词精确匹配
        builder.query(QueryBuilders.matchQuery("address", "Lane Holmes "));
        // 设置搜索参数
        searchRequest.source(builder);

        System.out.println(null == client);
        // 执行ES请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // 获取搜索到的文档
        SearchHits hits = searchResponse.getHits();
        // 遍历搜索结果
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            // 获取文档内容，json字符串格式
            String sourceAsString = hit.getSourceAsString();
            // 获取文档内容，Map对象格式
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            JSONObject object = new JSONObject(sourceAsMap);
            System.out.println(object.toJSONString());
        }
    }


    /**
     * 分词匹配查询：在执行查询时，匹配(包含)
     *
     * @throws IOException
     */
    @Test
    public void testPhraseQuery() throws IOException {
        // 创建SearchRequest对象
        SearchRequest searchRequest = new SearchRequest("ila-demo-*");
        // 通过SearchSourceBuilder构建搜索参数
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 通过QueryBuilders构建ES查询条件
        // matchPhraseQuery 分词精确匹配
        builder.query(QueryBuilders.matchPhraseQuery("fields.accountId", "accountId"));
        builder.size(100);
        // 设置搜索参数
        searchRequest.source(builder);

        // 执行ES请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // 获取搜索到的文档
        SearchHits hits = searchResponse.getHits();
        // 遍历搜索结果
        List<Result> results = SearchHitUtil.searchHits2ModelList(hits, Result.class);
        results.forEach(System.out::println);

//        for (SearchHit hit : searchHits) {
//            // 获取文档内容，json字符串格式
//            String sourceAsString = hit.getSourceAsString();
//            System.out.println(sourceAsString);
//            Result result = ReflectionUtil.parseObject(sourceAsString, Result.class);
//            Result result1 = SearchHitUtil.json2Model(hit, Result.class);
//            System.out.println(result1);
//            System.out.println(result1.getId());
////            // 获取文档内容，Map对象格式
////            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
////            System.out.println(sourceAsMap.get("host.architecture"));
//            break;
//        }
    }


    /**
     * term查询，主要用于实现等值匹配，类似SQL的fieldname=value表达式。
     *
     * @throws IOException
     */
    @Test
    public void testTermQuery() throws IOException {
        // 创建SearchRequest对象
        SearchRequest searchRequest = new SearchRequest("bank");
        // 通过SearchSourceBuilder构建搜索参数
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 通过QueryBuilders构建ES查询条件
        // matchPhraseQuery 分词精确匹配
        builder.query(QueryBuilders.termQuery("address", "800"));
        // 设置搜索参数
        searchRequest.source(builder);

        // 执行ES请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // 获取搜索到的文档
        SearchHits hits = searchResponse.getHits();
        // 遍历搜索结果
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            // 获取文档内容，json字符串格式
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
            // 获取文档内容，Map对象格式
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);
        }
    }

    /**
     * term查询，用于实现类似SQL的in语句，匹配其中一个值即可
     *
     * @throws IOException
     */
    @Test
    public void testTermsQuery() throws IOException {
        // 创建SearchRequest对象
        SearchRequest searchRequest = new SearchRequest("bank");
        // 通过SearchSourceBuilder构建搜索参数
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 通过QueryBuilders构建ES查询条件
        // matchPhraseQuery 分词精确匹配
        builder.query(QueryBuilders.termsQuery("address", "800", "Holmes"));
        // 设置搜索参数
        searchRequest.source(builder);

        // 执行ES请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // 获取搜索到的文档
        SearchHits hits = searchResponse.getHits();
        // 遍历搜索结果
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            // 获取文档内容，json字符串格式
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
            // 获取文档内容，Map对象格式
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);
        }
    }


    /**
     * 实现类似SQL语句中的>, >=, <, <=关系表达式
     *
     * @throws IOException
     */
    @Test
    public void testRangeQuery() throws IOException {
        // 创建SearchRequest对象
        SearchRequest searchRequest = new SearchRequest("bank");
        // 通过SearchSourceBuilder构建搜索参数
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 通过QueryBuilders构建ES查询条件
        // matchPhraseQuery 分词精确匹配
        builder.query(QueryBuilders.rangeQuery("age").gte(20).lte(30));
        // 设置搜索参数
        searchRequest.source(builder);

        // 执行ES请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // 获取搜索到的文档
        SearchHits hits = searchResponse.getHits();
        // 遍历搜索结果
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            // 获取文档内容，json字符串格式
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
            // 获取文档内容，Map对象格式
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);
        }
    }

    @Test
    public void testBoolQuery() throws IOException {
        // 创建SearchRequest对象
        SearchRequest searchRequest = new SearchRequest("bank");
        // 通过SearchSourceBuilder构建搜索参数
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 通过QueryBuilders构建ES查询条件
        // boolQuery 构建
        builder.query(QueryBuilders.boolQuery()
                .must(QueryBuilders.matchPhraseQuery("address", "Holmes Lane"))
                .should(QueryBuilders.rangeQuery("age").gte(20).lte(30)));
        // 设置搜索参数
        searchRequest.source(builder);

        // 执行ES请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // 获取搜索到的文档
        SearchHits hits = searchResponse.getHits();
        // 遍历搜索结果
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            // 获取文档内容，json字符串格式
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
            // 获取文档内容，Map对象格式
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);
        }
    }

}
