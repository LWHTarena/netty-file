package com.lwhtarena.netty.netty4.code;

import com.lwhtarena.netty.netty4.util.ObjectConvertUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @author： liwh
 * @Date: 2016/11/17.
 * @Description：编码器
 */
public class NettyMessageEncoder extends MessageToMessageEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        out.add(ObjectConvertUtil.request(msg));
    }
}
