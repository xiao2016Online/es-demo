package com.xiaopy.esdemo.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.xiaopy.esdemo.annotation.JSONParseField;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Objects;

/**
 * @author xiaopeiyu
 * @since 2021/2/2
 */
public class JsonReflectionUtil {


    /**
     * 将Json 字符串解析成特定对象
     *
     * @param json json字符串
     * @param cls  指定类
     * @param <T>  类型
     * @return 特定类型的对象
     */
    public static <T> T parseObject(String json, Class<T> cls) {
        T result = null;
        try {
            result = cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (null == result) {
            return null;
        }
        return parseObject(result, json, cls);
    }

    /**
     * 根据Json 更新对象的属性值
     *
     * @param t    待更新对象
     * @param json json字符串
     * @param cls  指定类
     * @param <T>  类型
     * @return 更新后的对象
     */
    public static <T> T updateObject(T t, String json, Class<T> cls) {
        return parseObject(t, json, cls);
    }


    private static <T> T parseObject(T t, String json, Class<T> cls) {
        Objects.requireNonNull(t);
        Field[] declaredFields = cls.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            JSONParseField annotation = field.getAnnotation(JSONParseField.class);
            String path =null;
            String aliasName =null;
            if(null!=annotation){
                path = StringUtils.isEmpty(annotation.path()) ? "" : annotation.path();
                aliasName = StringUtils.isEmpty(annotation.aliasName()) ? field.getName() : annotation.aliasName();
            }else {
                aliasName=field.getName();
            }
            Object value = null;
            String tmpJson=json;
            if (!StringUtils.isEmpty(path)) {
                Object read = JSONPath.read(json, path);
                tmpJson= JSONObject.toJSONString(read);
            }
            JSONObject object = JSONObject.parseObject(tmpJson);
            value = get(field, aliasName, object);
            try {
                if (null != value) {
                    field.set(t, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return t;
    }


    private static Object get(Field field, String name, JSONObject object) {
        Object result = null;
        if (field.getType() == String.class) {
            result = object.getString(name);
        } else if (field.getType() == Date.class) {
            result = object.getDate(name);
        } else if (field.getType() == int.class) {
            result = object.getIntValue(name);
        } else if (field.getType() == long.class) {
            result = object.getLongValue(name);
        } else if (field.getType() == float.class) {
            result = object.getFloatValue(name);
        } else if (field.getType() == Integer.class) {
            result = object.getInteger(name);
        } else if (field.getType() == Long.class) {
            result = object.getLong(name);
        } else if (field.getType() == Float.class) {
            result = object.getFloat(name);
        } else {
            result = object.get(name);
        }
        return result;
    }

}
