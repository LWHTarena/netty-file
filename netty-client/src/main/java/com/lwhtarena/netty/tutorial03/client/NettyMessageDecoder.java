package com.lwhtarena.netty.tutorial03.client;

import com.lwhtarena.netty.tutorial03.util.ObjectConvertUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @author： liwh
 * @Date: 2017/1/16.
 * @Description：<p>解密器</P>
 */
public class NettyMessageDecoder extends MessageToMessageDecoder<Object> {
    @Override
    protected void decode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        String o = msg.toString();
        Object outobj = ObjectConvertUtil.convertModle(o);
        out.add(outobj);
    }
}
