package com.lwhtarena.netty.heartbeat;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.MessageToMessageEncoder;


/**
 * @ModifiedBy 修改人
 * @Project MessageEncoder
 * @Desciption 编码器
 *	
 * @Author SuFH
 * @Data 2016-5-18下午2:26:25
 *
 */
public class MessageEncoder extends MessageToMessageEncoder<String> {

	@Override
	protected void encode(io.netty.channel.ChannelHandlerContext arg0,
			String msg, List<Object> out) throws Exception {
		byte[] tmp = msg.getBytes();
		ByteBuf b = Unpooled.buffer();
		b.writeInt(tmp.length);
		b.writeBytes(tmp);
		out.add(b);
		System.out.println("MessageEncoder"+ "发送信息");
	}
}
