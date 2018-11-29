package com.ldx.baseutils.socket;



import com.xuhao.android.libsocket.sdk.client.bean.IPulseSendable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class PulseBean implements IPulseSendable {
    private String str = "";

    public PulseBean(String json) {
        str = json;
    }
    @Override
    public byte[] parse() {
        byte[] body = str.getBytes(Charset.defaultCharset());
        ByteBuffer bb = ByteBuffer.allocate(4 + body.length);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.putInt(body.length);
        bb.put(body);
        return bb.array();
    }
}