package com.ldx.baseutils.utils;


import android.content.Context;

import com.ldx.baseutils.mvp.base.LoadingIml;
import com.ldx.baseutils.ui.dialog.LoaddingDialog;


/**
 * @author Administrator
 * @date 2018/6/7 0007
 */

public class DeflateLoading implements LoadingIml {

    private final LoaddingDialog alertDialog;

    public DeflateLoading(Context context) {
        alertDialog = new LoaddingDialog(context);
    }

    @Override
    public void show() {
        alertDialog.show();
    }

    @Override
    public void hide() {
        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
        }

    }

    @Override
    public void setmessage(String msg) {

    }
}
