package com.lwhtarena.netty.tutorial05.module;

import com.lwhtarena.netty.tutorial05.module.enums.MsgType;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 */
public abstract class BaseMsg  implements Serializable {
	private static final long serialVersionUID = 7884485597941697632L;
	private MsgType type;
    //必须唯一，否者会出现channel调用混乱
    private String clientId;
    private long dateTime;
    //初始化客户端id
    public BaseMsg(String clientId) {
        this.clientId = clientId;
        this.dateTime = new Date().getTime();
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public MsgType getType() {
        return type;
    }

    public void setType(MsgType type) {
        this.type = type;
    }

	public long getDateTime() {
		return dateTime;
	}

	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public String toString() {
		return "Msg [type=" + type + ", clientId=" + clientId + ", dateTime=" + dateTime + "]";
	}
    
}
