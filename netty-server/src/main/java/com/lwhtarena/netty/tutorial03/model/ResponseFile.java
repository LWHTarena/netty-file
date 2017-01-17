package com.lwhtarena.netty.tutorial03.model;

import java.io.File;

/**
 * @author： liwh
 * @Date: 2016/11/17.
 * @Description：
 */
public class ResponseFile {


    public ResponseFile(){

    }

    public ResponseFile(long start, String file_md5,long progress) {
        super();
        this.start = start;
        this.file_md5 = file_md5;
        this.end = false;
        this.progress = (int)progress;
    }

    public ResponseFile(long start, String file_md5, String file_url) {
        super();
        this.start = start;
        this.file_md5 = file_md5;
        this.file_url = file_url;
        this.end = true;
        this.progress = 100;
    }


    /**
     * 开始 读取点
     */
    private long start;
    /**
     * 文件的 MD5值
     */
    private String file_md5;
    /**
     * 文件下载地址
     */
    private String file_url;
    /**
     * 上传是否结束
     */
    private boolean end;
    /**
     * 进度
     */
    private int progress ;

    private File file; //文件

    private String file_name;// 文件名

    private String file_type;  //文件类型


    public long getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getFile_md5() {
        return file_md5;
    }

    public void setFile_md5(String file_md5) {
        this.file_md5 = file_md5;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }
}
