package com.lwhtarena.netty.heartbeat;

import java.nio.Buffer;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class MessageEncoder extends MessageToMessageEncoder<String> {

	@Override
	protected void encode(ChannelHandlerContext arg0,
			String msg, List<Object> out) throws Exception {
		byte[] tmp = msg.getBytes();
		ByteBuf b = Unpooled.buffer();
		b.writeInt(tmp.length);
		b.writeBytes(tmp);
		out.add(b);
		System.out.println("MessageEncoder"+ "发送信息");
	}
}
