package com.lwhtarena.netty.tutorial03.client;

import com.lwhtarena.netty.tutorial03.util.ObjectConvertUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 解码器
 * @author： liwh
 * @Date: 2016/11/17.
 * @Description：解码器
 */
public class NettyMessageDecoder extends MessageToMessageDecoder<Object> {
    @Override
    protected void decode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        String o = msg.toString();
        Object outobj = ObjectConvertUtil.convertModle(o);
        out.add(outobj);
    }
}
