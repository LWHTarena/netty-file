package com.lwhtarena.netty.tutorial05.client;

import com.alibaba.fastjson.JSONObject;
import com.lwhtarena.netty.tutorial05.module.BaseMsg;
import com.lwhtarena.netty.tutorial05.module.LoginMsg;
import com.lwhtarena.netty.tutorial05.module.PingMsg;
import com.lwhtarena.netty.tutorial05.module.QueryMsg;
import com.lwhtarena.netty.tutorial05.module.enums.MsgType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<BaseMsg> {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:
                	System.out.println("心跳连接起始!..");
                	ctx.writeAndFlush(new PingMsg("001"));
                    break;
                default:
                    break;
            }
        }
    }
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) throws Exception {
    	//接收服务端发送来的信息
    	MsgType msgType = baseMsg.getType();
		try {
			switch (msgType){
			    case LOGIN:{
			        //向服务器发起登录
			    	NettyClient.sendMsg(new LoginMsg("001"));
			    	System.out.println("客户端 进行登录请求 ..");
			    }break;
			    case PING:{
			        //接受服务器Ping命令
			    	System.out.println("来自服务器的心跳连接回应!..");
			    }break;
			    case QUERY:{
			    	//客户端接收查询信息
			    	QueryMsg queryMsg = (QueryMsg) baseMsg;
			    	JSONObject data = queryDatas(queryMsg);
			    	queryMsg.setResultData(data);
			    	NettyClient.sendMsg(queryMsg);
			    }break;
			    default:break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        ReferenceCountUtil.release(msgType);
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {//连接建立准备通信时被调用
    	ctx.pipeline().addLast(new StringEncoder(Charset.forName("utf-8")));
        System.out.println("客户端 准备通信...");
    }
    
    public void channelInactive(ChannelHandlerContext ctx)throws Exception{//连接断开时调用?
    	System.out.println("---- Channel Inactive/Connection Close ----");
    	ctx.channel().close();
    	NettyClient.afreshConnect();
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	//exceptionCaught()事件处理方法是当出现Throwable对象才会被调用，即当Netty由于IO错误或者处理器在处理事件
    	//时抛出的异常时。在大部分情况下，捕获的异常应该被记录下来并且把关联的channel给关闭掉。然而这个方法的处
    	//理方式会在遇到不同异常的情况下有不同的实现，比如你可能想在关闭连接之前发送一个错误码的响应消息。
    	System.out.println("客户端: 连接异常!..");
        ctx.close();
    }
    
    /**
     * 模拟数据查询
     * @param query
     * @return
     * @throws InterruptedException 
     */
    private JSONObject queryDatas(QueryMsg query) throws InterruptedException{
    	JSONObject datas = new JSONObject();
    	TimeUnit.SECONDS.sleep(4);
    	switch (query.getQueryType()) {
		case INFO:{
			datas.put("Id", query.getEquId());
			datas.put("Name", "查询数据1");
			datas.put("Type", "A");
			datas.put("time", "0000-00-00 00:00:00");
		}break;
		case LOG:{
			datas.put("A", query.getEquId());
			datas.put("B", 0);
			datas.put("C", "0");
			datas.put("D", false);
			datas.put("E", "");
			datas.put("F", "");
			datas.put("G", 20);
		}break;
		default:
			break;
		}
    	return datas;
    }

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) throws Exception {

	}
}
