package com.lwhtarena.netty.heartbeat;

import java.nio.charset.Charset;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.MessageToMessageDecoder;

public class MessageDecoder extends MessageToMessageDecoder<ByteBuf> {

	@Override
	protected void decode(io.netty.channel.ChannelHandlerContext arg0,
			ByteBuf buffer, List<Object> out) throws Exception {
		if (buffer.readableBytes() < 4) {
			return;
		}
		int dataLength = buffer.readInt();
		buffer.markReaderIndex();
		if(dataLength>buffer.readableBytes())
		{
			return;
		}
		ByteBuf resultBuf =  Unpooled.buffer();
		resultBuf.writeBytes(buffer, buffer.readerIndex(), dataLength);
		//服务端解码
		out.add(resultBuf.toString(Charset.defaultCharset()));
//		out.add(buffer.toString(Charset.defaultCharset()));
	}
}
