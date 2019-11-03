package com.mimi.mimigroup.model;

public class FlagPost {
    Integer SendPost;
    Integer ReceivePost;


    public FlagPost() {
    }


    public FlagPost(Integer sendPost,Integer receivePost) {
        this.SendPost=sendPost;
        this.ReceivePost=receivePost;
    }

    public Integer getSendPost() {
        return SendPost;
    }
    public void setSendPost(Integer sendPost) {
        SendPost = sendPost;
    }

    public Integer getReceivePost() {
        return ReceivePost;
    }
    public void setReceivePost(Integer receivePost) {
        ReceivePost = receivePost;
    }

}
