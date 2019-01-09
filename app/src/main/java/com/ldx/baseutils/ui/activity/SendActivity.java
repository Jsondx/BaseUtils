package com.ldx.baseutils.ui.activity;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.ldx.baseutils.R;
import com.ldx.baseutils.mvp.base.BaseActivity;
import com.ldx.baseutils.mvp.base.BasePresenter;
import com.ldx.baseutils.okrx2.ServerApi;
import com.ldx.baseutils.utils.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.adapter.ObservableResponse;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author babieta
 * @date 2018/12/20
 */

public class SendActivity extends BaseActivity {
    @Override
    protected BasePresenter createP() {
        return null;
    }

    @NonNull
    @Override
    public int setView() {
        return R.layout.ac_send;
    }

    @Override
    protected void initView() {
        Button btn = findViewById(R.id.btn_send);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = "15064875598";
                String url = "http://testing.lailezhuanche.com/home/Register/sendCode";
                HttpParams httpParams = new HttpParams();
                httpParams.put("phone", phone);
                ServerApi.postString(url, httpParams).subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                LogUtils.e("show Dialog");
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                LogUtils.e("onSubscribe= " + d.toString());
                            }

                            @Override
                            public void onNext(String s) {
                                LogUtils.e("onNext= " + s);
                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtils.e("onError= " + e.toString());
                            }

                            @Override
                            public void onComplete() {
                                LogUtils.e("onComplete= " );
                            }
                        });
            }

        });
    }

    @Override
    protected void loadDate() {

    }
}
