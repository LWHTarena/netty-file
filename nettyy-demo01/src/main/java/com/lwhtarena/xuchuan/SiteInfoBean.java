package com.lwhtarena.xuchuan;

/**
 * @author： liwh
 * @Date: 2017/2/6.
 * @Description：<p>要抓取的文件的信息，如文件保存的目录，名字，抓取文件的 URL 等</P>
 */
public class SiteInfoBean {
    private String sSiteURL; //Site's URL
    private String sFilePath; //Saved File's Path
    private String sFileName; //Saved File's Name
    private int nSplitter; //Count of Splited Downloading File

    public SiteInfoBean() {
        //default value of nSplitter is 5
        this("", "", "", 5);
    }

    public SiteInfoBean(String sURL, String sPath, String sName, int nSpiltter) {
        sSiteURL = sURL;
        sFilePath = sPath;
        sFileName = sName;
        this.nSplitter = nSpiltter;
    }

    public String getSSiteURL() {
        return sSiteURL;
    }

    public void setSSiteURL(String value) {
        sSiteURL = value;
    }

    public String getSFilePath() {
        return sFilePath;
    }

    public void setSFilePath(String value) {
        sFilePath = value;
    }

    public String getSFileName() {
        return sFileName;
    }

    public void setSFileName(String value) {
        sFileName = value;
    }

    public int getNSplitter() {
        return nSplitter;
    }

    public void setNSplitter(int nCount) {
        nSplitter = nCount;
    }
}
