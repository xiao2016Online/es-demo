package com.xiaopy.esdemo.service;

import com.xiaopy.esdemo.domain.AppLog;
import com.xiaopy.esdemo.util.SearchHitUtil;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @author xiaopeiyu
 * @since 2021/1/29
 */
@Component
public class ClientService {

    @Autowired
    private RestHighLevelClient client;



    public void test() {
        String indexPrefix = "log-2021.01";
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask task = new ForkJoinTask(2, getIndexList(indexPrefix));
        long start = System.currentTimeMillis();
        java.util.concurrent.ForkJoinTask<List<AppLog>> task1 = pool.submit(task);
        try {
            List<AppLog> logs = task1.get();
            System.out.println(logs.size());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000);
    }


    private List<AppLog> queryData(String index) {
        // 创建SearchRequest对象
        SearchRequest searchRequest = new SearchRequest(index);
        // 通过SearchSourceBuilder构建搜索参数
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 通过QueryBuilders构建ES查询条件
        // matchPhraseQuery 分词精确匹配
        builder.query(QueryBuilders.matchPhraseQuery("namespace_name", "istio-system"));
        builder.size(10000);
        // 设置搜索参数
        searchRequest.source(builder);

        // 执行ES请求
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null == searchResponse) {
            return new ArrayList<>();
        }
        // 获取搜索到的文档
        SearchHits hits = searchResponse.getHits();
        // 遍历搜索结果
        return SearchHitUtil.searchHits2ModelList(hits, AppLog.class);
    }

    public List<String> getIndexList(String indexPrefix) {
        List<String> result = new ArrayList<>();
        GetAliasesRequest request = new GetAliasesRequest();
        GetAliasesResponse response = null;
        try {
            response = client.indices().getAlias(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null == response) {
            return result;
        }
//        Map<String, Set<AliasMetaData>> aliases = response.getAliases();
//        Set<String> strings = aliases.keySet();
//        for (String str : strings) {
//            if (str.contains(indexPrefix)) {
//                result.add(str);
//            }
//        }
        return result;
    }


    public class ForkJoinTask extends RecursiveTask<List<AppLog>> {

        private final int threshold;

        private final List<String> indexList;

        public ForkJoinTask(int threshold, List<String> indexList) {
            this.threshold = threshold;
            this.indexList = indexList;
        }

        @Override
        protected List<AppLog> compute() {
            if (indexList.size() <= threshold) {
                return getQueryData(indexList);
            } else {
                int mid = indexList.size() >> 1;
                List<String> leftList = indexList.subList(0, mid);
                List<String> rightList = indexList.subList(mid, indexList.size());
                ForkJoinTask leftTask = new ForkJoinTask(threshold, leftList);
                ForkJoinTask rightTask = new ForkJoinTask(threshold, rightList);
                invokeAll(leftTask, rightTask);
                List<AppLog> result = new ArrayList<>();
                result.addAll(leftTask.join());
                result.addAll(rightTask.join());
                return result;
            }
        }

        private List<AppLog> getQueryData(List<String> indexList) {
            List<AppLog> result = new ArrayList<>();
            for (String str : indexList) {
                result.addAll(queryData(str));
            }
            return result;
        }
    }
}
