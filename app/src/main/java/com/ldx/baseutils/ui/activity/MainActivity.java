package com.ldx.baseutils.ui.activity;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.ldx.baseutils.demo.ConcreteBuilder;
import com.ldx.baseutils.demo.Director;
import com.ldx.baseutils.http.HttpUtils;
import com.ldx.baseutils.http.IhttpCallBack;
import com.ldx.baseutils.mvp.base.BaseActivity;
import com.ldx.baseutils.mvp.base.BasePresenter;
import com.ldx.baseutils.R;
import com.ldx.baseutils.mvp.presenter.MainPresenter;
import com.ldx.baseutils.mvp.view.MainView;
import com.lzy.okgo.model.HttpParams;

/**
 * @author babieta
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainView {


    @Override
    protected MainPresenter createP() {
        return new MainPresenter();
    }

    @NonNull
    @Override
    public int setView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        ConcreteBuilder concreteBuilder = new ConcreteBuilder();
        Director director = new Director(concreteBuilder);
        director.Construct("i7-6700", "三星DDR4", "希捷1T");

    }

    @Override
    protected void loadDate() {

    }

    public void onSendCode(View view) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("phone", "15064875827");
        HttpUtils.post(this, "http://testing.lailezhuanche.com/home/Register/sendCode", httpParams, new IhttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Log.e("TAG", jsonObject.toJSONString());
            }

            @Override
            public void onFailure(String message) {
                Log.e("TAG", message);
            }
        });
    }
}
