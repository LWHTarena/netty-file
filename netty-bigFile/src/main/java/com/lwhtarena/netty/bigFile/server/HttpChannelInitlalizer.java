package com.lwhtarena.netty.bigFile.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author： liwh
 * @Date: 2016/11/17.
 * @Description：
 */
public class HttpChannelInitlalizer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());

        /*
         * 不能添加这个，对传输文件 进行了大小的限制。。。。。
         */
//      pipeline.addLast("aggregator", new HttpChunkAggregator(6048576));

        pipeline.addLast(new HttpObjectAggregator(65536));

        /**--==加入chunked 主要作用是支持异步发送的码流（大文件传输），但不占用过多的内存，防止java内存溢出==--**/
        pipeline.addLast(new ChunkedWriteHandler());

        /**--==加入自定义处理文件服务器的业务逻辑handler==--**/
        pipeline.addLast(new HttpChannelHandler());
    }

}
