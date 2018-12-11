package com.ldx.baseutils.http;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author babieta
 * @date 2018/11/29
 */

public  class BaseHttp {

    protected HttpParams mCommonParams;
    protected String url;
    protected IhttpCallBack ihttpCallBack;

    public BaseHttp() {

    }

    public HttpParams getmCommonParams() {
        return mCommonParams;
    }

    public void setmCommonParams(HttpParams mCommonParams) {
        this.mCommonParams = mCommonParams;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public IhttpCallBack getIhttpCallBack() {
        return ihttpCallBack;
    }

    public void setIhttpCallBack(IhttpCallBack ihttpCallBack) {
        this.ihttpCallBack = ihttpCallBack;
    }

    public void get() {
        OkGo.<String>get(url).params(mCommonParams).execute(stringCallback);
    }

    public void post() {
        OkGo.<String>post(url).params(mCommonParams).execute(stringCallback);
    }

    public void callOnSuccess(JSONObject jsonObject) {

    }

    public void callOnFailure(String message) {

    }


    private JSONObject parse(String responseString) { //将Json字符串 转换成JsonObject
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(responseString);
        } catch (Exception ex) {
        }
        return jsonObject;
    }

    private StringCallback stringCallback = new StringCallback() {
        @Override
        public void onSuccess(Response<String> response) {


            JSONObject parse = null;
            try {
                parse = parse(response.body());
                if (parse == null) {
                    callOnFailure("数据解析失败" + response.body());
                    return;
                }
            } catch (Exception e) {
                callOnFailure("数据解析失败" + response.body());
                e.printStackTrace();
            }


            if (parse.get("status").toString().equals("0")) {
                callOnFailure(parse.get("msg").toString());
                return;
            }
            callOnSuccess(parse);
        }

        @Override
        public void onError(Response<String> response) {
            super.onError(response);
          callOnFailure(response.message() + response.code());
            Log.e("TAG", "base http onError " + response.message() + response.code());
        }

        @Override
        public void onStart(Request<String, ? extends Request> request) {
            super.onStart(request);
        }
    };
}
