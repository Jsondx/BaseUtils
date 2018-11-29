package com.ldx.baseutils.socket;




import com.xuhao.android.common.interfacies.client.msg.ISendable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * @author babieta
 * @date 2018/7/26
 */

public class TestSendData implements ISendable {
    private String str = "";

    public TestSendData(String json) {
        str = json;
    }
    @Override
    public byte[] parse() {
        //根据服务器的解析规则,构建byte数组
        byte[] body = str.getBytes(Charset.defaultCharset());
        ByteBuffer bb = ByteBuffer.allocate(4 + body.length);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.putInt(body.length);
        bb.put(body);
        return bb.array();
    }
}
