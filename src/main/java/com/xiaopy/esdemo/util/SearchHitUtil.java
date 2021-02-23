package com.xiaopy.esdemo.util;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaopeiyu
 * @since 2021/2/2
 */
public class SearchHitUtil {

    public static <T extends EsBaseModel> List<T> searchHits2ModelList(SearchHits searchHits, Class<T> cls) {
        List<T> result = new ArrayList<>();
        for (SearchHit searchHit : searchHits.getHits()) {
            result.add(searchHit2Model(searchHit, cls));
        }
        return result;
    }

    public static <T extends EsBaseModel> T searchHit2Model(SearchHit searchHit, Class<T> cls) {
        T result = null;
        try {
            result =  cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (null == result) {
            return null;
        }
        Field id = null;
        Field index = null;
        try {
            id = cls.getSuperclass().getDeclaredField("id");
            id.setAccessible(true);
            id.set(result, searchHit.getId());

            index = cls.getSuperclass().getDeclaredField("index");
            index.setAccessible(true);
            index.set(result, searchHit.getIndex());

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return JsonReflectionUtil.updateObject(result, searchHit.getSourceAsString(), cls);
    }
}
