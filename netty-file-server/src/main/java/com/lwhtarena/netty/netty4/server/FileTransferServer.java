package com.lwhtarena.netty.netty4.server;


import com.lwhtarena.netty.netty4.util.FileTransferProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.core.io.FileSystemResourceLoader;

import java.io.IOException;

/**
 * @author： liwh
 * @Date: 2016/11/17.
 * @Description：
 */
public class FileTransferServer {
    private Logger log = Logger.getLogger(FileTransferServer.class);

    public void bind(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new FileChannelInitializer());

            log.info("bind port:"+port);

            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private static void init(){
        try {
            //请把加载属性文件放在 加载日志配置的上面，因为读取日志输出的目录配置在 属性文件里面
            FileTransferProperties.load("classpath:systemConfig.properties");

            System.setProperty("WORKDIR", FileTransferProperties.getString("WORKDIR","/"));

            PropertyConfigurator.configure(new FileSystemResourceLoader().getResource(
                    "classpath:log4j.xml").getInputStream());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        init();
        // 获取端口
        int port  = FileTransferProperties.getInt("port",10012);

        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        try {
            new FileTransferServer().bind(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
