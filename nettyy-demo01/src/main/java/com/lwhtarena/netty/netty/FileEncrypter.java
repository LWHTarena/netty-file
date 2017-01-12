package com.lwhtarena.netty.netty;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * @author： liwh
 * @Date: 2017/1/9.
 * @Description：<p></P>
 * (1)jdk版本：1.40以上
 * (2)加密背景：文件加密 加密方法：三重des加密
 * (3)加密解密原理:
 *    加密时,对输入的48位秘钥,前两位,中间两位和后44位,分别求md5值,
 *  先用第一个md5值加密,再用第二个md5值加密,最后用第三个md5值加密,,共三重加密,
 *  解密时,对输入的byte数组即加密后的文件, 先用第三个md5值解密,再用第二个md5值解密,再用第一个md5值解密,这样返回的就是解密后的文件
 *  md5值的获取用 java.security 包里的类
 *  加密解密用的是 javax.crypto 包里面的类
 * (4)加密解密过程用法：
 * 在console 输入秘钥  AD67EA2F3BE6E5ADD368DFE03120B5DF92A8FD8FEC2F0746
 * 再输入需要加密的文件名  def.txt(文件路径是相对路径是相对这个的路径  D:\***\EclipseWorkingspace\WorkspaceLearning\jsp_api)
 * 再输入en,即可实现加密
 * 在以上路径下出现加密文件   en_def.txt,
 * 解密过程方法:输入秘钥  AD67EA2F3BE6E5ADD368DFE03120B5DF92A8FD8FEC2F0746
 * 再输入需要解密的文件名  en_def.txt
 * 再输入de,即可实现解密
 */
@Deprecated
public class FileEncrypter {
    /**
     * 加密函数 输入： 要加密的文件，密码（由0-F组成，共48个字符，表示3个16位的密码）如：
     * AD67EA2F3BE6E5ADD368DFE03120B5DF92A8FD8FEC2F0746 其中：
     * AD67EA2F3BE6E5AD  DES密码一      D368DFE03120B5DF DES密码二 92A8FD8FEC2F0746 DES密码三
     * 输出：对输入的文件加密后，保存到同一文件夹下增加了 "en+原文件名" 为扩展名的文件中。
     *
     * param:
     * sKey 是三个md5值的字符串拼接, 一共48位
     */
    private void encrypt(File fileIn, String sKey) {
        try {
            if (sKey.length() == 48) {
                byte[] bytK1 = getKeyByStr(sKey.substring(0, 16));
                byte[] bytK2 = getKeyByStr(sKey.substring(16, 32));
                byte[] bytK3 = getKeyByStr(sKey.substring(32, 48));

                FileInputStream fis = new FileInputStream(fileIn);
                byte[] bytIn = new byte[(int) fileIn.length()];
                for (int i = 0; i < fileIn.length(); i++) {
                    bytIn[i] = (byte) fis.read();
                }
                // 加密
                byte[] bytOut = encryptByDES(encryptByDES(encryptByDES(bytIn,
                        bytK1), bytK2), bytK3);
                String fileOut = "en_"+fileIn.getPath();
                FileOutputStream fos = new FileOutputStream(fileOut);
                for (int i = 0; i < bytOut.length; i++) {
                    fos.write((int) bytOut[i]);
                }
                fos.close();
            } else
                ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解密函数 输入： 要解密的文件，密码（由0-F组成，共48个字符，表示3个16位的密码）如：
     * AD67EA2F3BE6E5ADD368DFE03120B5DF92A8FD8FEC2F0746 其中：
     * AD67EA2F3BE6E5AD  DES密码一 D368DFE03120B5DF DES密码二 92A8FD8FEC2F0746 DES密码三
     * 输出：对输入的文件解密后，保存到用户指定的文件中。
     */
    private void decrypt(File fileIn, String sKey) {
        try {
            if (sKey.length() == 48) {
                String strPath = fileIn.getPath();
                strPath = "de"+strPath.substring(2, strPath.length());
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File("."));
                chooser.setSelectedFile(new File(strPath));
                byte[] bytK1 = getKeyByStr(sKey.substring(0, 16));
                byte[] bytK2 = getKeyByStr(sKey.substring(16, 32));
                byte[] bytK3 = getKeyByStr(sKey.substring(32, 48));

                FileInputStream fis = new FileInputStream(fileIn);
                byte[] bytIn = new byte[(int) fileIn.length()];
                for (int i = 0; i < fileIn.length(); i++) {
                    bytIn[i] = (byte) fis.read();
                }
                // 解密
                byte[] bytOut = decryptByDES(decryptByDES(decryptByDES(bytIn,
                        bytK3), bytK2), bytK1);
                File fileOut = chooser.getSelectedFile();
                fileOut.createNewFile();
                FileOutputStream fos = new FileOutputStream(fileOut);
                for (int i = 0; i < bytOut.length; i++) {
                    fos.write((int) bytOut[i]);
                }
                fos.close();
                System.out.println("解密成功");
            }
        } catch (Exception e) {
            System.out.println("解密错误！");
        }
    }

    /**
     * 用DES方法加密输入的字节 bytKey需为8字节长，是加密的密码
     * param:bytP 文件  , bytKey 密码
     * return:
     */
    private byte[] encryptByDES(byte[] bytP, byte[] bytKey) throws Exception {
        DESKeySpec desKS = new DESKeySpec(bytKey);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey sk = skf.generateSecret(desKS);
        Cipher cip = Cipher.getInstance("DES");
        cip.init(Cipher.ENCRYPT_MODE, sk);
        return cip.doFinal(bytP);
    }

    /**
     * 用DES方法解密输入的字节 bytKey需为8字节长，是解密的密码
     */
    private byte[] decryptByDES(byte[] bytE, byte[] bytKey) throws Exception {
        DESKeySpec desKS = new DESKeySpec(bytKey);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey sk = skf.generateSecret(desKS);
        Cipher cip = Cipher.getInstance("DES");
        cip.init(Cipher.DECRYPT_MODE, sk);
        return cip.doFinal(bytE);
    }

    /**
     * 输入密码的字符形式，返回字节数组形式。 如输入字符串：AD67EA2F3BE6E5AD
     * 返回字节数组：{173,103,234,47,59,230,229,173}
     */
    private byte[] getKeyByStr(String str) {
        byte[] bRet = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            Integer itg = new Integer(16 * getChrInt(str.charAt(2 * i))
                    + getChrInt(str.charAt(2 * i + 1)));
            bRet[i] = itg.byteValue();
        }
        return bRet;
    }

