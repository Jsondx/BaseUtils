package com.ldx.baseutils.https;

import com.google.gson.Gson;
import com.lzy.okgo.callback.AbsCallback;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * Created by babieta on 2018/12/7.
 */

public abstract class BaseCallBack<T> extends AbsCallback<T>  {
    private Gson gson;


    public BaseCallBack() {

    }

    /**
     * 局部代码块 在构造方法之后执行
     */ {
        gson = new Gson();
    }

    Class<T> doGetClass() {
        Type genType = this.getClass().getGenericSuperclass();
        System.out.print(genType);
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<T>) params[0];
    }


    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        ResponseBody body = response.body();
        if (body == null) return null;
        String string = body.string();
        T t = gson.fromJson(string, doGetClass());


        return t;
    }
}
