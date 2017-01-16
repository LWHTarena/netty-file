package com.lwhtarena.netty.tutorial02;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author： liwh
 * @Date: 2017/1/16.
 * @Description：<p>MappedByteBuffer的读取/写入文件和普通I/O流的对比</P>
 */
public class MapMemeryBuffer02 {

    public static void main(String[] args) throws Exception{
        ByteBuffer byteBuf = ByteBuffer.allocate(1024 * 14 * 1024);

        byte[] bbb = new byte[14 * 1024 * 1024];

        FileInputStream fis = new FileInputStream("d:\\test");

        FileOutputStream fos = new FileOutputStream("d:\\outFile.txt");

        FileChannel fc = fis.getChannel();

        long timeStar = System.currentTimeMillis();//得到当前的时间

        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());//1 读取

        long timeEnd = System.currentTimeMillis();//得到当前的时间

        System.out.println("Read time :" + (timeEnd - timeStar) + "ms");

        timeStar = System.currentTimeMillis();

        //fos.write(bbb);// 写入
        mbb.flip();

        timeEnd = System.currentTimeMillis();

        System.out.println("Write time :" + (timeEnd - timeStar) + "ms");

        fos.flush();

        fc.close();

        fis.close();

    }
}
