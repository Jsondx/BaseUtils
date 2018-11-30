package com.ldx.baseutils.http;

import com.lzy.okgo.model.HttpParams;

/**
 * @author babieta
 * @date 2018/11/30
 */

public interface IHttpInterface {

    void setUrl(String url);

    void setParams(HttpParams httpParams);

    void setCallBack(IhttpCallBack stringCallback);
}
