package com.lwhtarena.netty.tutorial01;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;

/**
 * @author： liwh
 * @Date: 2017/1/13.
 * @Description：<p>增加自己的逻辑HelloServerHandler</P>
 * <p>自己的Handler我们这里先去继承extends官网推荐的SimpleChannelInboundHandler<C> 。在这里C，由于我们需求里面发送的是字符串。这里的C改写为String。</p>
 */
public class HelloServerHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 收到消息直接打印输出
        System.out.println(ctx.channel().remoteAddress() + " Say : " + msg);

        // 返回客户端消息 - 我已经接收到了你的消息
        ctx.writeAndFlush("Received your message !\n");
    }

    /**
     *
     * 覆盖 channelActive 方法 在channel被启用的时候触发 (在建立连接的时候)
     *
     * channelActive 和 channelInActive 在后面的内容中讲述，这里先不做详细的描述
     * */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");

        ctx.writeAndFlush( "Welcome to " + InetAddress.getLocalHost().getHostName() + " service!\n");

        super.channelActive(ctx);
    }
}

/***
 * 在channelHandlerContent自带一个writeAndFlush方法。方法的作用是写入Buffer并刷入。
 *
 * 注意:在3.x版本中此处有很大区别。在3.x版本中write()方法是自动flush的。在4.x版本的前面几个版本也是一样的。
 * 但是在4.0.9之后修改为WriteAndFlush。普通的write方法将不会发送消息。需要手动在write之后flush()一次
 *
 * 这里channeActive的意思是当连接活跃(建立)的时候触发.输出消息源的远程地址。并返回欢迎消息。
 *
 * channelRead0 在这里的作用是类似于3.x版本的messageReceived()。可以当做是每一次收到消息是触发。
 *
 * 我们在这里的代码是返回客户端一个字符串"Received your message !".
 *
 * 注意:字符串最后面的"\n"是必须的。因为我们在前面的解码器DelimiterBasedFrameDecoder是一个根据字符串结
 * 尾为“\n”来结尾的。假如没有这个字符的话。解码会出现问题。
 *
 */
