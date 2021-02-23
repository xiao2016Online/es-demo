package com.xiaopy.esdemo.domain;

import com.xiaopy.esdemo.annotation.JSONParseField;
import com.xiaopy.esdemo.util.EsBaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author xiaopeiyu
 * @since 2021/2/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppLog extends EsBaseModel implements Serializable {

    @JSONParseField(aliasName = "pod_name")
    private String podName;

    @JSONParseField(aliasName = "namespace_name")
    private String namespace;

    @JSONParseField(aliasName = "container_name")
    private String containerName;

    @JSONParseField(aliasName = "Payload")
    private String payLoad;

    @JSONParseField(aliasName = "user_name")
    private String userName;

    @JSONParseField(aliasName = "cluster_name")
    private String clusterName;

    @JSONParseField(aliasName = "cluster_id")
    private String clusterId;

    @JSONParseField(aliasName = "Hostname")
    private String hostName;

    private String timestamp;

    @JSONParseField(aliasName = "long_time")
    private long longTime;

}
