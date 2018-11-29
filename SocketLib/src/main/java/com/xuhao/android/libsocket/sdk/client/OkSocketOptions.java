package com.xuhao.android.libsocket.sdk.client;

import com.xuhao.android.common.basic.protocol.DefaultNormalReaderProtocol;
import com.xuhao.android.common.interfacies.IIOCoreOptions;
import com.xuhao.android.common.interfacies.IReaderProtocol;
import com.xuhao.android.libsocket.sdk.client.connection.AbsReconnectionManager;
import com.xuhao.android.libsocket.sdk.client.connection.DefaultReconnectManager;
import com.xuhao.android.libsocket.sdk.client.connection.NoneReconnect;
import com.xuhao.android.libsocket.sdk.client.connection.abilities.IConfiguration;

import java.nio.ByteOrder;

/**
 * OkSocket参数配置类<br>
 * Created by xuhao on 2017/5/16.
 */
public class OkSocketOptions implements IIOCoreOptions {
    /**
     * 框架是否是调试模式
     */
    private static boolean isDebug;
    /**
     * Socket通讯模式
     * <p>
     * 请注意:<br>
     * 阻塞式仅支持冷切换(断开后切换)<br>
     * 非阻塞式可以热切换<br>
     * </p>
     */
    private IOThreadMode mIOThreadMode;
    /**
     * 连接是否管理保存<br>
     * <p>
     * true:连接将会保存在管理器中,进行性能优化和断线重连<br>
     * false:不会保存在管理器中,对于已经保存的会进行删除,将不进行性能优化和断线重连.
     * </p>
     */
    private boolean isConnectionHolden;
    /**
     * 写入Socket管道中给服务器的字节序
     */
    private ByteOrder mWriteOrder;
    /**
     * 从Socket管道中读取字节序时的字节序
     */
    private ByteOrder mReadByteOrder;
    /**
     * Socket通讯中,业务层定义的数据包包头格式
     */
    private IReaderProtocol mReaderProtocol;
    /**
     * 发送给服务器时单个数据包的总长度
     */
    private int mWritePackageBytes;
    /**
     * 从服务器读取时单次读取的缓存字节长度,数值越大,读取效率越高.但是相应的系统消耗将越大
     */
    private int mReadPackageBytes;
    /**
     * 脉搏频率单位是毫秒
     */
    private long mPulseFrequency;
    /**
     * 脉搏丢失次数<br>
     * 大于或等于丢失次数时将断开该通道的连接<br>
     * 抛出{@link com.xuhao.android.libsocket.impl.exceptions.DogDeadException}
     */
    private int mPulseFeedLoseTimes;
    /**
     * 连接超时时间(秒)
     */
    private int mConnectTimeoutSecond;
    /**
     * 最大读取数据的兆数(MB)<br>
     * 防止服务器返回数据体过大的数据导致前端内存溢出.
     */
    private int mMaxReadDataMB;
    /**
     * 重新连接管理器
     */
    private AbsReconnectionManager mReconnectionManager;
    /**
     * 安全套接字层配置
     */
    private OkSocketSSLConfig mSSLConfig;
    /**
     * 套接字工厂
     */
    private OkSocketFactory mOkSocketFactory;
    /**
     * 从线程进行回调.
     */
    private boolean isCallbackInThread;

    private OkSocketOptions() {
    }

    public static void setIsDebug(boolean isDebug) {
        OkSocketOptions.isDebug = isDebug;
    }

    public static class Builder {
        private OkSocketOptions mOptions;

        public Builder() {
            this(OkSocketOptions.getDefault());
        }

        public Builder(IConfiguration configuration) {
            this(configuration.getOption());
        }

        public Builder(OkSocketOptions okOptions) {
            mOptions = new OkSocketOptions();
            mOptions.mIOThreadMode = okOptions.mIOThreadMode;
            mOptions.mPulseFrequency = okOptions.mPulseFrequency;
            mOptions.mMaxReadDataMB = okOptions.mMaxReadDataMB;
            mOptions.mReaderProtocol = okOptions.mReaderProtocol;
            mOptions.mConnectTimeoutSecond = okOptions.mConnectTimeoutSecond;
            mOptions.mWritePackageBytes = okOptions.mWritePackageBytes;
            mOptions.mReadPackageBytes = okOptions.mReadPackageBytes;
            mOptions.mWriteOrder = okOptions.mWriteOrder;
            mOptions.mReadByteOrder = okOptions.mReadByteOrder;
            mOptions.isConnectionHolden = okOptions.isConnectionHolden;
            mOptions.mPulseFeedLoseTimes = okOptions.mPulseFeedLoseTimes;
            mOptions.mReconnectionManager = okOptions.mReconnectionManager;
            mOptions.mSSLConfig = okOptions.mSSLConfig;
            mOptions.mOkSocketFactory = okOptions.mOkSocketFactory;
            mOptions.isCallbackInThread = okOptions.isCallbackInThread;
        }

