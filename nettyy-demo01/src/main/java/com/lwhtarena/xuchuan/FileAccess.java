package com.lwhtarena.xuchuan;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author： liwh
 * @Date: 2017/2/6.
 * @Description：<p>负责文件的存储</P>
 */
public class FileAccess {
    RandomAccessFile oSavedFile;
    long nPos;

    public FileAccess() throws IOException {
        this("", 0);
    }

    public FileAccess(String sName, long nPos) throws IOException {
        oSavedFile = new RandomAccessFile(sName, "rw");
        this.nPos = nPos;
        oSavedFile.seek(nPos);
    }

    public synchronized int write(byte[] b, int nStart, int nLen) {
        int n = -1;
        try {
            oSavedFile.write(b, nStart, nLen);
            n = nLen;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return n;
    }
}
