package com.ldx.baseutils.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ldx.baseutils.R;
import com.ldx.baseutils.mvp.base.BaseActivity;
import com.ldx.baseutils.mvp.base.BasePresenter;
import com.ldx.baseutils.server.MQTTMessage;
import com.ldx.baseutils.server.MQTTService;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author babieta
 * @date 2019/1/8
 */

public class MqttActivity extends BaseActivity {

    @Override
    protected BasePresenter createP() {
        return null;
    }

    @NonNull
    @Override
    public int setView() {
        EventBus.getDefault().register(this);
        return R.layout.ac_mqtt;
    }

    @Override
    protected void initView() {
        startService(new Intent(this, MQTTService.class));
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MQTTService.publish("你好 mqtt");
            }
        });



    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMqttMessage(MQTTMessage mqttMessage){
        Log.i(MQTTService.TAG,"get message:"+mqttMessage.getMessage());
        Toast.makeText(this,mqttMessage.getMessage(),Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void loadDate() {

    }
}
