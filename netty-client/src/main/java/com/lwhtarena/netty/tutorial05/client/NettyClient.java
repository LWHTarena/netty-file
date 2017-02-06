package com.lwhtarena.netty.tutorial05.client;

import com.lwhtarena.netty.tutorial05.module.BaseMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 */
public class NettyClient {
    private static volatile Bootstrap bootstrap;
    private static volatile boolean closed = true;
    private static int port;
    private static String host;
    private static EventLoopGroup eventLoopGroup;
    private static SocketChannel socketChannel;
    
    /**
     * 启动socket服务器
     * @param port
     * @param host
     * @throws InterruptedException
     */
    public static void start(int port, String host) throws InterruptedException {
    	if(!closed){
    		return;
    	}
    	closed = false;
    	NettyClient.port = port;
    	NettyClient.host = host;
        eventLoopGroup=new NioEventLoopGroup();
        bootstrap=new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
        bootstrap.group(eventLoopGroup);
        bootstrap.remoteAddress(host,port);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new IdleStateHandler(20,10,0));
                socketChannel.pipeline().addLast(new ObjectEncoder());
                socketChannel.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                socketChannel.pipeline().addLast(new NettyClientHandler());
            }
        });
        doConnect();
    }
    
    /**
     * 服务器主动断开连接后 进行重连
     */
    public static void afreshConnect(){
    	if(null !=socketChannel){
    		if(socketChannel.isActive()){
    			socketChannel.close();
    		}
    		socketChannel = null;
    	}
    	if(null != bootstrap){
    		doConnect();
    	}
    }
    
    /**
     * 关闭客户端连接
     */
    public static void close(){
    	closed = true;
    	if(null != eventLoopGroup){
    		eventLoopGroup.shutdownGracefully();
    	}
    	System.out.println("客户端连接关闭!...");
    }
    
    /**
     * 进行连接
     */
    private static void doConnect() {
        if (closed) {
            return;
        }
		try {
			final ChannelFuture future = bootstrap.connect(host,port);
			future.addListener(new ChannelFutureListener() {
	            public void operationComplete(ChannelFuture f) throws Exception {
	                if (f.isSuccess()) {
	                	socketChannel = (SocketChannel)future.channel();
	                    System.out.println("connect server  成功---------");
	                } else {
	                    System.out.println("connect server  失败---------等待重新连接 " );
	                    f.channel().eventLoop().schedule(() -> doConnect(), 3, TimeUnit.SECONDS);
	                }
	            }
	        });
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 向服务器发送信息
     * @param msg
     */
    public static void sendMsg(BaseMsg msg){
    	try {
    		if(null != socketChannel){
    			System.out.println("开始发送信息到服务器.."+msg.toString());
    			socketChannel.writeAndFlush(msg);
    		}else{
    			System.out.println("发送失败! 未连接服务器或链接异常...");
    		}
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
    
    
    public static void main(String[]args) throws InterruptedException {
        NettyClient.start(9999,"localhost");
        //NettyClient.start(9999,"localhost");
       // NettyClient.start(9999,"localhost");
//        TimeUnit.SECONDS.sleep(3);
//        NettyClient.sendMsg(new CometMsg("001", new JSONObject()));
//       while(true){
//        	TimeUnit.SECONDS.sleep(3);
//        	JSONObject jsonObject = new JSONObject();
//        	jsonObject.put("comet msg ", " comet 信息...");
//        	NettyClient.sendMsg(new CometMsg("001", jsonObject));
//        }
    }
}
