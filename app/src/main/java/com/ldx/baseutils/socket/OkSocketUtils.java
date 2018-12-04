package com.ldx.baseutils.socket;


import android.content.Context;

import com.ldx.baseutils.utils.LogUtils;
import com.ldx.baseutils.utils.StringUtils;
import com.xuhao.android.common.basic.bean.OriginalData;
import com.xuhao.android.common.interfacies.client.msg.ISendable;
import com.xuhao.android.libsocket.impl.client.PulseManager;
import com.xuhao.android.libsocket.sdk.OkSocket;
import com.xuhao.android.libsocket.sdk.client.ConnectionInfo;
import com.xuhao.android.libsocket.sdk.client.OkSocketOptions;
import com.xuhao.android.libsocket.sdk.client.action.SocketActionAdapter;
import com.xuhao.android.libsocket.sdk.client.bean.IPulseSendable;
import com.xuhao.android.libsocket.sdk.client.connection.IConnectionManager;

import java.lang.reflect.Field;
import java.nio.charset.Charset;


/**
 * @author babieta
 * @date 2018/11/1
 */

public class OkSocketUtils {
    private static boolean isConnect = false;
    private static String mContent = "";
    private static boolean isBreak = false;
    private static String strJson = "";
    private static String sendJson = "";
    private static ConnectionInfo info;
    private static IConnectionManager option;

    private OkSocketUtils() {
    }

    private static OkSocketUtils mSingleMode = null;

    public static OkSocketUtils getInstance() {
        if (mSingleMode == null) {
            synchronized (OkSocketUtils.class) {
                if (mSingleMode == null) {
                    mSingleMode = new OkSocketUtils();
                }
            }
        }
        return mSingleMode;
    }

    /**
     * 连接socket
     */
    public static void initSocket() {
        if (mSingleMode == null) {
            throw new RuntimeException(" 请初始化 getInstance  ");
        }
        LogUtils.e("  --->  创建连接  ");
        if (option == null) {
//            info = new ConnectionInfo(UriApi.SOCKET_URI, UriApi.PORT_NUMBER);
            //调用OkSocket,开启这次连接的通道,调用通道的连接方法进行连接.
            OkSocketOptions.Builder builder = new OkSocketOptions.Builder();
            builder.setPulseFrequency(18000);
            builder.setWritePackageBytes(1024);
            builder.setReconnectionManager(new DefaultReconnectManager());

//            final Handler handler = new Handler(Looper.getMainLooper());
//            builder.setCallbackThreadModeToken(new OkSocketOptions.ThreadModeToken() {
//                @Override
//                public void handleCallbackEvent(ActionDispatcher.ActionRunnable runnable) {
//                    handler.post(runnable);
//                }
//            });
            option = OkSocket.open(info).option(builder.build());

            option.registerReceiver(adapter);
            option.connect();
        }
    }


    /**
     * 发送json
     *
     * @param json
     */
    public static void send(String json) {
        if (option != null) {
            if (option.isConnect()) {
                if (option != null) {
                    strJson = json;
                    sendJson = json;
                    option.send(new TestSendData(json));
                }
            }
        } else {
            strJson = json;
            sendJson = json;
            initSocket();
        }
    }

