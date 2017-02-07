package com.lwhtarena.netty.bigFile.client;

/**
 * @author： liwh
 * @Date: 2017/2/7.
 * @Description：<p></P>
 */
public class MonitorThread implements Runnable{

    public Thread t;

    private String threadName;

    boolean suspended =false;

    public MonitorThread(String threadName) {
        this.threadName = threadName;
        System.out.println("创建线程..."+threadName);
    }

    public void run() {

    }

    /**开始**/
    public void start(){
        if(t==null){
            t =new Thread(this,threadName);
            t.start();
        }
    }

    /**
     * 暂停
     */
    public void suspend(){
        suspended =true;
    }

    synchronized void resume(){
        suspended =false;
        notify();
    }
}
