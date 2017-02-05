package com.lwhtarena.netty.tutorial02.thread02;

/**
 * @author： liwh
 * @Date: 2017/2/5.
 * @Description：<p>java 线程的开始、暂停、继续</P>
 * <p>
 *     一个需求：通过线程读取文件内容，并且可以控制线程的开始、暂停、继续，来控制读文件。在此记录下。

　　直接在主线程中，通过wait、notify、notifyAll去控制读文件的线程（子线程），报错：java.lang.IllegalMonitorStateException。

　　需要注意的几个问题：

　　　　1.任何一个时刻，对象的控制权（monitor）只能被一个线程拥有。

　　　　2.无论是执行对象的wait、notify还是notifyAll方法，必须保证当前运行的线程取得了该对象的控制权（monitor）。

　　　　3.如果在没有控制权的线程里执行对象的以上三种方法，就会报错java.lang.IllegalMonitorStateException。

　　　　4.JVM基于多线程，默认情况下不能保证运行时线程的时序性。

　　线程取得控制权的3种方法：

　　　　1.执行对象的某个同步实例方法。

　　　　2.执行对象对应类的同步静态方法。

　　　　3.执行对该对象加同步锁的同步块。

　　这里将开始、暂停、继续封装在线程类中，直接调用该实例的方法就行。
 * </p>
 */
public class ReadThread implements Runnable{

    public Thread t;
    private String threadName;
    boolean suspended =false;

    public ReadThread(String threadName) {
        this.threadName = threadName;
        System.out.println("创建线程... ： "+threadName);
    }

    @Override
    public void run() {
        for (int i=100;i>0;i--){
            System.out.println("线程： "+threadName+" , "+i);
            try {
                Thread.sleep(300);
                synchronized (this){
                    while (suspended){
                        wait();
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Thread " +  threadName + " interrupted.");
                e.printStackTrace();
            }
        }
        System.out.println("Thread " +  threadName + " exiting.");
    }

    /**
     * 开始
     */
    public void start(){
        System.out.println("开始: "+threadName);
        if(t==null){
            t =new Thread(this,threadName);
            t.start();
        }
    }


    /**
     * 暂停
     */
    void suspend(){
        suspended =true;
    }

    /**
     * 继续
     */
    synchronized void resume(){
        suspended =false;
        notify();
    }
}
