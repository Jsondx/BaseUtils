package com.ldx.baseutils.http;

import android.content.Context;

import com.lzy.okgo.model.HttpParams;

/**
 * @author babieta
 * @date 2018/11/30
 */

public class BaseHttpIml extends BaseHttp implements IHttpInterface {

    public BaseHttpIml(Context context) {
        super(context);
    }

    @Override
    public void setUrl(String thisurl) {
        url = thisurl;

    }

    @Override
    public void setParams(HttpParams thishttpParams) {
        mCommonParams = thishttpParams;
    }

    @Override
    public void setCallBack(IhttpCallBack stringCallback) {
        ihttpCallBack = stringCallback;
    }

    public static class Builder {
        private BaseHttp baseHttp;

        public Builder(Context context) {
            baseHttp = new BaseHttp(context);
        }

        public Builder setUrl(String thisurl) {
            baseHttp.url = thisurl;
            return this;
        }

        public Builder setParams(HttpParams httpParams) {
            baseHttp.mCommonParams = httpParams;
            return this;
        }

        public Builder setStringCallBack(IhttpCallBack stringCallBack) {
            baseHttp.ihttpCallBack = stringCallBack;
            return this;
        }


        public Builder post() {
            post();
            return this;
        }

        public Builder get() {
            get();
            return this;
        }
    }
}
