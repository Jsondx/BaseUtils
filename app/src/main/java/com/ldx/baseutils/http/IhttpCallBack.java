package com.ldx.baseutils.http;

import com.alibaba.fastjson.JSONObject;

/**
 * @author ly
 * @date 2016/11/25
 */

public interface IhttpCallBack<T> {
    /**
     * 成功
     *
     * @param jsonObject
     */
    void onSuccess(JSONObject jsonObject, T t);

    /**
     * error信息
     *
     * @param message
     */
    void onFailure(String message);
}
