package com.mimi.mimigroup.model;

import java.io.Serializable;

public class SystemParam implements Serializable {
    public int timerSync;
    public String keyEncrypt;
    public int scope;
    public int dayClear;
    public String adminPass;
    public int isActive;

    public SystemParam(int timerSync, String keyEncrypt, int scope,
                       int dayClear, String adminPass,
                       int isActive) {
        this.timerSync = timerSync;
        this.keyEncrypt = keyEncrypt;
        this.scope = scope;
        this.dayClear = dayClear;
        this.adminPass = adminPass;
        this.isActive = isActive;
    }
}
