package com.lwhtarena.netty.heartbeat;

import java.util.Date;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class UserHandler extends SimpleChannelInboundHandler<String>{

	int loss_connect_time=0;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		// TODO 自动生成的方法存根
		System.out.println("UserHandlerchannelRead0!!!!!    "+ msg+"     " +new Date());

	}

}
