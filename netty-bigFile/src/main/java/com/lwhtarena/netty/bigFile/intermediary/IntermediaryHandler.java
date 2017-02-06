package com.lwhtarena.netty.bigFile.intermediary;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.internal.SystemPropertyUtil;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author： liwh
 * @Date: 2017/2/6.
 * @Description：<p></P>
 */
public class IntermediaryHandler extends ChannelInboundHandlerAdapter{

    private boolean readingChunks = false;         // 分块读取开关
    private FileOutputStream fOutputStream = null; // 文件输出流
    private File localfile = null;                 // 下载文件的本地对象
    private String local = null;                   // 待下载文件名
    private int succCode;                          // 状态码
    private long process;//

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("----------------- channelRead --------------------");

        if(msg instanceof HttpResponse){//响应头信息
            HttpResponse response = (HttpResponse) msg;
            succCode = response.getStatus().code();
            if (succCode == 200) {
                setDownLoadFile();// 设置下载文件
                readingChunks = true;
            }
            if(succCode == 404){
                System.out.println("=====================>>>>>>>>>>>>>>>>404");
            }
        }

        if (msg instanceof HttpContent) {// 响应内容体信息

            System.out.println("文件的长度："+new File(localfile.getAbsolutePath()).length());

            HttpContent chunk = (HttpContent) msg;
            if (chunk instanceof LastHttpContent) {
                readingChunks = false;
            }

            ByteBuf buffer = chunk.content();
            byte[] dst = new byte[buffer.readableBytes()];

            if (succCode == 200) {
                while (buffer.isReadable()) {
                    buffer.readBytes(dst);
                    fOutputStream.write(dst);
                    buffer.release();
                }
                if (null != fOutputStream) {
                    fOutputStream.flush();
                }
            }

            if(succCode == 404){
                System.out.println("=====================>>>>>>>>>>>>>>>>404");
            }

        }


        if (!readingChunks) {
            if (null != fOutputStream) {
                System.out.println("Download done->"+ localfile.getAbsolutePath());
                fOutputStream.flush();
                fOutputStream.close();
                localfile = null;
                fOutputStream = null;
            }
            ctx.channel().close();
        }
    }

    /**
     * 配置本地参数，准备下载
     */
    private void setDownLoadFile() throws Exception {
        if (null == fOutputStream) {
            local = SystemPropertyUtil.get("user.dir") + File.separator +local;
            System.out.println("================>>>>>>>>>>>>>>>>>>>>>");
            System.out.println(local);
            localfile = new File(local);
            if (!localfile.exists()) {
                localfile.createNewFile();
            }
            fOutputStream = new FileOutputStream(localfile);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        System.out.println("管道异常：" + cause.getMessage());
        cause.printStackTrace();
        ctx.channel().close();
    }


    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println(ctx);
        System.out.println(ctx.channel());
//        super.channelWritabilityChanged(ctx);
    }


    /**
     * 是客户端链接到服务端的标志
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("有效的通道=========>>>>>>>>>");
//        super.channelActive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<< 下载完毕");
        System.out.println(ctx.channel().toString());
//        super.channelReadComplete(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("无效=========>>>>>>>>>");
//        super.channelInactive(ctx);
    }
}
