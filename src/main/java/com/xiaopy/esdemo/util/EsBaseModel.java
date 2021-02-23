package com.xiaopy.esdemo.util;

import lombok.Data;

/**
 * @author xiaopeiyu
 * @since 2021/2/2
 */
@Data
public abstract class EsBaseModel {

    protected String id;

    protected String index;

}
