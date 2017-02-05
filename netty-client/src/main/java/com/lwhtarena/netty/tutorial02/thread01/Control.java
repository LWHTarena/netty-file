package com.lwhtarena.netty.tutorial02.thread01;

import java.io.IOException;

/**
 * @author： liwh
 * @Date: 2017/2/5.
 * @Description：<p></P>
 */
public class Control extends Thread {
    private Thread1 t;
    public Control(Thread1 t) {
        this.t = t;
    }

    public void run() {
        while(true) {
            int r=0;
            try {
                r=System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(r == 'g') {
                t.setSleep(false);
            } else if(r == 'w') {
                t.setSleep(true);
            } else if(r == 's') {
                t.setStop(true);
            }
        }
    }
}