    /**
     * 发送心跳
     *
     * @param json
     */
    public static void sendHeartbeat(String json) {
        if (option != null) {
            if (isIsConnect()) {
                PulseManager pulseManager = option.getPulseManager();
                Class<PulseManager> aClass = (Class<PulseManager>) pulseManager.getClass();
                try {
                    try {
                        //
                        Field isDid = aClass.getDeclaredField("isDead");
                        isDid.setAccessible(true);
                        boolean isDid1 = isDid.getBoolean(pulseManager);
                        isDid.setBoolean(pulseManager, false);

                        Field mLoseTimes = aClass.getDeclaredField("mLoseTimes");
                        mLoseTimes.setAccessible(true);
                        int anInt = mLoseTimes.getInt(pulseManager);
                        mLoseTimes.setInt(pulseManager, -1);

                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                pulseManager.setPulseSendable(new PulseBean(json)).pulse();
            }
        } else {
            initSocket();
        }
    }

    /**
     * 停止心跳
     */
    public static void stopHeartbeat() {
        if (option != null) {
            if (isConnect) {
                if (option != null) {
                    strJson = "";
                    sendJson = "";
                    option.getPulseManager().dead();
                }
            }
        }
    }

    public static void setString(String s) {
        strJson = s;
        sendJson = s;
    }

    /**
     * 是否连接
     *
     * @return
     */
    public static boolean isIsConnect() {
        if (option != null) {
            return option.isConnect();
        }
        return false;
    }

    /**
     * 停止socket
     */
    public static void stopSocket() {
        if (option != null) {
            option.disconnect();
            option.unRegisterReceiver(adapter);
            strJson = "";
            sendJson = "";
            option = null;
            LogUtils.e("  --->  销毁连接  ");
        }
    }


    private static SocketActionAdapter adapter = new SocketActionAdapter() {
        @Override
        public void onSocketIOThreadStart(Context context, String action) {
            super.onSocketIOThreadStart(context, action);
            LogUtils.d("onSocketIOThreadStart: " + action);
            isConnect = true;
        }

        @Override
        public void onSocketIOThreadShutdown(Context context, String action, Exception e) {
            super.onSocketIOThreadShutdown(context, action, e);
            isConnect = false;
        }

        @Override
        public void onSocketConnectionSuccess(Context context, ConnectionInfo info, String action) {
            super.onSocketConnectionSuccess(context, info, action);
            isConnect = true;
            if (baseOkSocketInterface != null) {
                baseOkSocketInterface.onConnectionSuccess();
            }
            LogUtils.d("连接成功: " + action);
            LogUtils.d("开始心跳: " + action);
            LogUtils.d("是否发送心跳  " + isBreak);
            if (isBreak) {
                if (option != null) {
                    if (!StringUtils.isEmpty(strJson)) {
                        sendHeartbeat(strJson);
                        option.getPulseManager().trigger();
                    }
                }
            } else {
                LogUtils.e(" send Json   " + sendJson);
                if (!StringUtils.isEmpty(sendJson)) {
                    send(sendJson);
                    sendHeartbeat(sendJson);
                }
            }


        }

        /**
         * Socket通讯从服务器读取到消息后的响应<br>
         * @param info
         * @param action  {@link.connection.interfacies.IAction#ACTION_READ_COMPLETE}
         * @param data    原始的读取到的数据{@link OriginalData}
         */

        @Override
        public void onSocketReadResponse(Context context, ConnectionInfo info, String action, OriginalData data) {
            super.onSocketReadResponse(context, info, action, data);
            mContent = new String(data.getBodyBytes(), Charset.forName("utf-8"));
            LogUtils.d("获取服务器读取的数据: " + mContent);
            if (mContent != null && !mContent.equals("")) {
                if (baseOkSocketInterface != null) {
                    baseOkSocketInterface.onMessage(mContent);
                }
                if (option != null) {
                    if (option.getPulseManager() != null) {
                        option.getPulseManager().feed();
                    }
                } else {
                    if (baseOkSocketInterface != null) {
                        baseOkSocketInterface.onMessage("");
                    }
                }

            }
        }

        @Override
        public void onSocketConnectionFailed(Context context, ConnectionInfo info, String action, Exception e) {
            super.onSocketConnectionFailed(context, info, action, e);
            LogUtils.d("onSocketConnectionFailed: " + "连接失败" + e.getMessage());
            if (baseOkSocketInterface != null) {
                baseOkSocketInterface.onDisconnect();
            }
            isConnect = false;
        }

        /**
         *  Socket断开后进行的回调<br>
         * 当Socket彻底断开后,系统会回调该方法<br>
         * @param info    这次连接的连接信息
         * @param action  {@link.connection.interfacies.IAction#ACTION_DISCONNECTION}
         * @param e       Socket断开时的异常信息,如果是正常断开(调用disconnect()),异常信息将为null.使用e变量时应该进行判空操作
         */
        @Override
        public void onSocketDisconnection(Context context, ConnectionInfo info, String
                action, Exception e) {
            if (e != null) {
                if (e instanceof RuntimeException) {
                    LogUtils.d("onSocketDisconnection:正在重定向连接... ");
                    if (option != null) {
                        option.switchConnectionInfo(info);
                        option.connect();
                    }
                    isBreak = true;
                } else {
                    isConnect = true;
                    LogUtils.d("onSocketDisconnection:异常断开:" + e.getMessage());
                    if (baseOkSocketInterface != null) {
                        baseOkSocketInterface.onError(e);
                        baseOkSocketInterface.onDisconnect();
                    }
                }
            } else {
                LogUtils.d("onSocketDiscondnection: 正常断开");
                isConnect = false;
                isBreak = false;
            }
        }

        @Override
        public void onSocketWriteResponse(Context context, ConnectionInfo info, String
                action, ISendable data) {
            super.onSocketWriteResponse(context, info, action, data);
            LogUtils.d("onSocketWriteResponse: " + new String(data.parse(), Charset.forName("utf-8")));
        }

        @Override
        public void onPulseSend(Context context, ConnectionInfo info, IPulseSendable data) {
            super.onPulseSend(context, info, data);
            LogUtils.d("onPulseSend: " + new String(data.parse(), Charset.forName("utf-8")));
            if (option != null) {
                option.getPulseManager().feed();
            }

        }
    };
    private static BaseOkSocketInterface baseOkSocketInterface;

    public static void setOkSocketInterface(BaseOkSocketInterface socketInterface) {
        baseOkSocketInterface = socketInterface;
    }
}

