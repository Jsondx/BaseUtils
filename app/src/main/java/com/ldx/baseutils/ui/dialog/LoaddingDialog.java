package com.ldx.baseutils.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.ldx.baseutils.R;
import com.ldx.baseutils.mvp.base.BaseDilaog;


/**
 *
 * @author Administrator
 * @date 2018/6/15 0015
 */

public class LoaddingDialog extends BaseDilaog {

    private View viewById;
    private RotateAnimation rotateAnimation;

    public LoaddingDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int setGravity() {
        return 0;
    }

    @Override
    public View getView() {
        return getLayoutInflater().inflate(R.layout.dialog_loadding, null, false);
    }
    @Override
    public void init(View view) {
        setCanceledOnTouchOutside(false);
        viewById = view.findViewById(R.id.view_ring);
        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setInterpolator(new LinearInterpolator());

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        viewById.startAnimation(rotateAnimation);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        rotateAnimation.cancel();

    }
}
