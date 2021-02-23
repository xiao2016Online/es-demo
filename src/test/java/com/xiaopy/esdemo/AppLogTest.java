package com.xiaopy.esdemo;

import com.xiaopy.esdemo.domain.AppLog;
import com.xiaopy.esdemo.util.SearchHitUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

/**
 * @author xiaopeiyu
 * @since 2021/2/4
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class AppLogTest {

    @Autowired
    private RestHighLevelClient client;



    @Test
    public void test(){
        int a=10+100>>1;
        System.out.println(a);

        String msg= "2021-02-07T15:51:55.442+0800#011INFO#011[monitoring]#011log/log.go:145#011Non-zero metrics in the last 30s#011{\"monitoring\": {\"metrics\": {\"beat\":{\"cpu\":{\"system\":{\"ticks\":837240,\"time\":{\"ms\":134}},\"total\":{\"ticks\":1293580,\"time\":{\"ms\":243},\"value\":1293580},\"user\":{\"ticks\":456340,\"time\":{\"ms\":109}}},\"handles\":{\"limit\":{\"hard\":4096,\"soft\":1024},\"open\":20},\"info\":{\"ephemeral_id\":\"506406d4-de93-4353-b287-a9f727c0563f\",\"uptime\":{\"ms\":259770217}},\"memstats\":{\"gc_next\":24100464,\"memory_alloc\":12213352,\"memory_total\":8687811592},\"runtime\":{\"goroutines\":70}},\"filebeat\":{\"events\":{\"added\":2,\"done\":2},\"harvester\":{\"files\":{\"428299f9-705c-4ad0-826b-dfdbbdc8acb7\":{\"last_event_published_time\":\"2021-02-07T15:51:30.804Z\",\"last_event_timestamp\":\"2021-02-07T15:51:25.803Z\",\"read_offset\":1397,\"size\":1397},\"84761dde-5700-4617-9c1b-472aec929b4b\":{\"last_event_published_time\":\"2021-02-07T15:51:26.098Z\",\"last_event_timestamp\":\"2021-02-07T15:51:26.098Z\",\"read_offset\":165,\"size\":165}},\"open_files\":3,\"running\":3}},\"libbeat\":{\"config\":{\"module\":{\"running\":1},\"scans\":3},\"output\":{\"events\":{\"acked\":2,\"batches\":2,\"total\":2}},\"pipeline\":{\"clients\":3,\"events\":{\"active\":0,\"published\":2,\"total\":2},\"queue\":{\"acked\":2}}},\"registrar\":{\"states\":{\"current\":18,\"update\":2},\"writes\":{\"success\":2,\"total\":2}},\"system\":{\"load\":{\"1\":0,\"15\":0.05,\"5\":0.03,\"norm\":{\"1\":0,\"15\":0.0125,\"5\":0.0075}}}}}}";
        System.out.println(msg);
    }

    @Test
    public void testIndex() throws  IOException{
        GetAliasesRequest request = new GetAliasesRequest();
        GetAliasesResponse response = client.indices().getAlias(request, RequestOptions.DEFAULT);
//        Map<String, Set<AliasMetaData>> aliases = response.getAliases();
//        Set<String> strings = aliases.keySet();
//        for (String str:strings){
//            if(str.contains("log-2021")){
//                System.out.println(str);
//            }
//        }
    }

    @Test
    public void testPhraseQuery() throws IOException {
        // 创建SearchRequest对象
        SearchRequest searchRequest = new SearchRequest("log-2021.01.15");
        // 通过SearchSourceBuilder构建搜索参数
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 通过QueryBuilders构建ES查询条件
        // matchPhraseQuery 分词精确匹配
        builder.query(QueryBuilders.matchPhraseQuery("namespace_name", "istio-system"));
        builder.size(100);
        // 设置搜索参数
        searchRequest.source(builder);

        // 执行ES请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // 获取搜索到的文档
        SearchHits hits = searchResponse.getHits();
        // 遍历搜索结果
        List<AppLog> results = SearchHitUtil.searchHits2ModelList(hits, AppLog.class);
        results.forEach(System.out::println);
    }
}
