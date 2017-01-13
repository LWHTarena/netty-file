package com.lwhtarena.netty.tutorial02;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author： liwh
 * @Date: 2017/1/13.
 * @Description：<p>RandomAccessFile是用来访问那些保存数据记录的文件的，你就
 * 可以用seek( )方法来访问记录，并进行读写了。这些记录的大小不必相同；但是其大小和
 * 位置必须是可知的。但是该类仅限于操作文件。</P>
 */
public class RandomAccessFileDemo01 {
    public static void main(String[] args) throws IOException {
        RandomAccessFile rf = new RandomAccessFile("rtest.dat", "rw");
        for (int i = 0; i < 10; i++) {
            //写入基本类型double数据
            rf.writeDouble(i * 1.414);
        }
        rf.close();
        rf = new RandomAccessFile("rtest.dat", "rw");
        //直接将文件指针移到第5个double数据后面
        rf.seek(5 * 8);
        //覆盖第6个double数据
        rf.writeDouble(47.0001);
        rf.close();
        rf = new RandomAccessFile("rtest.dat", "r");
        for (int i = 0; i < 10; i++) {
            System.out.println("Value " + i + ": " + rf.readDouble());
        }
        rf.close();
    }
}
