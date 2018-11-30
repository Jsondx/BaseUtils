package com.ldx.baseutils.http;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

/**
 * @author babieta
 * @date 2018/11/29
 */

public class BaseHttp {

    public HttpParams mCommonParams;
    public String url;
    public  IhttpCallBack ihttpCallBack;
    private Context context;

    public BaseHttp(Context context) {
        this.context = context;
    }

    public void get() {
        OkGo.<String>get(url).params(mCommonParams).execute(stringCallback);
    }

    public void post() {
        OkGo.<String>post(url).params(mCommonParams).execute(stringCallback);
    }

    private void callOnSuccess(JSONObject jsonObject) {
        BaseHttp.this.ihttpCallBack.onSuccess(jsonObject);
    }

    private void callOnFailure(String message) {
        BaseHttp.this.ihttpCallBack.onFailure(message);
    }

    public void onSuccess(JSONObject jsonObject) {

    }

    public void onFailure(String message) {
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
            JSONObject parse = parse(response.body());
            if (parse == null) {
                callOnFailure("数据解析失败" + response.body());
                return;
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
            BaseHttp.this.callOnFailure(response.message() + response.code());
            Log.e("TAG", "base http onError " + response.message() + response.code());
        }

        @Override
        public void onStart(Request<String, ? extends Request> request) {
            super.onStart(request);
        }
    };

}
