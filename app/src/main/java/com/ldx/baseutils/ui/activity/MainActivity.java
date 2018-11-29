package com.ldx.baseutils.ui.activity;

import android.support.annotation.NonNull;

import com.ldx.baseutils.mvp.base.BaseActivity;
import com.ldx.baseutils.mvp.base.BasePresenter;
import com.ldx.baseutils.R;

/**
 * @author babieta
 */
public class MainActivity extends BaseActivity {


    @Override
    protected BasePresenter createP() {
        return null;
    }

    @NonNull
    @Override
    public int setView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void loadDate() {

    }
}
