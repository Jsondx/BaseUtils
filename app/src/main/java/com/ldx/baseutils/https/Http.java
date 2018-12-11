package com.ldx.baseutils.https;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

/**
 * Created by babieta on 2018/12/7.
 */

public class Http {

    public static void post(String url, HttpParams httpParams, BaseCallBack baseCallBack) {
        OkGo.<String>post(url).params(httpParams).execute(baseCallBack);
    }
}
