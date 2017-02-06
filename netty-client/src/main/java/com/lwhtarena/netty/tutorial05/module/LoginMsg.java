package com.lwhtarena.netty.tutorial05.module;


import com.lwhtarena.netty.tutorial05.module.enums.MsgType;

public class LoginMsg extends BaseMsg{

	private static final long serialVersionUID = -8966952564685219343L;
	
	public LoginMsg(String clientId) {
		super(clientId);
		this.setType(MsgType.LOGIN);
	}
}
