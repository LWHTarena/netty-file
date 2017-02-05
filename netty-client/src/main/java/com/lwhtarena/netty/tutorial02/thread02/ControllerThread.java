package com.lwhtarena.netty.tutorial02.thread02;

import java.io.IOException;

/**
 * @author： liwh
 * @Date: 2017/2/5.
 * @Description：<p></P>
 */
public class ControllerThread implements Runnable {

    private  ReadThread rt;

    public ControllerThread(ReadThread rt) {
        this.rt = rt;
    }

    @Override
    public void run() {
        while (true){

            int r =0;
            try {
                r =System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(r=='g'){ //暂停
                rt.suspend();
            }else if(r == 'w'){ //继续
                rt.resume();
            }
        }

    }


}
