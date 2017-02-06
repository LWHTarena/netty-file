package com.lwhtarena.netty.tutorial06;

import java.io.File;

/**
 * @author： liwh
 * @Date: 2017/2/6.
 * @Description：<p></P>
 */
public class TestMethod {
    public TestMethod() {
        try {
            //DownFileInfoBean bean = new DownFileInfoBean(
            //        "D:/lwhSpaces/TemplateRepo/Centos6.7bit64.xva", "S:\\TEMP",
            //        "Centos6.7bit64.xva", 5,true,null);

            /*----========本地下载=========----*/
            File file = new File("D:\\lwhSpaces\\TemplateRepo\\Centos7.1bit64.xva");
            DownFileInfoBean bean = new DownFileInfoBean(null, "S:\\TEMP",
                    "Centos6.7bit64.xva", 5,false,file);
            DownFileFetch fileFetch = new DownFileFetch(bean);
            fileFetch.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TestMethod();
    }
}
