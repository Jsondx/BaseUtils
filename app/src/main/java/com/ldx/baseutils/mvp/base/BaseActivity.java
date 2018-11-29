package com.ldx.baseutils.mvp.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ldx.baseutils.utils.ActivityManger;
import com.ldx.baseutils.utils.DeflateLoading;


/**
 * @author Suning
 * @date 2018/4/9
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {
    private P p;
    private LoadingIml loadingIml;
    private Toast toast;

    public P getP() {
        return p;
    }

    protected abstract P createP();

    private View rootView;

    public View getRootview() {
        return rootView;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManger.addac(this);
        p = createP();
        if (p != null) {
            p.attchView(this);
        }
        setContentView(setView());
//        View decorView = getWindow().getDecorView();
//        ViewGroup content = decorView.findViewById(android.R.id.content);
//        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        LinearLayout linearLayout = (LinearLayout) content.getChildAt(0);
//        rootView = getLayoutInflater().inflate(setView(), linearLayout, false);
//        linearLayout.addView(rootView);
//        if (hideTitle()) {
//            findViewById(R.id.rela_topbar).setVisibility(View.GONE);
//        }
//        TextView tv_title = findViewById(R.id.tv_title);
//        if (TextUtils.isEmpty(setTitle())) {
//            tv_title.setText("");
//        } else {
//            tv_title.setText(setTitle());
//        }
        initView();
        loadDate();
    }

    @NonNull
    public abstract int setView();

    protected abstract void initView();

    protected abstract void loadDate();

//    protected abstract String setTitle();
//
//    protected abstract boolean hideTitle();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideLoading();
        ActivityManger.removeac(this);
        if (p != null) {
            p.dettchView();
        }
        if (toast != null) {
            toast.cancel();
        }
    }

    @Override
    public void showToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    @Override
    public void showLoading(LoadingIml loadingIml) {
        if (loadingIml == null) {
            loadingIml = new DeflateLoading(this);
        }
        this.loadingIml = loadingIml;
        loadingIml.show();
    }

    @Override
    public void hideLoading() {
        if (this.loadingIml != null) {
            this.loadingIml.hide();
        }
    }

}
