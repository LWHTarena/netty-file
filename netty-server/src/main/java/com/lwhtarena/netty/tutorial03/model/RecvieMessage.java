package com.lwhtarena.netty.tutorial03.model;

import java.io.Serializable;

/**
 * @author： liwh
 * @Date: 2016/11/17.
 * @Description：
 */
public class RecvieMessage implements Serializable {

    private short msgType;

    private String data;

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
