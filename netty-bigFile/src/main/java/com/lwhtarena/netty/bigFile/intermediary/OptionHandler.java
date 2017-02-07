package com.lwhtarena.netty.bigFile.intermediary;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

import java.net.URI;

/**
 * @author： liwh
 * @Date: 2017/2/7.
 * @Description：<p>选项操作</P>
 */
public class OptionHandler {

    private static volatile Bootstrap bootstrap;
    private static EventLoopGroup eventLoopGroup;

    /**
     * 启动netty连接，建立客户端和服务端的连接
     * @throws Exception
     */
    public void start() throws Exception{
        try {
            eventLoopGroup=new NioEventLoopGroup();
            bootstrap=new Bootstrap();
            bootstrap.group(eventLoopGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
            bootstrap.handler(new IntermediaryChannelHandler());

            ChannelFuture f =bootstrap.connect("127.0.0.1",9003).sync();
            URI uri =new URI("..");
            /**DefaultFullHttpRequest --模拟浏览器请求，http的版本|http的请求方式|请求url**/
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.toASCIIString());
        /*--======== 构建 http 请求 =========--*/
            request.headers().set(HttpHeaders.Names.HOST, "127.0.0.1");
            request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());

        /*--======== 发送 http 请求 =========--*/
            f.channel().write(request);
            f.channel().flush();
            f.channel().closeFuture().sync();

        }finally {
            eventLoopGroup.shutdownGracefully();//优雅退出
        }


    }

    /**
     * 暂停下载 -- 进入休眠
     * @throws Exception
     */
    public void suspend() throws Exception{
        eventLoopGroup.wait();
    }

    /**
     * 继续下载
     * @throws Exception
     */
    public void resume() throws Exception{
        eventLoopGroup.next();
    }
}
