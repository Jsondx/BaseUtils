package com.ldx.baseutils.http;

import com.alibaba.fastjson.JSONObject;

/**
 * @author ly
 * @date 2016/11/25
 */

public interface IhttpCallBack{
    /**
     * 成功
     *
     * @param jsonObject
     */
    void onSuccess(JSONObject jsonObject);

    /**
     * error信息
     *
     * @param message
     */
    void onFailure(String message);
}
