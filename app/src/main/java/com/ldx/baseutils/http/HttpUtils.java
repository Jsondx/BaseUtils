package com.ldx.baseutils.http;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.model.HttpParams;

/**
 * @author babieta
 * @date 2018/11/29
 */

public  class HttpUtils {

    public static void post(Context context, String url, HttpParams httpParams, IhttpCallBack ihttpCallBack) {

        BaseHttpIml.Builder builder = new BaseHttpIml.Builder(context)
                .setUrl(url)
                .setParams(httpParams)
                .setStringCallBack(ihttpCallBack);
        builder.post();

    }

}
