package com.lwhtarena.netty.heartbeat;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

public class asdsad extends ChannelInboundHandlerAdapter implements TimerTask {

	private final Timer timer=new HashedWheelTimer();  

	private volatile boolean reconnect = true;  

	@Override  
	public void channelActive(ChannelHandlerContext ctx) throws Exception {  

		System.out.println("当前链路已经激活了，重连尝试次数重新置为0");  

		API.attempts = 0;  
		ctx.fireChannelActive();  
	}  

	@Override  
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {  
		System.out.println("链接关闭");  
		ctx.close();
		if(reconnect){  
			System.out.println("链接关闭，将进行重连"+API.attempts);  
			if (API.attempts < 12) {  
				API.attempts++;
				int timeout = 2 << API.attempts;  
				timer.newTimeout(this, timeout, TimeUnit.MILLISECONDS); 
			}  else{
				System.out.println("等待主动触发重连");//主动触发需要将API.attempts清零并调用NettyClient.doConnect()
			}

		}
	} 
	
	public void run(Timeout timeout) throws Exception { 
		NettyClient.doConnect(); //延时重连
	}
}
