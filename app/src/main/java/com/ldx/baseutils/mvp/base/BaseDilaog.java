package com.ldx.baseutils.mvp.base;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.ldx.baseutils.R;


/**
 *
 * @author Administrator
 * @date 2018/4/23 0023
 */

public abstract class BaseDilaog extends Dialog {
    private Context context;

    public BaseDilaog(@NonNull Context context) {
        super(context, R.style.mydailaog);
        this.context = context;
        View getview = getView();
        setContentView(getview);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = context.getResources().getDisplayMetrics().widthPixels;
        attributes.gravity = setGravity() <= 0 ? Gravity.CENTER : setGravity();
        attributes.dimAmount = 0.5f;
        getWindow().setAttributes(attributes);
        init(getview);
    }
    public abstract View getView();

    public abstract int setGravity();

    public abstract void init(View view);
}
