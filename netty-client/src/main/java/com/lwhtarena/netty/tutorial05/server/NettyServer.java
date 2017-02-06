package com.lwhtarena.netty.tutorial05.server;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import com.alibaba.fastjson.JSONObject;
import com.lwhtarena.netty.tutorial05.module.LoginMsg;
import com.lwhtarena.netty.tutorial05.module.QueryMsg;
import com.lwhtarena.netty.tutorial05.module.enums.QueryType;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 */
public class NettyServer {
	private static EventLoopGroup boss;
	private static EventLoopGroup worker;
	private static ServerBootstrap bootstrap;
    private static int port;
    private static volatile boolean closed = true;
    
    /**启动socket 服务器
     * @param port
     * @throws InterruptedException
     */
    public static void start(int port){
    	if(!closed){
    		return;
    	}
    	closed = false;
    	NettyServer.port = port;
        boss=new NioEventLoopGroup();
        worker=new NioEventLoopGroup();
        bootstrap=new ServerBootstrap();
        bootstrap.group(boss,worker);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.SO_BACKLOG, 128);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline p = socketChannel.pipeline();
                p.addLast(new ObjectEncoder());
                p.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                p.addLast(new NettyServerHandler());
            }
        });
        doBind();
    }
    /**
     * 进行绑定连接
     */
    private static void doBind() {
    	if (closed) {
            return;
        }
        bootstrap.bind(port).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                if (f.isSuccess()) {
                	 System.out.println("server start---------------");
                } else {
                	 System.out.println("server server  失败---------等待重启 " );
                     f.channel().eventLoop().schedule(() -> doBind(), 3, TimeUnit.SECONDS);
                }
            }
        });
    }
    
    /**
     * 向客户端发送登录命令
     * @param channelHandlerContext
     */
    public static void sendLoginMsg(ChannelHandlerContext channelHandlerContext){
    	if(null != channelHandlerContext){
    		channelHandlerContext.channel().writeAndFlush(new LoginMsg(""));
    	}
    }
    
    /**
     * 向指定客户端发送查询命令
     * @param queryMsg
     * @return
     */
    public static JSONObject sendQueryMsg(QueryMsg queryMsg){
    	JSONObject data = null;
    	try {
			if(ServerTools.checkConnMap(queryMsg.getClientId())){
				//可查询 
				System.out.println("服务器发送查询指令! :"+queryMsg.getClientId());
				//1、 建立查询队列 
				LinkedBlockingDeque<JSONObject> deque = ServerTools.addQueryDeque(queryMsg.getQueryId(),5000);
				//查询数据
				if(ServerTools.sendConnMsg(queryMsg)){
					//等待查询结果
					data = deque.take();
				}
			}else{
				System.out.println("服务器发送查询指令失败! 无连接 :"+queryMsg.getClientId());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//清除数据队列
			ServerTools.removeQueryDeque(queryMsg.getQueryId());
		}
    	return data;
    }
    
    /**
     * 关闭socket 服务器
     */
    public static void close() {
    	closed = true;
    	if(null != boss){
    		boss.shutdownGracefully();
    	}
    	if(worker != null){
    		worker.shutdownGracefully();
    	}
    	System.out.println("服务端连接关闭!...");
    }
    public static void main(String []args) throws InterruptedException {
        NettyServer.start(9999);
//        TimeUnit.SECONDS.sleep(10);
//        System.out.println("服务器发送查询命令");
//        JSONObject data = NettyServer.sendQueryMsg(new QueryMsg("001", "111", QueryType.INFO,null));
//        System.out.println("服务器查询结果:"+data);
        while(true){
//        	ServerTools.sendConnMsg(new PingMsg("001"));
        	JSONObject data = NettyServer.sendQueryMsg(new QueryMsg("001", "111", QueryType.INFO,null));
        	System.out.println("服务器查询结果:"+data);
        	TimeUnit.SECONDS.sleep(1);
        }
    }
}
