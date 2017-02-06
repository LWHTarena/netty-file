package com.lwhtarena.netty.tutorial02.thread02;

import java.io.IOException;

/**
 * @author： liwh
 * @Date: 2017/2/5.
 * @Description：<p>测试线程！</P>
 */
public class TestThread2 {

    public static void main(String[] args) {
        ReadThread rt =new ReadThread("ceshi001");

        System.out.println("======================================-----启动线程: ");
        rt.start();

        while(true) {
            int r=0;
            try {
                r =System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(r =='q'){
                System.out.println("======================================-----暂停线程: ");
                rt.suspend();
            }
            if(r == 'n'){
                System.out.println("======================================-----继续线程: ");
                rt.resume();
            }
        }
    }
}