    /**
     * 计算一个16进制字符的10进制值 输入：0-F
     */
    private int getChrInt(char chr) {
        int iRet = 0;
        if (chr == "0".charAt(0))
            iRet = 0;
        if (chr == "1".charAt(0))
            iRet = 1;
        if (chr == "2".charAt(0))
            iRet = 2;
        if (chr == "3".charAt(0))
            iRet = 3;
        if (chr == "4".charAt(0))
            iRet = 4;
        if (chr == "5".charAt(0))
            iRet = 5;
        if (chr == "6".charAt(0))
            iRet = 6;
        if (chr == "7".charAt(0))
            iRet = 7;
        if (chr == "8".charAt(0))
            iRet = 8;
        if (chr == "9".charAt(0))
            iRet = 9;
        if (chr == "A".charAt(0))
            iRet = 10;
        if (chr == "B".charAt(0))
            iRet = 11;
        if (chr == "C".charAt(0))
            iRet = 12;
        if (chr == "D".charAt(0))
            iRet = 13;
        if (chr == "E".charAt(0))
            iRet = 14;
        if (chr == "F".charAt(0))
            iRet = 15;
        return iRet;
    }

    public String md5s(String plainText) {
        String str = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            // System.out.println("result: " + buf.toString());// 32位的加密
            // System.out.println("result: " + buf.toString().substring(8,
            // 24));// 16位的加密
            str = buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    public static void main(String args[]) {
        FileEncrypter da = new FileEncrypter();//相对路径是相对这个的路径  D:\***\EclipseWorkingspace\WorkspaceLearning\jsp_api
        Scanner scan = new Scanner(System.in);
        String pass = scan.next();
        String pass1 = pass.substring(0, 2);
        String pass2 = pass.substring(2, 4);
        String pass3 = pass.substring(4);
        System.out.println(pass1);
        System.out.println(pass2);
        System.out.println(pass3);
        String name = scan.next();
//      System.out.println("加密请输入en, 解密请输入de");
        String method = scan.next();
        if (method.equals("en"))
            da.encrypt(new File(name), da.md5s(pass1) + da.md5s(pass2)
                    + da.md5s(pass3));
        else if(method.equals("de"))
            da.decrypt(new File(name), da.md5s(pass1) + da.md5s(pass2)
                    + da.md5s(pass3));
        else
            System.out.println("input error!");
    }
}
