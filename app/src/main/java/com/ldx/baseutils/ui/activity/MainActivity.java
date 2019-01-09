package com.ldx.baseutils.ui.activity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ldx.baseutils.R;
import com.ldx.baseutils.https.BaseCallBack;
import com.ldx.baseutils.https.Http;
import com.ldx.baseutils.mvp.base.BaseActivity;
import com.ldx.baseutils.mvp.presenter.MainPresenter;
import com.ldx.baseutils.mvp.view.MainView;
import com.ldx.baseutils.server.ScreenListener;
import com.ldx.baseutils.utils.LogUtils;
import com.ldx.baseutils.utils.androidutilcode.util.SPUtils;
import com.ldx.baseutils.view.SlideRightViewDragHelper;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.adapter.ObservableResponse;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * @author babieta
 */

public class MainActivity extends BaseActivity<MainPresenter> implements MainView, ScreenListener.ScreenStateListener {
    private static final String TAG = "TAG";
    private boolean flag = false;
    private Disposable mDisposable;

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

        Button btn_sp = findViewById(R.id.btn_sp);
        Button btn_sp_clean = findViewById(R.id.btn_sp_clean);


        btn_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.getInstance().put("key", "你是Java");
            }
        });
        btn_sp_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.getInstance().clear();
            }
        });

        ScreenListener screenListener = new ScreenListener(this);
        screenListener.begin(this);


        //被观察者 相当于连载小说
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("连载1");
                e.onNext("连载2");
                e.onNext("连载3");
                e.onNext("连载4");
                e.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribeOn(Schedulers.io())               //执行在IO线程
                .subscribe(new Observer<String>() {         //观察者 读者
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                        Log.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(String value) {
                        if ("连载2".equals(value)) {
                            // 如果读者不想看了。可以通过dispose 取消订阅
                            mDisposable.dispose();
                            Log.e(TAG, "onNext: 我不想看小说了。 ");
                            return;
                        }
                        Log.e(TAG, "onNext:" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError=" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete()");
                    }
                });


        Observable<Response<String>> aaa = OkGo.<String>post("")
                .params("aaa", "123")
                .converter(new StringConvert())
                .adapt(new ObservableResponse<String>());

    }

    @Override
    protected void loadDate() {
    }

    @SuppressLint("NewApi")
    public void requestPermission(View view) {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setContentInfo(TimeUtils.getTimeZoneDatabaseVersion());
        builder.setContentText("11111111111111111");
        builder.setContentTitle("title");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("22222222222222222222222222222222");
        builder.setAutoCancel(true);
        builder.setWhen(100000000);
        Intent intent = new Intent(this, SendActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivities(getApplicationContext(), 0, new Intent[]{intent}, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notificationManager.notify(2, notification);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtils.e("-----> onSaveInstanceState  ");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtils.e("-----> onRestoreInstanceState  ");
    }

    @Override
    public void onScreenOn() {
        //开屏
        LogUtils.e("---->  onScreenOn ");
    }

    @Override
    public void onScreenOff() {
        //锁屏
        LogUtils.e("---->  onScreenOff ");
    }

    @Override
    public void onUserPresent() {
        //解锁
        LogUtils.e("---->  onUserPresent ");
    }
}
