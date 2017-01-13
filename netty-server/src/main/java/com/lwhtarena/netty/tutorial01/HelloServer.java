package com.lwhtarena.netty.tutorial01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author： liwh
 * @Date: 2017/1/13.
 * @Description：<p>创建一个服务 HelloServer</P>
 */
public class HelloServer {

    /**
     * 服务端监听的端口地址
     */
    private static final int portNumber = 7878;

    /**<p>定义了两个工作线程，一个命名为WorkerGroup，另一个命名为BossGroup。都是实例化NioEventLoopGroup。</p>
     * <p>这一点和3.x版本中基本思路是一致的。Worker线程用于管理线程为Boss线程服务。</p>
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new HelloServerInitializer());

            // 服务器绑定端口监听
            ChannelFuture f = b.bind(portNumber).sync();
            // 监听服务器关闭监听
            f.channel().closeFuture().sync();

            // 可以简写为
            /* b.bind(portNumber).sync().channel().closeFuture().sync(); */
        } finally {
            /** <p>EventLoopGroup，它是4.x版本提出来的一个新概念。类似于3.x版本中的线程。用于管理Channel连接的。</p>
             * <p>shutdownGraceFully()，翻译为中文就是优雅的全部关闭.</p>
             * <p>我们可以在DefaultEventExecutorGroup的父类MultithreadEventExecutorGroup中看到它的实现代码。关闭
             * 了全部EventExecutor数组child里面子元素。相比于3.x版本这是一个比较重大的改动。开发者可以很轻松的全部关闭，
             * 而不需要担心出现内存泄露。</p>
             *
             */
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
