package com.ldx.baseutils.callBack;

import java.util.List;

/**
 * 权限回调接口
 * @author babieta
 */

public interface PermissionListener {

    void onGranted();

    void onDenied(List<String> deniedPermissions);
}
