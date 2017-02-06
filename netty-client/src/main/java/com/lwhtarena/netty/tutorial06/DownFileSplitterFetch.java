package com.lwhtarena.netty.tutorial06;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author： liwh
 * @Date: 2017/2/6.
 * @Description：<p>下载上传子线程</P>
 */
public class DownFileSplitterFetch extends Thread{
    String sURL; // 下载文件的地址
    long nStartPos; // 文件分段的开始位置
    long nEndPos; // 文件分段的结束位置
    int nThreadID; // 线程的 ID
    boolean bDownOver = false; // 是否下载完成
    boolean bStop = false; // 停止下载
    DownFileAccess fileAccessI = null; // 文件对象
    boolean fileflag; //是URL下载还是本地下载
    File file = null;//本地下载文件
    boolean bFirst = true;

    /**
     * 下载，上传子线程初始化
     * @param sURL
     * @param sName
     * @param nStart
     * @param nEnd
     * @param id
     * @param fileflag
     * @param downfile
     * @throws IOException
     */
    public DownFileSplitterFetch(String sURL, String sName, long nStart, long nEnd,
                                 int id,boolean fileflag,File downfile,boolean bFirst) throws IOException {
        this.sURL = sURL;
        this.nStartPos = nStart;
        this.nEndPos = nEnd;
        nThreadID = id;
        fileAccessI = new DownFileAccess(sName, nStartPos,bFirst);
        this.fileflag = fileflag;
        this.file = downfile;
        this.bFirst = bFirst;
    }

    /**
     * 线程执行
     */
    public void run() {
        if(fileflag){
            this.urldownload();
        }else{
            this.filedownload();
        }
    }

    /**
     * 打印回应的头信息
     * @param con
     */
    public void logResponseHead(HttpURLConnection con) {
        for (int i = 1;; i++) {
            String header = con.getHeaderFieldKey(i);
            if (header != null){
                DownFileUtility.log(header + " : " + con.getHeaderField(header));
            }else{
                break;
            }
        }
    }

    /**
     * 地址下载
     */
    private void urldownload(){
        DownFileUtility.log("Thread " + nThreadID + " url down filesize is "+(nEndPos-nStartPos));
        DownFileUtility.log("Thread " + nThreadID + " url start >> "+nStartPos +"------end >> "+nEndPos);
        while (nStartPos < nEndPos && !bStop) {
            try {
                URL url = new URL(sURL);
                HttpURLConnection httpConnection = (HttpURLConnection) url
                        .openConnection();
                httpConnection.setRequestProperty("User-Agent", "NetFox");
                String sProperty = "bytes=" + nStartPos + "-";
                httpConnection.setRequestProperty("RANGE", sProperty);
                DownFileUtility.log(sProperty);
                InputStream input = httpConnection.getInputStream();
                byte[] b = new byte[1024];
                int nRead;
                while ((nRead = input.read(b, 0, 1024)) > 0
                        && nStartPos < nEndPos && !bStop) {
                    if((nStartPos+nRead)>nEndPos)
                    {
                        nRead = (int)(nEndPos - nStartPos);
                    }
                    nStartPos += fileAccessI.write(b, 0, nRead);
                }
                DownFileUtility.log("Thread " + nThreadID + " nStartPos : "+nStartPos);
                fileAccessI.oSavedFile.close();
                DownFileUtility.log("Thread " + nThreadID + " is over!");
                input.close();
                bDownOver = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(!bDownOver){
            if(nStartPos >= nEndPos){
                bDownOver = true;
            }
        }
    }

    /**
     * 文件下载
     */
    private void filedownload(){
        DownFileUtility.log("Thread " + nThreadID + " down filesize is "+(nEndPos-nStartPos));
        DownFileUtility.log("Thread " + nThreadID + " start >> "+nStartPos +"------end >> "+nEndPos);
        while (nStartPos < nEndPos && !bStop) {
            try {
                RandomAccessFile input = new RandomAccessFile(file,"r");
                input.seek(nStartPos);
                byte[] b = new byte[1024];
                int nRead;
                while ((nRead = input.read(b, 0, 1024)) > 0
                        && nStartPos < nEndPos && !bStop) {
                    if((nStartPos+nRead)>nEndPos)
                    {
                        nRead = (int)(nEndPos - nStartPos);
                    }
                    nStartPos += fileAccessI.write(b, 0, nRead);
                }
                fileAccessI.oSavedFile.close();
                DownFileUtility.log("Thread " + nThreadID + " is over!");
                input.close();
                bDownOver = true;
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(!bDownOver){
            if(nStartPos >= nEndPos){
                bDownOver = true;
            }
        }
        DownFileUtility.log("Thread " + nThreadID + "last start >> "+nStartPos );
    }

    /**
     * 停止
     */
    public void splitterStop() {
        bStop = true;
    }
}
