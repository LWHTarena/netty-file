package com.lwhtarena.netty;


public class TestA {

    public static void main(String[] args) {
        System.out.println(100000 / 10);
        System.out.println(34333 * 100 / 34334);

        for (int x = 0; x < 1; x++) {
            ThreadPool.addWork(new PushTask(x));
        }


    }

}
