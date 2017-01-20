package com.lwhtarena.netty.heartbeat;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class AcceptorIdleStateTrigger extends ChannelInboundHandlerAdapter{


	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		// TODO 自动生成的方法存根

		if (evt instanceof IdleStateEvent) {
			IdleState state = ((IdleStateEvent) evt).state();
			if (state == IdleState.WRITER_IDLE) {
				ctx.writeAndFlush("1");
			}
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}
}
