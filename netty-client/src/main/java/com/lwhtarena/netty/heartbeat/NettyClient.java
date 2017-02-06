package com.lwhtarena.netty.heartbeat;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @ModifiedBy 修改人
 * @Project NettyServer
 * @Desciption 客户端服务
 * 1.配置环境
 * 2.建立连接doConnect()
 * 3.心跳包、连接处理类 line55
 *	
 * @Author SuFH
 * @Data 2016-4-29上午10:27:13
 *
 */
public class NettyClient{

	public static  ChannelFuture future;
	private static  EventLoopGroup workerGroup = new NioEventLoopGroup();

	static Bootstrap client = new Bootstrap();
	public static  void connect() {
		// TODO 自动生成的方法存根
		new Thread(new  Runnable() {
			public void run() {

				client.group(workerGroup).channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,60000)
				.option(ChannelOption.AUTO_READ, true)
				.handler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						// TODO 自动生成的方法存根
						ChannelPipeline	pipeline=ch.pipeline();
						
						pipeline.addLast(new asdsad());
						pipeline.addLast(new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS));
						
						pipeline.addLast("encoder",new MessageEncoder());
						pipeline.addLast("decoder",new MessageDecoder());
						pipeline.addLast(new AcceptorIdleStateTrigger());
						pipeline.addLast("handle",new ClientHandler());
					}

				});

				client.option(ChannelOption.TCP_NODELAY, true);
				client.option(ChannelOption.SO_TIMEOUT, 4000);
				client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
				future=client.connect(API.IP, API.PORT);
				future.addListener(new ChannelFutureListener() {
					public void operationComplete(ChannelFuture f) throws Exception {
						// TODO 自动生成的方法存根
						if(f.isSuccess()){
							System.out.println("连接成功");
						}else{
							System.out.println("重连失败"); 
							f.channel().pipeline().fireChannelInactive();
						}
					}
				});
			}

		}).start();
	}
	/**
	 * doConnect：连接到服务端 
	 * 端口8001
	 */
	public static void doConnect(){
		try {
			future=client.connect(API.IP, API.PORT);
			future.addListener(new ChannelFutureListener() {
				public void operationComplete(ChannelFuture f) throws Exception {
					// TODO 自动生成的方法存根
					if(f.isSuccess()){
						System.out.println("连接成功");
					}else{
						System.out.println("重连失败"); 
						f.channel().pipeline().fireChannelInactive();
					}
				}
			});
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public static void main(String []arg)
	{
		NettyClient.connect();
	}
}
