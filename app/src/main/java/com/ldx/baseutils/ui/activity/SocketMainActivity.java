package com.ldx.baseutils.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ldx.baseutils.R;
import com.ldx.baseutils.mvp.base.BaseActivity;
import com.ldx.baseutils.mvp.base.BasePresenter;
import com.ldx.baseutils.utils.LogUtils;
import com.ldx.baseutils.utils.StringUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by babieta on 2018/12/25.
 */

public class SocketMainActivity extends BaseActivity {

    private Socket socket;
    private Button btnLink;
    private Button btnDis;
    private EditText text;
    private Button btnSend;
    private BufferedReader in;
    private boolean isRun = false;
    private TextView tvContent;

    @Override
    protected BasePresenter createP() {
        return null;
    }

    @NonNull
    @Override
    public int setView() {
        return R.layout.ac_scoket;
    }

    @Override
    protected void initView() {
        btnLink = findViewById(R.id.btn_link);
        btnDis = findViewById(R.id.btn_dis);
        text = findViewById(R.id.tv_text);
        btnSend = findViewById(R.id.btn_send);
        tvContent = findViewById(R.id.tv_content);
    }

    @Override
    protected void loadDate() {
        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLink();
            }
        });
        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDisconnect();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSend();
            }
        });

    }

    public void onLink() {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    socket = new Socket("192.168.0.144", 8888);
                    boolean connected = socket.isConnected();
                    if (connected) {
                        LogUtils.e("连接服务器成功");
                        isRun = true;
                        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        onRun();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
        t.start();


    }

    public void onDisconnect() {
        if (socket != null) {
            try {
                socket.close();
                boolean closed = socket.isClosed();
                if (closed) {
                    LogUtils.e("断开服务器");
                    isRun = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onSend() {
        Thread t = new Thread() {
            @Override
            public void run() {
                String trim = text.getText().toString().trim();
                if (!StringUtils.isEmpty(trim)) {
                    try {
                        //报文发送
                        String socketData = "[" + trim + "]";
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        writer.write(trim.replace("\n", " ") + "\n");
                        writer.flush();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    showToast("内容不能为空");
                }

            }
        };
        t.start();


    }

    /**
     * 读取服务器发来的信息，并通过Handler发给UI线程
     * //死循环守护，监控服务器发来的消息
     * //如果服务器没有关闭
     * //连接正常
     * //如果输入流没有断开
     * //读取接收的信息
     * //通知UI更新
     */
    public void onRun() {
        try {
            while (isRun) {
                if (!socket.isClosed()) {
                    if (socket.isConnected()) {
                        if (!socket.isInputShutdown()) {
                            String getLine;
                            if ((getLine = in.readLine()) != null) {
                                LogUtils.e("---> in line "+in.readLine());
                                getLine += "\n";
                                Message message = new Message();
                                message.obj = getLine;
                                mHandler.sendMessage(message);
                            } else {

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //接收线程发送过来信息，并用TextView追加显示
    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tvContent.append((CharSequence) msg.obj);
        }
    };
}
