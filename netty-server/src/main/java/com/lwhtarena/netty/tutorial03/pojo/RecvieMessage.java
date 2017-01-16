package com.lwhtarena.netty.tutorial03.pojo;

import java.io.Serializable;

/**
 * @author： liwh
 * @Date: 2017/1/16.
 * @Description：<p>响应信息</P>
 */
public class RecvieMessage implements Serializable {

    private short msgType; //响应信息类型

    private String data; //信息

    public short getMsgType() {
        return msgType;
    }

    public void setMsgType(short msgType) {
        this.msgType = msgType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
