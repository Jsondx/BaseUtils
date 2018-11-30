package com.ldx.baseutils.http;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;

/**
 * Created by babieta on 2018/11/30.
 */

public class BaseHttpController {

    private String url;
    private HttpParams httpParams;
    private StringCallback stringCallback;

    public StringCallback getStringCallback() {
        return stringCallback;
    }

    public void setStringCallback(StringCallback stringCallback) {
        this.stringCallback = stringCallback;
    }

    public HttpParams getHttpParams() {
        return httpParams;
    }

    public void setHttpParams(HttpParams httpParams) {
        this.httpParams = httpParams;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
