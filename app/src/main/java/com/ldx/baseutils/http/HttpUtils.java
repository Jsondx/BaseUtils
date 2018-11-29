package com.ldx.baseutils.http;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.model.HttpParams;

/**
 *
 * @author babieta
 * @date 2018/11/29
 */

public class HttpUtils {

    public static void post(Context context, String url, HttpParams httpParams, final IhttpCallBack callback) {
//        if (!NetworkUtils.isAvailable(context)) {
//            return;
//        }
        HttpRequest httpRequest = new HttpRequest(context) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                callback.onSuccess(jsonObject);
            }

            @Override
            public void onFailure(String message) {
                callback.onFailure(message);
            }
        };
        httpRequest.url = url;
        httpRequest.mCommonParams = httpParams;
        httpRequest.post();

    }

}
