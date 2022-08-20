package com.mirror.todomate_android.classes;

public class Todo {
    private String key;
    private String email;
    private String date;
    private String content;

    public Todo() {}

    public Todo(String key, String email, String date, String content) {
        this.key = key;
        this.email = email;
        this.date = date;
        this.content = content;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getEmail() {
        return email;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }
}
