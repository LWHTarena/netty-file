package com.lwhtarena.netty.heartbeat;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;


/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG
*/



/**
 * @ModifiedBy 修改人
 * @Project NettyServer
 * @Desciption 服务
 * 主函数，服务基类
 *	
 * @Author SuFH
 * @Data 2016-6-8上午9:46:01
 *
 */
public class NettyServer {



	public static ChannelGroup group=null;

	public static DefaultChannelGroup clientsGroup;

	EventLoopGroup bossGroup=new NioEventLoopGroup();
	EventLoopGroup workGroup=new NioEventLoopGroup();

	public NettyServer(){

		ServerBootstrap server=new ServerBootstrap();
		server.group(bossGroup, workGroup)
		.channel(NioServerSocketChannel.class)
		.handler(new LoggingHandler(LogLevel.INFO))  
		.option(ChannelOption.SO_BACKLOG, 1024)
		.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
		.option(ChannelOption.SO_KEEPALIVE, true)
		
		.childOption(ChannelOption.TCP_NODELAY, true)
		.childHandler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				// TODO 自动生成的方法存根
				ChannelPipeline pipeline = ch.pipeline();
				
				pipeline.addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
				
				pipeline.addLast("encoder", new MessageEncoder());
				pipeline.addLast("decoder", new MessageDecoder());
				pipeline.addLast(new AcceptorIdleStateTrigger()); 
				pipeline.addLast("user", new UserHandler());
			}
		});

		try {

			ChannelFuture future = server.bind(API.PORT).sync();//启动服务器

			InetAddress locAdd = InetAddress.getLocalHost();
			System.out.println("本机可达" + locAdd.isReachable(API.PORT));
			future.addListener(new ChannelFutureListener() {
				public void operationComplete(ChannelFuture arg0) throws Exception {
					// TODO 自动生成的方法存根
					if (arg0.isSuccess()) {
						System.out.println("服务端开启成功");
						// 命令线程启动
					}
				}
			});
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}


	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		new NettyServer();

	}
}
