package com.xiaopy.esdemo;

import com.xiaopy.esdemo.util.JsonReflectionUtil;
import com.xiaopy.esdemo.util.Result;

/**
 * @author xiaopeiyu
 * @since 2021/2/3
 */
public class MineTest {
    public static void main(String[] args) {
        String json="{\n" +
                "\t\"@timestamp\": {\n" +
                "\t\t\"date\": \"2021-02-02T01:33:19.619Z\"\n" +
                "\t},\n" +
                "\t\"host\": {\n" +
                "\t\t\"os\": {\n" +
                "\t\t\t\"version\": \"centos 7\",\n" +
                "\t\t\t\"number\": 11\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"cloud\": {\n" +
                "\t\t\"availability_zone\": \"cn-north3\"\n" +
                "\t}\n" +
                "}";
        Result result = JsonReflectionUtil.parseObject(json, Result.class);
        System.out.println(result);
    }
}
