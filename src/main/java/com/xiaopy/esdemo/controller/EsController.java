package com.xiaopy.esdemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiaopy.esdemo.service.ClientService;
import com.xiaopy.esdemo.service.TermsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaopeiyu
 * @since 2021/1/19
 */
@RestController
public class EsController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private TermsService termService;

//    @GetMapping("/testfork")
//    public void testFork(){
//        clientService.test();
//    }
//
//
//    @GetMapping("/testsTerms500")
//    public void testTerms(){
//        termService.testTerms(500);
//        System.out.println(">>>>>>>>>>>>>>end");
//    }
//
//    @GetMapping("/testsTerms1000")
//    public void testsTerms1000(){
//        termService.testTerms(1000);
//        System.out.println(">>>>>>>>>>>>>>end");
//    }

    @GetMapping("/json1")
    public JSONObject json1(){
        String msg= "2021-02-07T15:51:55.442+0800#011INFO#011[monitoring]#011log/log.go:145#011Non-zero metrics in the last 30s#011{\"monitoring\": {\"metrics\": {\"beat\":{\"cpu\":{\"system\":{\"ticks\":837240,\"time\":{\"ms\":134}},\"total\":{\"ticks\":1293580,\"time\":{\"ms\":243},\"value\":1293580},\"user\":{\"ticks\":456340,\"time\":{\"ms\":109}}},\"handles\":{\"limit\":{\"hard\":4096,\"soft\":1024},\"open\":20},\"info\":{\"ephemeral_id\":\"506406d4-de93-4353-b287-a9f727c0563f\",\"uptime\":{\"ms\":259770217}},\"memstats\":{\"gc_next\":24100464,\"memory_alloc\":12213352,\"memory_total\":8687811592},\"runtime\":{\"goroutines\":70}},\"filebeat\":{\"events\":{\"added\":2,\"done\":2},\"harvester\":{\"files\":{\"428299f9-705c-4ad0-826b-dfdbbdc8acb7\":{\"last_event_published_time\":\"2021-02-07T15:51:30.804Z\",\"last_event_timestamp\":\"2021-02-07T15:51:25.803Z\",\"read_offset\":1397,\"size\":1397},\"84761dde-5700-4617-9c1b-472aec929b4b\":{\"last_event_published_time\":\"2021-02-07T15:51:26.098Z\",\"last_event_timestamp\":\"2021-02-07T15:51:26.098Z\",\"read_offset\":165,\"size\":165}},\"open_files\":3,\"running\":3}},\"libbeat\":{\"config\":{\"module\":{\"running\":1},\"scans\":3},\"output\":{\"events\":{\"acked\":2,\"batches\":2,\"total\":2}},\"pipeline\":{\"clients\":3,\"events\":{\"active\":0,\"published\":2,\"total\":2},\"queue\":{\"acked\":2}}},\"registrar\":{\"states\":{\"current\":18,\"update\":2},\"writes\":{\"success\":2,\"total\":2}},\"system\":{\"load\":{\"1\":0,\"15\":0.05,\"5\":0.03,\"norm\":{\"1\":0,\"15\":0.0125,\"5\":0.0075}}}}}}";
        msg=msg.replaceAll("\"",
                "\"");
        JSONObject result=new JSONObject();
        result.put("msg",msg);
        return result;
    }
}
