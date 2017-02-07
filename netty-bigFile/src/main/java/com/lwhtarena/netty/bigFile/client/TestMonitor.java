package com.lwhtarena.netty.bigFile.client;

import java.io.IOException;

/**
 * @author： liwh
 * @Date: 2017/2/7.
 * @Description：<p></P>
 */
public class TestMonitor {
    public static void main(String[] args) {
        MonitorThread mt =new MonitorThread("监控线程");

        mt.start();

        while (true){
            int r =0;
            try {
                r =System.in.read();
            }catch (IOException e){
                e.printStackTrace();
            }

            if(r =='q'){
                mt.suspend();
            }
            if(r =='n'){
                mt.resume();
            }
        }
    }
}
