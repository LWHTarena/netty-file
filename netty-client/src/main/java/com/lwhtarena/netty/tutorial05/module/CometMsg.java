package com.lwhtarena.netty.tutorial05.module;

import com.alibaba.fastjson.JSONObject;
import com.lwhtarena.netty.tutorial05.module.enums.MsgType;

public class CometMsg extends BaseMsg{

	private static final long serialVersionUID = -8966952564685219343L;
	
	private JSONObject jsonMsg;
	
	public CometMsg(String clientId,JSONObject jsonMsg) {
		super(clientId);
		this.setType(MsgType.COMET);
		this.jsonMsg = jsonMsg;
	}

	public JSONObject getJsonMsg() {
		return jsonMsg;
	}

	public void setJsonMsg(JSONObject jsonMsg) {
		this.jsonMsg = jsonMsg;
	}
	
	

}
