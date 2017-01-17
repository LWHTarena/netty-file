package com.lwhtarena.netty.tutorial03.code;

import com.lwhtarena.netty.tutorial03.util.ObjectConvertUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @author： liwh
 * @Date: 2016/11/17.
 * @Description：解码器
 */
public class NettyMessageDecoder extends MessageToMessageDecoder<String> {

    @Override
    protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        Object outobj = ObjectConvertUtil.convertModle(msg);
        out.add(outobj);
    }


}
