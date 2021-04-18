package com.ls.libnetwork;

import java.lang.reflect.Type;

/**
 * 在解析返回的结果时，需要使用Convert对结果进行解析
 * @param <T>
 */
public interface Convert<T> {

    /**
     * 通过 TYPE  进行转换
     * @param response
     * @param type
     * @return
     */
    T convert(String response, Type type);

    /**
     * 指明返回体的Class进行转换
     * @param response
     * @param claz
     * @return
     */
    T convert(String response, Class claz);
}
