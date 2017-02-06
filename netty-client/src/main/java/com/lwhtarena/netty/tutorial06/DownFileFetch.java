package com.lwhtarena.netty.tutorial06;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author： liwh
 * @Date: 2017/2/6.
 * @Description：<p>文件断点续传加分段上传线程</P>
 *
 */
public class DownFileFetch extends Thread {
    DownFileInfoBean siteInfoBean = null; // 文件信息 Bean
    long[] nStartPos; // 开始位置
    long[] nEndPos; // 结束位置
    DownFileSplitterFetch[] fileSplitterFetch; // 子线程对象
    long nFileLength; // 文件长度
    boolean bFirst = true; // 是否第一次取文件
    boolean bStop = false; // 停止标志
    File tmpFile; // 文件下载的临时信息
    DataOutputStream output; // 输出到文件的输出流
    boolean fileflag; //是本地上传还是远程下载的标志
    File downfile; //本地文件下载
    int splitter = 0;

    /**
     * 下载上传文件抓取初始化
     * @param bean
     * @throws IOException
     */
    public DownFileFetch(DownFileInfoBean bean) throws IOException {
        siteInfoBean = bean;
        /**
         * File.separator windows是\,unix是/
         */
        tmpFile = new File(bean.getSFilePath() + File.separator + bean.getSFileName() + ".info");
        if (tmpFile.exists()) {
            bFirst = false;
            //读取已下载的文件信息
            read_nPos();
        } else {
            nStartPos = new long[bean.getNSplitter()];
            nEndPos = new long[bean.getNSplitter()];
        }
        fileflag = bean.getFileflag();
        downfile = bean.getDownfile();
        this.splitter = bean.getNSplitter();
    }

    public void run() {
        // 获得文件长度
        // 分割文件
        // 实例 FileSplitterFetch
        // 启动 FileSplitterFetch 线程
        // 等待子线程返回
        try {
            if (bFirst) {
                nFileLength = getFileSize();
                if (nFileLength == -1) {
                    DownFileUtility.log("File Length is not known!");
                } else if (nFileLength == -2) {
                    DownFileUtility.log("File is not access!");
                } else {
                    for (int i = 0; i < nStartPos.length; i++) {
                        nStartPos[i] = (long) (i * (nFileLength / nStartPos.length));
                    }
                    for (int i = 0; i < nEndPos.length - 1; i++) {
                        nEndPos[i] = nStartPos[i + 1];
                    }
                    nEndPos[nEndPos.length - 1] = nFileLength;
                }
            }
            // 启动子线程
            fileSplitterFetch = new DownFileSplitterFetch[nStartPos.length];
            for (int i = 0; i < nStartPos.length; i++) {
                fileSplitterFetch[i] = new DownFileSplitterFetch(
                        siteInfoBean.getSSiteURL(), siteInfoBean.getSFilePath()
                        + File.separator + siteInfoBean.getSFileName()+"_"+i,
                        nStartPos[i], nEndPos[i], i,fileflag,downfile,bFirst);
                DownFileUtility.log("Thread " + i + " , nStartPos = " + nStartPos[i]
                        + ", nEndPos = " + nEndPos[i]);
                fileSplitterFetch[i].start();
            }
            //下载子线程是否完成标志
            boolean breakWhile = false;
            while (!bStop) {
                write_nPos();
                DownFileUtility.sleep(500);
                breakWhile = true;
                for (int i = 0; i < nStartPos.length; i++) {
                    if (!fileSplitterFetch[i].bDownOver) {
                        breakWhile = false;
                        break;
                    }else{
                        write_nPos();
                    }
                }
                if (breakWhile){
                    break;
                }
            }
            hebinfile(siteInfoBean.getSFilePath()+ File.separator + siteInfoBean.getSFileName(),splitter);
            DownFileUtility.log("文件下载结束！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得文件长度
     * @return
     */
    public long getFileSize() {
        int nFileLength = -1;
        if(fileflag){
            try {
                URL url = new URL(siteInfoBean.getSSiteURL());
                HttpURLConnection httpConnection = (HttpURLConnection) url
                        .openConnection();
                httpConnection.setRequestProperty("User-Agent", "NetFox");
                int responseCode = httpConnection.getResponseCode();
                if (responseCode >= 400) {
                    processErrorCode(responseCode);
                    //represent access is error
                    return -2;
                }
                String sHeader;
                for (int i = 1;; i++) {
                    sHeader = httpConnection.getHeaderFieldKey(i);
                    if (sHeader != null) {
                        if (sHeader.equals("Content-Length")) {
                            nFileLength = Integer.parseInt(httpConnection
                                    .getHeaderField(sHeader));
                            break;
                        }
                    } else {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            DownFileUtility.log(nFileLength);
        }else{
            try{
                File myflie = downfile;
                nFileLength = (int)myflie.length();
            }catch(Exception e){
                e.printStackTrace();
            }
            DownFileUtility.log(nFileLength);
        }
        return nFileLength;
    }

    /**
     * 保存下载信息（文件指针位置）
     */
    private void write_nPos() {
        try {
            output = new DataOutputStream(new FileOutputStream(tmpFile));
            output.writeInt(nStartPos.length);
            for (int i = 0; i < nStartPos.length; i++) {
                output.writeLong(fileSplitterFetch[i].nStartPos);
                output.writeLong(fileSplitterFetch[i].nEndPos);
            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取保存的下载信息（文件指针位置）
     */
    private void read_nPos() {
        try {
            DataInputStream input = new DataInputStream(new FileInputStream(
                    tmpFile));
            int nCount = input.readInt();
            nStartPos = new long[nCount];
            nEndPos = new long[nCount];
            for (int i = 0; i < nStartPos.length; i++) {
                nStartPos[i] = input.readLong();
                nEndPos[i] = input.readLong();
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出错误信息
     * @param nErrorCode
     */
    private void processErrorCode(int nErrorCode) {
        DownFileUtility.log("Error Code : " + nErrorCode);
    }

    /**
     * 停止文件下载
     */
    public void siteStop() {
        bStop = true;
        for (int i = 0; i < nStartPos.length; i++)
            fileSplitterFetch[i].splitterStop();
    }

    /**
     * 合并文件
     * @param sName
     * @param splitternum
     */
    private void hebinfile(String sName,int splitternum){
        try{
            File file = new File(sName);
            if(file.exists()){
                file.delete();
            }
            RandomAccessFile saveinput = new RandomAccessFile(sName,"rw");
            for(int i = 0;i<splitternum;i++){
                try {
                    RandomAccessFile input = new RandomAccessFile (new File(sName+"_"+i),"r");
                    byte[] b = new byte[1024];
                    int nRead;
                    while ((nRead = input.read(b, 0, 1024)) > 0) {
                        write(saveinput,b, 0, nRead);
                    }
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            DownFileUtility.log("file size is "+saveinput.length());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 写文件
     * @param b
     * @param nStart
     * @param nLen
     * @return
     */
    private int write(RandomAccessFile oSavedFile,byte[] b, int nStart, int nLen) {
        int n = -1;
        try {
            oSavedFile.seek(oSavedFile.length());
            oSavedFile.write(b, nStart, nLen);
            n = nLen;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return n;
    }


}
