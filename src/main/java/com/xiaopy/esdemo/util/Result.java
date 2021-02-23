package com.xiaopy.esdemo.util;

import com.xiaopy.esdemo.annotation.JSONParseField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author xiaopeiyu
 * @since 2021/2/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Result extends EsBaseModel implements Serializable {

    @JSONParseField(aliasName = "date",path = "\\@timestamp")
    @com.alibaba.fastjson.annotation.JSONField
    private String timestamp;

    @JSONParseField(aliasName = "version", path = "host.os")
    private String hostOsVersion;

    @JSONParseField(aliasName = "availability_zone", path = "cloud")
    private String az;

    @JSONParseField(path ="host.os")
    private int number;
}
