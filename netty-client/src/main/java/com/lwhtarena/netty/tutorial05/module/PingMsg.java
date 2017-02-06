package com.lwhtarena.netty.tutorial05.module;


import com.lwhtarena.netty.tutorial05.module.enums.MsgType;

public class PingMsg extends BaseMsg{

	private static final long serialVersionUID = -1287809029734521425L;

	public PingMsg(String clientId) {
		super(clientId);
		this.setType(MsgType.PING);
	}
}
