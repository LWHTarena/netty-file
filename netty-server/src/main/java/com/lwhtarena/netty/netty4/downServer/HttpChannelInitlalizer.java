package com.lwhtarena.netty.netty4.downServer;

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
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpChannelHandler());
    }

}
