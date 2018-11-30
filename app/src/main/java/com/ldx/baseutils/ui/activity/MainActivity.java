package com.ldx.baseutils.ui.activity;

import android.app.AlertDialog;
import android.support.annotation.NonNull;

import com.ldx.baseutils.demo.ConcreteBuilder;
import com.ldx.baseutils.demo.Director;
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
        ConcreteBuilder concreteBuilder = new ConcreteBuilder();
        Director director = new Director(concreteBuilder);
        director.Construct("i7-6700", "三星DDR4", "希捷1T");




    }

    @Override
    protected void loadDate() {

    }
}
