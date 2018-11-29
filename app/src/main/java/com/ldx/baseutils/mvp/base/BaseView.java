package com.ldx.baseutils.mvp.base;

/**
 * View的接口规范，所有的View都应该继承该借口
 *
 * @author Suning
 * @date 2018/4/9
 */

public interface BaseView {

    void  showToast(String msg);

    void  showLoading(LoadingIml loadingIml);

    void  hideLoading();
}
