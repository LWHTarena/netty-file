package com.lwhtarena.netty.tutorial02.thread02;

/**
 * @author： liwh
 * @Date: 2017/2/5.
 * @Description：<p></P>
 */
public class ContinueThread {
    public static void main(String[] args) {



        ReadThread rt =new ReadThread("ceshi001");
        System.out.println("======================================-----暂停线程: ");
        rt.resume();
    }
}
