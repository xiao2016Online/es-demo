package com.xiaopy.esdemo.service;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xiaopeiyu
 * @since 2021/2/7
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class TermsServiceTest {

    @Autowired
    private TermsService service;

    @Test
    void getDateQuery() {
        service.getDateQuery("log-2021.01.24");
    }
}