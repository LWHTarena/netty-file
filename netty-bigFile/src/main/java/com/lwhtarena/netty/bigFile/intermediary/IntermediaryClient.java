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
 * @Date: 2017/2/6.
 * @Description：<p>中介客户端</P>
 */
public class IntermediaryClient {

    /**
     *
     * 向互联网下载输入完整路径
     * @param host 目的主机 ip 或域名
     * @param port 目标主机端口
     * @param url 文件路径
     * @param local	本地存储路径 ===文件名
     */
    public void connect(String host,int port,String url,String local) throws Exception{

        EventLoopGroup workerGroup =new NioEventLoopGroup();

        try {
            Bootstrap b =new Bootstrap();

            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE,true);
            b.handler(new IntermediaryChannelHandler());

        /*--========== 启动客户端 ==========--*/
            ChannelFuture f =b.connect(host,port).sync();
            URI uri = new URI(url);
            /**DefaultFullHttpRequest --模拟浏览器请求，http的版本|http的请求方式|请求url**/
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.toASCIIString());

        /*--======== 构建 http 请求 =========--*/
            request.headers().set(HttpHeaders.Names.HOST, host);
            request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());

        /*--======== 发送 http 请求 =========--*/
            f.channel().write(request);
            f.channel().flush();
            f.channel().closeFuture().sync();
        }finally {
            workerGroup.shutdownGracefully();//优雅退出
        }
    }
}
