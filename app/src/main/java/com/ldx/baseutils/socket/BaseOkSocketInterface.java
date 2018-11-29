package com.ldx.baseutils.socket;

/**
 * @author babieta
 * @date 2018/7/28
 */

public interface BaseOkSocketInterface {
    /**
     * 连接成功
     */
    void onConnectionSuccess();

    /**
     * 返回服务的数据
     *
     * @param json
     */
    void onMessage(String json);

    /**
     * 定义错误回调
     *
     * @param e
     */
    void onError(Exception e);

    /**
     * 连接断开
     */
    void onDisconnect();
}
