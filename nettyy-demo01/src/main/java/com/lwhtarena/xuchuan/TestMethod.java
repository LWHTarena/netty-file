package com.lwhtarena.xuchuan;

/**
 * @author： liwh
 * @Date: 2017/2/6.
 * @Description：<p测试类</P>
 */
public class TestMethod {
    public TestMethod() { ///xx/weblogic60b2_win.exe
        try {
            SiteInfoBean bean = new SiteInfoBean("http://localhost/xx/weblogic60b2_win.exe",
                    "L:\\temp", "weblogic60b2_win.exe", 5);
            //SiteInfoBean bean = new SiteInfoBean("http://localhost:8080/down.zip","L:\\temp","weblogic60b2_win.exe", 5);
            SiteFileFetch fileFetch = new SiteFileFetch(bean);
            fileFetch.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TestMethod();
    }
}
