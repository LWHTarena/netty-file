package com.lwhtarena.netty.tutorial05.server;


import com.alibaba.fastjson.JSONObject;

import com.lwhtarena.netty.tutorial05.module.*;
import com.lwhtarena.netty.tutorial05.module.enums.MsgType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.Charset;

/**
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<BaseMsg> {
	
    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception { //连接建立时发送给客户端的消息
    	channelHandlerContext.pipeline().addLast(new StringEncoder(Charset.forName("utf-8")));
    	System.out.println("服务器:建立了一个连接");
    	//要求客户端登录
    	NettyServer.sendLoginMsg(channelHandlerContext);
    }
	
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {//连接断开时调用
        ServerTools.removeConnMap((SocketChannel)ctx.channel());
        System.out.println("服务器:一个连接断开了");
    }
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) throws Exception {
    	
        try {
			if(MsgType.LOGIN.equals(baseMsg.getType())){
				LoginMsg loginMsg = (LoginMsg) baseMsg;
				ServerTools.addConnMap(loginMsg.getClientId(),(SocketChannel)channelHandlerContext.channel());
			    System.out.println("client:"+loginMsg.getClientId()+" 登录成功");
			    //登录成功  要求客户端发送topo信息
			}else{
			    if(ServerTools.getConnMap(baseMsg.getClientId())==null){
			        //说明未登录，或者连接断了，服务器向客户端发起登录请求，让客户端重新登录
			    	System.out.println("客户端身份未识别,要求其重新登录!...");
			    	NettyServer.sendLoginMsg(channelHandlerContext);
			    }else{
			    	switch (baseMsg.getType()){
			        case COMET:{
			            //收到Comet信息
			        	CometMsg cometMsg = (CometMsg) baseMsg;
			        	System.out.println("Comet: "+cometMsg.getJsonMsg());
			        }break;
			        case QUERY:{
			            //收到查询回复 1、检查回复ID 向对应队列添加数据
			        	QueryMsg queryMsg = (QueryMsg) baseMsg;
			        	JSONObject data = queryMsg.getResultData();
			        	ServerTools.appendQueryDequeData(queryMsg.getQueryId(), data);
			        }break;
			        case PING:{
				        //接受客户端Ping命令  向客户端发送ping命令
			        	System.out.println("心跳连接响应...");
			        	channelHandlerContext.channel().writeAndFlush(new PingMsg(""));
				    }break;
			        default:break;
			    }
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        ReferenceCountUtil.release(baseMsg);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	//exceptionCaught()事件处理方法是当出现Throwable对象才会被调用，即当Netty由于IO错误或者处理器在处理事件
    	//时抛出的异常时。在大部分情况下，捕获的异常应该被记录下来并且把关联的channel给关闭掉。然而这个方法的处
    	//理方式会在遇到不同异常的情况下有不同的实现，比如你可能想在关闭连接之前发送一个错误码的响应消息。
    	System.out.println("服务端 连接异常!..");
        ctx.close();
    }

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) throws Exception {

	}
}
