package com.lwhtarena.netty.tutorial02.thread01;

/**
 * @author： liwh
 * @Date: 2017/2/5.
 * @Description：<p></P>
 */
public class Thread1 extends Thread {
    private boolean isSleep = true;
    private boolean isStop = false;

    public void run() {
        while(!isStop) {
            if(isSleep) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Thread: "+Thread.currentThread().getName() + " do someting.");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Thread: "+Thread.currentThread().getName() + " finished.");
    }

    public void setSleep(boolean sleep) {
        this.isSleep = sleep;
    }
    public void setStop(boolean stop) {
        this.isStop = stop;
    }
}