        /**
         * Socket通讯模式
         * <p>
         * 请注意:<br>
         * 阻塞式仅支持冷切换(断开后切换)<br>
         * 非阻塞式可以热切换<br>
         * </p>
         *
         * @param IOThreadMode {@link IOThreadMode}
         */
        public Builder setIOThreadMode(IOThreadMode IOThreadMode) {
            mOptions.mIOThreadMode = IOThreadMode;
            return this;
        }

        /**
         * 最大读取数据的兆数(MB)<br>
         * 防止服务器返回数据体过大的数据导致前端内存溢出<br>
         *
         * @param maxReadDataMB 兆字节为单位
         */
        public Builder setMaxReadDataMB(int maxReadDataMB) {
            mOptions.mMaxReadDataMB = maxReadDataMB;
            return this;
        }

        /**
         * 安全套接字层配置<br>
         *
         * @param SSLConfig {@link OkSocketSSLConfig}
         */
        public Builder setSSLConfig(OkSocketSSLConfig SSLConfig) {
            mOptions.mSSLConfig = SSLConfig;
            return this;
        }

        /**
         * Socket通讯中,业务层定义的数据包包头格式<br>
         * 默认的为{@link DefaultNormalReaderProtocol}<br>
         *
         * @param readerProtocol {@link IReaderProtocol} 通讯头协议
         */
        public Builder setReaderProtocol(IReaderProtocol readerProtocol) {
            mOptions.mReaderProtocol = readerProtocol;
            return this;
        }

        /**
         * 设置脉搏间隔频率<br>
         * 单位是毫秒<br>
         *
         * @param pulseFrequency 间隔毫秒数
         */

        public Builder setPulseFrequency(long pulseFrequency) {
            mOptions.mPulseFrequency = pulseFrequency;
            return this;
        }

        /**
         * 连接是否管理保存<br>
         * <p>
         * true:连接将会保存在管理器中,进行性能优化和断线重连<br>
         * false:不会保存在管理器中,对于已经保存的会进行删除,将不进行性能优化和断线重连.
         * </p>
         * 默认是 true
         *
         * @param connectionHolden true 讲此次链接交由OkSocket进行缓存管理,false 则不进行缓存管理.
         */
        public Builder setConnectionHolden(boolean connectionHolden) {
            mOptions.isConnectionHolden = connectionHolden;
            return this;
        }

        /**
         * 脉搏丢失次数<br>
         * 大于或等于丢失次数时将断开该通道的连接<br>
         * 抛出{@link com.xuhao.android.libsocket.impl.exceptions.DogDeadException}<br>
         * 默认是5次
         *
         * @param pulseFeedLoseTimes 丢失心跳ACK的次数,例如5,当丢失3次时,自动断开.
         */
        public Builder setPulseFeedLoseTimes(int pulseFeedLoseTimes) {
            mOptions.mPulseFeedLoseTimes = pulseFeedLoseTimes;
            return this;
        }

        /**
         * 设置输出Socket管道中给服务器的字节序<br>
         * 默认是:大端字节序<br>
         *
         * @param writeOrder {@link ByteOrder} 字节序
         * @deprecated 请使用 {@link Builder#setWriteByteOrder(ByteOrder)}
         */
        public Builder setWriteOrder(ByteOrder writeOrder) {
            setWriteByteOrder(writeOrder);
            return this;
        }


        /**
         * 设置输出Socket管道中给服务器的字节序<br>
         * 默认是:大端字节序<br>
         *
         * @param writeOrder {@link ByteOrder} 字节序
         */
        public Builder setWriteByteOrder(ByteOrder writeOrder) {
            mOptions.mWriteOrder = writeOrder;
            return this;
        }

        /**
         * 设置输入Socket管道中读取时的字节序<br>
         * 默认是:大端字节序<br>
         *
         * @param readByteOrder {@link ByteOrder} 字节序
         */
        public Builder setReadByteOrder(ByteOrder readByteOrder) {
            mOptions.mReadByteOrder = readByteOrder;
            return this;
        }

        /**
         * 发送给服务器时单个数据包的总长度
         *
         * @param writePackageBytes 单个数据包的总大小
         */
        public Builder setWritePackageBytes(int writePackageBytes) {
            mOptions.mWritePackageBytes = writePackageBytes;
            return this;
        }

