package com.example.taskmanager;

public class Task {
    private long id;
    private String name;
    private String tags;
    private boolean showInCalendar;

    public Task(String name, String tags, boolean showInCalendar) {
        this.name = name;
        this.tags = tags;
        this.showInCalendar = showInCalendar;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public boolean isShowInCalendar() {
        return showInCalendar;
    }

    public void setShowInCalendar(boolean showInCalendar) {
        this.showInCalendar = showInCalendar;
    }
}
