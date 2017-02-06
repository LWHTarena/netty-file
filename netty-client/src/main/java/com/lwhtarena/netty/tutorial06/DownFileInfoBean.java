package com.lwhtarena.netty.tutorial06;

import java.io.File;

/**
 * @author： liwh
 * @Date: 2017/2/6.
 * @Description：<p></P>
 */
public class DownFileInfoBean {
    private String sSiteURL; // 文件的下载地址
    private String sFilePath; // 保存文件的路径
    private String sFileName; // 保存文件的名字
    private int nSplitter; // 文件分成几段，默认是5段
    private boolean fileflag; // 如果为FALSE则是本地下载,为TRUE则URL下载
    private File downfile;

    public File getDownfile() {
        return downfile;
    }

    public void setDownfile(File downfile) {
        this.downfile = downfile;
    }

    public boolean getFileflag() {
        return fileflag;
    }

    public void setFileflag(boolean fileflag) {
        this.fileflag = fileflag;
    }

    /**
     * 默认初始化
     */
    public DownFileInfoBean() {
        // default 5
        this("", "", "", 5,false,null);
    }

    /**
     * 下载文件信息初始化
     * @param sURL 下载的链接地址
     * @param sPath 上传的保存路径
     * @param sName 上传保存的文件名
     * @param nSpiltter 文件分段个数
     * @param fileflag 是本地文件上传下载还是链接上传下载的标志
     * @param downfile 本地下载文件(FILE)
     */
    public DownFileInfoBean(String sURL, String sPath, String sName, int nSpiltter,boolean fileflag,File downfile) {
        sSiteURL = sURL;
        sFilePath = sPath;
        sFileName = sName;
        this.nSplitter = nSpiltter;
        this.fileflag = fileflag;
        this.downfile = downfile;
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
