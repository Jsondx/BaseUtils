package com.ldx.baseutils.mvp.base;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

/**
 * 所有Presenter都要求继承该类
 *
 * @author Suning
 * @date 2018/4/9
 */

public abstract class BasePresenter<V extends BaseView, T extends BaseModle> {
    private V v;
    private V proxyv;

    public void attchView(final V a) {
        this.v = a;


        proxyv = (V) Proxy.newProxyInstance(v.getClass().getClassLoader(), v.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (!isNull()) {
                    return null;
                } else {
                    return method.invoke(v, args);
                }

            }
        });
    }

    private boolean isNull() {
        if (this.v == null) {
            return false;
        }
        return true;
    }

    public V getV() {
        return proxyv;
    }

    public void dettchView() {
        this.v = null;
    }

    protected T t;


    public T getmodle() {
        if (t == null) {
            Type t = getClass().getGenericSuperclass();
            if (t instanceof ParameterizedType) {
                Type[] p = ((ParameterizedType) t).getActualTypeArguments();
                Class aClass = (Class<T>) p[1];
                try {
                    this.t = (T) aClass.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return t;
    }
}
