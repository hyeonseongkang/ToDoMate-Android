package com.mirror.todomate_android.classes;

public class Todo {
    private String key;
    private String nickName;
    private String date;
    private String content;

    public Todo() {}

    public Todo(String key, String nickName, String date, String content) {
        this.key = key;
        this.nickName = nickName;
        this.date = date;
        this.content = content;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getNickName() {
        return nickName;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }
}
