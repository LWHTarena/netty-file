package com.lwhtarena.netty.tutorial02;

import java.io.RandomAccessFile;

/**
 * @author： liwh
 * @Date: 2017/1/13.
 * @Description：<p>程序功能：演示了RandomAccessFile类的操作，同时实现了一个文件复制操作。</P>
 */
public class RandomAccessFileDemo02 {

    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile("file", "rw");
        // 以下向file文件中写数据
        file.writeInt(20);// 占4个字节
        file.writeDouble(8.236598);// 占8个字节
        file.writeUTF("这是一个UTF字符串");// 这个长度写在当前文件指针的前两个字节处，可用readShort()读取
        file.writeBoolean(true);// 占1个字节
        file.writeShort(395);// 占2个字节
        file.writeLong(2325451l);// 占8个字节
        file.writeUTF("又是一个UTF字符串");
        file.writeFloat(35.5f);// 占4个字节
        file.writeChar('a');// 占2个字节

        file.seek(0);// 把文件指针位置设置到文件起始处

        // 以下从file文件中读数据，要注意文件指针的位置
        System.out.println("——————从file文件指定位置读数据——————");
        System.out.println(file.readInt());
        System.out.println(file.readDouble());
        System.out.println(file.readUTF());

        file.skipBytes(3);// 将文件指针跳过3个字节，本例中即跳过了一个boolean值和short值。
        System.out.println(file.readLong());

        file.skipBytes(file.readShort()); // 跳过文件中“又是一个UTF字符串”所占字节，注意readShort()方法会移动文件指针，所以不用加2。
        System.out.println(file.readFloat());

        //以下演示文件复制操作
        System.out.println("——————文件复制（从file到fileCopy）——————");
        file.seek(0);
        RandomAccessFile fileCopy=new RandomAccessFile("fileCopy","rw");
        int len=(int)file.length();//取得文件长度（字节数）
        byte[] b=new byte[len];
        file.readFully(b);
        fileCopy.write(b);
        System.out.println("复制完成！");
    }

    /**
     * <p>RandomAccessFile 插入写示例：</p>
     *
     * @param skip 跳过多少过字节进行插入数据
     * @param str 要插入的字符串
     * @param fileName 文件路径
     */
    public static void beiju(long skip, String str, String fileName){
        try {
            RandomAccessFile raf = new RandomAccessFile(fileName,"rw");
            if(skip <  0 || skip > raf.length()){
                System.out.println("跳过字节数无效");
                return;
            }
            byte[] b = str.getBytes();
            raf.setLength(raf.length() + b.length);
            for(long i = raf.length() - 1; i > b.length + skip - 1; i--){
                raf.seek(i - b.length);
                byte temp = raf.readByte();
                raf.seek(i);
                raf.writeByte(temp);
            }
            raf.seek(skip);
            raf.write(b);
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
