package com.xuhao.android.libsocket.impl.client;

import android.content.BroadcastReceiver;
import android.content.Context;

import com.xuhao.android.libsocket.impl.client.abilities.IConnectionSwitchListener;
import com.xuhao.android.libsocket.impl.client.action.ActionDispatcher;
import com.xuhao.android.libsocket.sdk.client.ConnectionInfo;
import com.xuhao.android.libsocket.sdk.client.connection.IConnectionManager;
import com.xuhao.android.libsocket.sdk.client.action.ISocketActionListener;

import java.io.Serializable;


/**
 * Created by xuhao on 2017/5/17.
 */

public abstract class AbsConnectionManager implements IConnectionManager {
    /**
     * 上下文
     */
    protected Context mContext;
    /**
     * 连接信息
     */
    protected ConnectionInfo mConnectionInfo;
    /**
     * 连接信息switch监听器
     */
    private IConnectionSwitchListener mConnectionSwitchListener;
    /**
     * 状态机
     */
    protected ActionDispatcher mActionDispatcher;

    public AbsConnectionManager(Context context, ConnectionInfo info) {
        mContext = context;
        mConnectionInfo = info;
        mActionDispatcher = new ActionDispatcher(mContext, info, this);
    }

    public IConnectionManager registerReceiver(final ISocketActionListener socketResponseHandler) {
        mActionDispatcher.registerReceiver(socketResponseHandler);
        return this;
    }
    
    public IConnectionManager unRegisterReceiver(ISocketActionListener socketResponseHandler) {
        mActionDispatcher.unRegisterReceiver(socketResponseHandler);
        return this;
    }

    protected void sendBroadcast(String action, Serializable serializable) {
        mActionDispatcher.sendBroadcast(action, serializable);
    }

    protected void sendBroadcast(String action) {
        mActionDispatcher.sendBroadcast(action);
    }

    @Override
    public ConnectionInfo getConnectionInfo() {
        if (mConnectionInfo != null) {
            return mConnectionInfo.clone();
        }
        return null;
    }

    @Override
    public void switchConnectionInfo(ConnectionInfo info) {
        if (info != null) {
            ConnectionInfo tempOldInfo = mConnectionInfo;
            mConnectionInfo = info.clone();
            if (mActionDispatcher != null) {
                mActionDispatcher.setConnectionInfo(mConnectionInfo);
            }
            if (mConnectionSwitchListener != null) {
                mConnectionSwitchListener.onSwitchConnectionInfo(this, tempOldInfo, mConnectionInfo);
            }
        }
    }

    protected void setOnConnectionSwitchListener(IConnectionSwitchListener listener) {
        mConnectionSwitchListener = listener;
    }
}
