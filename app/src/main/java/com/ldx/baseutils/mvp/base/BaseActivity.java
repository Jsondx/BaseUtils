package com.ldx.baseutils.mvp.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ldx.baseutils.callBack.PermissionListener;
import com.ldx.baseutils.utils.activity.ActivityManger;
import com.ldx.baseutils.utils.activity.ActivityUtils;
import com.ldx.baseutils.utils.DeflateLoading;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Suning
 * @date 2018/4/9
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {
    private P p;
    private LoadingIml loadingIml;
    private Toast toast;
    private static PermissionListener mPermissionListener;
    private static final int CODE_REQUEST_PERMISSION = 1;

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

    protected void intentActivity(Activity activity, Class clazz, Bundle bundle) {
        Intent intent = new Intent(activity, clazz);
        if (bundle == null) {
            activity.startActivity(intent);
        } else {
            intent.putExtras(bundle);
            activity.startActivity(intent);
        }
    }

    protected void intentActivity(Activity activity, Class clazz, Bundle bundle, boolean Isfinsh) {
        Intent intent = new Intent(activity, clazz);
        if (bundle == null) {
            activity.startActivity(intent);
        } else {
            intent.putExtras(bundle);
            activity.startActivity(intent);
        }
        if (Isfinsh) {
            activity.finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void intentForResult(Activity activity, Class clazz, Bundle bundle, int requestCode) {
        Intent intent = new Intent(activity, clazz);
        if (null == bundle) {
            activity.startActivityForResult(intent, requestCode, bundle);
        } else {
            intent.putExtras(bundle);
            activity.startActivityForResult(intent, requestCode, bundle);
        }
    }

    /**
     * 申请权限
     *
     * @param permissions 需要申请的权限(数组)
     * @param listener    权限回调接口
     */
    public static void requestPermissions(String[] permissions, PermissionListener listener) {
        Activity activity = ActivityUtils.getTopActivity();
        if (null == activity) {
            return;
        }

        mPermissionListener = listener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            //权限没有授权
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionList.toArray(new String[permissionList.size()]), CODE_REQUEST_PERMISSION);
        } else {
            mPermissionListener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CODE_REQUEST_PERMISSION:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int result = grantResults[i];
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            String permission = permissions[i];
                            deniedPermissions.add(permission);
                        }
                    }
                    if (deniedPermissions.isEmpty()) {
                        mPermissionListener.onGranted();
                    } else {
                        mPermissionListener.onDenied(deniedPermissions);
                    }
                }
                break;

            default:
                break;
        }
    }
}
