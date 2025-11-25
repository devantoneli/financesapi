package com.example.model;

public class Task {
    private Integer id;
    private String title;
    private boolean done;
    private Integer userId; // FK para User

    public Task() {}

    public Task(String title, boolean done, Integer userId) {
        this.title = title;
        this.done = done;
        this.userId = userId;
    }

    // Getters e setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "Task{title='" + title + "', done=" + done + "}";
    }
}