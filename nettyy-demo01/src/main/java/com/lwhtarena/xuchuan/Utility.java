package com.lwhtarena.xuchuan;

/**
 * @author： liwh
 * @Date: 2017/2/6.
 * @Description：<p>工具类，放一些简单的方法</P>
 */
public class Utility {
    public Utility() {
    }

    public static void sleep(int nSecond) {
        try {
            Thread.sleep(nSecond);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void log(String sMsg) {
        System.err.println(sMsg);
    }

    public static void log(int sMsg) {
        System.err.println(sMsg);
    }
}
