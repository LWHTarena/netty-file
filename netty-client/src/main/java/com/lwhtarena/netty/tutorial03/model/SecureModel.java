package com.lwhtarena.netty.tutorial03.model;

import java.io.Serializable;

/**
 * @author： liwh
 * @Date: 2016/11/17.
 * @Description：
 */
public class SecureModel implements Serializable {

    /**
     * 验证 token
     */
    private String token ;

    private boolean autoSuccess;



 /// ==========>>>>>>>> getter and setter >>>>>>>>>=========
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
