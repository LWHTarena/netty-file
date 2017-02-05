package com.lwhtarena.netty.tutorial02.thread01;

/**
 * @author： liwh
 * @Date: 2017/2/5.
 * @Description：<p>
 *
程序说明：
    1，刚开始程序的等待的。
    2，你输入‘g’回车后会运行。
    3，你输入‘w’回车后会再次等待。
    4，再次输入‘g’回车后又会运行。
    5，输入‘s'回车，会终止程序。
    6，这里将控制线程设置成了Deamon的形式，因为线程t由线程c控制可以终止，而线程c始终无法终止，所以把它设置
    为后台线程，当让控制的线程t退出时，所有的前台线程都结束了，这样线程c就可以自动退出。
 *     </P>
 */
public class TestThread {
    public static void main(String[] args) {
        Thread1 t = new Thread1();
        Thread c = new Control(t);
        t.setSleep(true);
        c.setDaemon(true);

        t.start();
        c.start();
        System.out.println("You can input 'g' and 'Enter' to start your job.");
        System.out.println("You can input 'w' and 'Enter' to let your job to wait...");
        System.out.println("You can input 's' and 'Enter' to finish your job.");
    }
}
