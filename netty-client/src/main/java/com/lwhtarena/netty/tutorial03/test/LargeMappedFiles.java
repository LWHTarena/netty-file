package com.lwhtarena.netty.tutorial03.test;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author： liwh
 * @Date: 2017/1/17.
 * @Description：<p></P>
 */
public class LargeMappedFiles {

    static int length = 0x8FFFFFF; // 128 Mb

    public static void main(String[] args) throws Exception {
        MappedByteBuffer out = new RandomAccessFile("D:/lwhSpaces/TemplateRepo/windows2008.xva", "rw").getChannel().map(FileChannel.MapMode.READ_WRITE, 0, length);
        for (int i = 0; i < length; i++)
            out.put((byte) 'x');
        System.out.println("Finished  writing ");
        for (int i = length / 2; i < length / 2 + 6; i++)
            System.out.print((char) out.get(i)); // read file
    }
}

/**
 * 说明：
 * RandomAccessFile类：支持对随机存取文件的读取和写入。随机存取文件的行为类似存储在文件系统中的一个大型字节数组。
 * FileChannel：用于读取、写入、映射和操作文件的通道。
 * MappedByteBuffer：直接字节缓冲区，其内容是文件的内存映射区域。 参数部分：
 * FileChannel.MapMode.READ_WRITE（mode） - 根据是按只读、读取/写入或专用（写入时拷贝）来映射文件，分别为 FileChannel.MapMode 类中所定义的 READ_ONLY、READ_WRITE 或 PRIVATE 之一；
 * 0（position） - 文件中的位置，映射区域从此位置开始；必须为非负数 ；
 * length（size） - 要映射的区域大小；必须为非负数且不大于 Integer.MAX_VALUE //这个值你设置得太大了，2个G的文件你全部映射到内存中来了。
 *
 * */