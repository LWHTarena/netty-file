package com.lwhtarena.netty.tutorial02;

/**
 * @author： liwh
 * @Date: 2017/1/13.
 * @Description：
 * <p>利用RandomAccessFile实现文件的多线程下载，即多线程下载一
 * 个文件时，将文件分成几块，每块用不同的线程进行下载。</P>
 *
 * <p>下面是一个利用多线程在写文件时的例子，其中预先分配文件所需要的空间，
 * 然后在所分配的空间中进行分块，然后写入：</p>
 */


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 测试利用多线程进行文件的写操作
 */
public class TestRandomAccessFile {

    // 利用线程在文件的指定位置写入指定数据
    static class FileWriteThread extends Thread{
        private int skip;
        private byte[] content;

        public FileWriteThread(int skip,byte[] content){
            this.skip = skip;
            this.content = content;
        }

        public void run(){
            RandomAccessFile raf = null;
            try {
                raf = new RandomAccessFile("D://abc.txt", "rw");
                raf.seek(skip);
                raf.write(content);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    raf.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static void main(String[] args) {

        // 预分配文件所占的磁盘空间，磁盘中会创建一个指定大小的文件
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile("D://abc.txt", "rw");
            raf.setLength(1024*1024); // 预分配 1M 的文件空间
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 所要写入的文件内容
        String s1 = "第一个字符串";
        String s2 = "第二个字符串";
        String s3 = "第三个字符串";
        String s4 = "第四个字符串";
        String s5 = "第五个字符串";

        // 利用多线程同时写入一个文件
        new FileWriteThread(1024*1,s1.getBytes()).start(); // 从文件的1024字节之后开始写入数据
        new FileWriteThread(1024*2,s2.getBytes()).start(); // 从文件的2048字节之后开始写入数据
        new FileWriteThread(1024*3,s3.getBytes()).start(); // 从文件的3072字节之后开始写入数据
        new FileWriteThread(1024*4,s4.getBytes()).start(); // 从文件的4096字节之后开始写入数据
        new FileWriteThread(1024*5,s5.getBytes()).start(); // 从文件的5120字节之后开始写入数据
    }
}
