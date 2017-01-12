package com.lwhtarena.netty.demo02;

import java.io.Serializable;

/**
 * @author： liwh
 * @Date: 2017/1/12.
 * @Description：<p>netty 传输序列化对象</P>
 */
public class User implements Serializable {

    private Long id;
    private String username;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
