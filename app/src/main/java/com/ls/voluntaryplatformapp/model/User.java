package com.ls.voluntaryplatformapp.model;

import java.io.Serializable;

public class User implements Serializable {

    private Long id;
    private String tokenKey;
    private Long expiresTime;
    private boolean isStudent = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public Long getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(Long expiresTime) {
        this.expiresTime = expiresTime;
    }

    public boolean isStudent() {
        return isStudent;
    }

    public void setStudent(boolean student) {
        isStudent = student;
    }
}
