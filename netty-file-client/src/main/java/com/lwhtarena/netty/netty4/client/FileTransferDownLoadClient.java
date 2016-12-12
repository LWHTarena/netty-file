package com.lwhtarena.netty.netty4.client;

import com.lwhtarena.netty.netty4.model.RequestFile;
import com.lwhtarena.netty.netty4.util.MD5FileUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.File;

/**
 * @author： liwh
 * @Date: 2016/11/17.
 * @Description：
 */
public class FileTransferDownLoadClient {

    public void connect(int port, String host, final RequestFile echoFile) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<Channel>() {

                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(new ObjectEncoder());
                    ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null))); // 最大长度

                    ch.pipeline().addLast(new NettyMessageDecoder());//设置服务器端的编码和解码
                    ch.pipeline().addLast(new NettyMessageEncoder());
                    ch.pipeline().addLast(new FileTransferClientHandler(echoFile));
                }
            });
            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    private static String getSuffix(String fileName) {
        String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        return fileType;
    }

    /**
     * 绑定端口
     * @param port
     * @throws Exception
     */
    public void bind(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>(){
                  protected void initChannel(SocketChannel ch) throws Exception {
//                      ch.pipeline().addLast(new FileTransferClientHandler());
                      ch.pipeline().addLast(new ObjectEncoder());
                      ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null))); // 最大长度

                      ch.pipeline().addLast(new NettyMessageDecoder());//设置服务器端的编码和解码
                      ch.pipeline().addLast(new NettyMessageEncoder());

//                      ch.pipeline().addLast(new SecureServerHandler());
//                      ch.pipeline().addLast(new FileTransferServerHandler());

                  }
            });


//          ServerBootstrap b = new ServerBootstrap();
//            b.group(bossGroup, workerGroup)
//                    .channel(NioServerSocketChannel.class)
//                    .option(ChannelOption.SO_BACKLOG, 1024)
//                    .childHandler(new FileChannelInitializer());
//
//
//            ChannelFuture f = b.bind(port).sync();
//            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        int port = 10012;
		/*if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}*/
        try {
            RequestFile echo = new RequestFile();
            File file = new File("D://lwhSpaces//Centos7.1bit64.xva");  //  "D://files/xxoo"+args[0]+".amr"
            String fileName = file.getName();// 文件名
            echo.setFile(file);
            echo.setFile_md5(MD5FileUtil.getFileMD5String(file));
            echo.setFile_name(fileName);
            echo.setFile_type(getSuffix(fileName));
            echo.setStarPos(0);// 文件开始位置
            new FileTransferDownLoadClient().connect(port, "127.0.0.1", echo);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        if (args != null && args.length > 0) {
//            try {
//                port = Integer.valueOf(args[0]);
//            } catch (NumberFormatException e) {
//                e.printStackTrace();
//            }
//        }
//
        try {
            new FileTransferDownLoadClient().bind(port);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
