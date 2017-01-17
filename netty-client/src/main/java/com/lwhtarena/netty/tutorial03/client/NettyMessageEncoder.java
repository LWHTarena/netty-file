package com.lwhtarena.netty.tutorial03.client;

import com.lwhtarena.netty.tutorial03.util.ObjectConvertUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**编码器
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
