package com.ldx.baseutils.mvp.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ldx.baseutils.utils.DeflateLoading;


/**
 * Created by Suning on 2018/4/9.
 */

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseView {
    private P p;
    protected Context context;
    private LoadingIml loadingIml;
    private Toast toast;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    public P getP() {
        if (p == null) {
            p = createP();
        }
        return p;
    }

    protected abstract P createP();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (p == null) {
            p = createP();

        }
        if (p != null) {

            p.attchView(this);
        }
    }

    private View Contentvew;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(setView(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Contentvew = view;
        initView();
    }

    public View findviewById(@IdRes int id) {
        if (Contentvew != null) {
            return Contentvew.findViewById(id);
        }
        return null;
    }

    protected abstract int setView();

    protected abstract void initView();

    @Override
    public void onDestroy() {
        super.onDestroy();
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
            toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    @Override
    public void showLoading(LoadingIml loadingIml) {
        if (loadingIml == null) {
            loadingIml = new DeflateLoading(context);
        }
        this.loadingIml = loadingIml;
        this.loadingIml.show();
    }

    @Override
    public void hideLoading() {
        if (this.loadingIml != null) {
            this.loadingIml.hide();
        }
    }
}
