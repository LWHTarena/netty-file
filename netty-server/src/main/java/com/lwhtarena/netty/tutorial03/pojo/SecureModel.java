package com.lwhtarena.netty.tutorial03.pojo;

import java.io.Serializable;

/**
 * @author： liwh
 * @Date: 2017/1/16.
 * @Description：<p></P>
 */
public class SecureModel implements Serializable{
    /**
     * 验证 token
     */
    private String token ;

    private boolean autoSuccess;

    public SecureModel() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAutoSuccess() {
        return autoSuccess;
    }

    public void setAutoSuccess(boolean autoSuccess) {
        this.autoSuccess = autoSuccess;
    }
}