        /**
         * 从服务器读取时单个数据包的总长度
         *
         * @param readPackageBytes 单个数据包的总大小
         */
        public Builder setReadPackageBytes(int readPackageBytes) {
            mOptions.mReadPackageBytes = readPackageBytes;
            return this;
        }

        /**
         * 设置连接超时时间,该超时时间是链路上从开始连接到连接上的时间
         *
         * @param connectTimeoutSecond 超时秒数,注意单位是秒
         * @return
         */
        public Builder setConnectTimeoutSecond(int connectTimeoutSecond) {
            mOptions.mConnectTimeoutSecond = connectTimeoutSecond;
            return this;
        }

        /**
         * 设置断线重连的连接管理器<br>
         * 默认的连接管理器为{@link DefaultReconnectManager}<br>
         * 如果不需要断线重连请设置该参数为{@link NoneReconnect}
         *
         * @param reconnectionManager 断线重连管理器{@link AbsReconnectionManager}
         * @return
         */
        public Builder setReconnectionManager(
                AbsReconnectionManager reconnectionManager) {
            mOptions.mReconnectionManager = reconnectionManager;
            return this;
        }

        /**
         * 设置Socket工厂类,用于提供一个可以连接的Socket.
         * 可以是加密Socket,也可以是未加密的socket.
         *
         * @param factory socket工厂方法
         * @return
         */
        public Builder setSocketFactory(OkSocketFactory factory) {
            mOptions.mOkSocketFactory = factory;
            return this;
        }

        /**
         * 设置回调在线程中,不是在UI线程中.
         *
         * @param inThread true表示回调在非UI线程中,false表示回调在UI线程中
         * @return
         */
        public Builder setCallbackInThread(boolean inThread) {
            mOptions.isCallbackInThread = inThread;
            return this;
        }


        public OkSocketOptions build() {
            return mOptions;
        }
    }

    public IOThreadMode getIOThreadMode() {
        return mIOThreadMode;
    }

    public long getPulseFrequency() {
        return mPulseFrequency;
    }

    public OkSocketSSLConfig getSSLConfig() {
        return mSSLConfig;
    }

    public OkSocketFactory getOkSocketFactory() {
        return mOkSocketFactory;
    }

    public int getConnectTimeoutSecond() {
        return mConnectTimeoutSecond;
    }

    public boolean isConnectionHolden() {
        return isConnectionHolden;
    }

    public int getPulseFeedLoseTimes() {
        return mPulseFeedLoseTimes;
    }

    public AbsReconnectionManager getReconnectionManager() {
        return mReconnectionManager;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    @Override
    public int getWritePackageBytes() {
        return mWritePackageBytes;
    }

    @Override
    public int getReadPackageBytes() {
        return mReadPackageBytes;
    }

    @Override
    public ByteOrder getWriteByteOrder() {
        return mWriteOrder;
    }

    @Override
    public IReaderProtocol getReaderProtocol() {
        return mReaderProtocol;
    }

    @Override
    public int getMaxReadDataMB() {
        return mMaxReadDataMB;
    }

    @Override
    public ByteOrder getReadByteOrder() {
        return mReadByteOrder;
    }

    public boolean isCallbackInThread() {
        return isCallbackInThread;
    }

    public static OkSocketOptions getDefault() {
        OkSocketOptions okOptions = new OkSocketOptions();
        okOptions.mPulseFrequency = 5 * 1000;
        okOptions.mIOThreadMode = IOThreadMode.DUPLEX;
        okOptions.mReaderProtocol = new DefaultNormalReaderProtocol();
        okOptions.mMaxReadDataMB = 5;
        okOptions.mConnectTimeoutSecond = 3;
        okOptions.mWritePackageBytes = 100;
        okOptions.mReadPackageBytes = 50;
        okOptions.mReadByteOrder = ByteOrder.BIG_ENDIAN;
        okOptions.mWriteOrder = ByteOrder.BIG_ENDIAN;
        okOptions.isConnectionHolden = true;
        okOptions.mPulseFeedLoseTimes = 5;
        okOptions.mReconnectionManager = new DefaultReconnectManager();
        okOptions.mSSLConfig = null;
        okOptions.mOkSocketFactory = null;
        okOptions.isCallbackInThread = false;
        return okOptions;
    }

    /**
     * 线程模式
     */
    public enum IOThreadMode {
        /**
         * 单工通讯
         */
        SIMPLEX,
        /**
         * 双工通讯
         */
        DUPLEX;
    }
}