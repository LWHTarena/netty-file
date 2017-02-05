package com.lwhtarena.netty.tutorial02.thread02;

import io.netty.util.Constant;

import java.util.Scanner;

/**
 * @author： liwh
 * @Date: 2017/2/5.
 * @Description：<p>测试线程！</P>
 */
public class TestThread {

    public static void main(String[] args) {
        ReadThread rt =new ReadThread("ceshi001");
        ControllerThread ct =new ControllerThread(rt);

        rt.start();
        ct.run();
    }
}
