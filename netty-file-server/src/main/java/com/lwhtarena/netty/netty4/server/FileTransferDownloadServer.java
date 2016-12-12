package com.lwhtarena.netty.netty4.server;


import com.lwhtarena.netty.netty4.model.ResponseFile;
import com.lwhtarena.netty.netty4.util.FileTransferProperties;
import com.lwhtarena.netty.netty4.util.MD5FileUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.core.io.FileSystemResourceLoader;

import java.io.File;
import java.io.IOException;

/**
 * @author： liwh
 * @Date: 2016/11/17.
 * @Description：下载服务
 */
public class FileTransferDownloadServer {
    private Logger log = Logger.getLogger(FileTransferDownloadServer.class);

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

    private static String getSuffix(String fileName) {
        String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        return fileType;
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

        try {
            //响应的文件
            ResponseFile echo =new ResponseFile();
            File file =new File("D://lwhSpaces//Centos7.1bit64.xva");
            String fileName =file.getName(); ///======>>>> 获得文件名字
            echo.setFile(file); //文件名
            echo.setFile_md5(MD5FileUtil.getFileMD5String(file));
            echo.setFile_name(fileName);
            echo.setFile_name(getSuffix(fileName));
            echo.setStart(0);//文件开始位置

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        try {
            new FileTransferDownloadServer().bind(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
