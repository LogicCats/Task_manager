package com.example.taskmanager;

public class Task {
    private long id;
    private String name;
    private String description; // Добавляем описание
    private String tags;
    private boolean showInCalendar;
    private boolean notify; // Добавляем оповещение

    public Task(String name, String tags, boolean showInCalendar) {
        this.name = name;
        this.tags = tags;
        this.showInCalendar = showInCalendar;
    }

    // Добавляем конструктор для создания задачи с описанием и оповещением
    public Task(String name, String description, String tags, boolean showInCalendar, boolean notify) {
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.showInCalendar = showInCalendar;
        this.notify = notify;
    }

    // Геттеры и сеттеры для описания и оповещения
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    // Остальные геттеры и сеттеры остаются без изменений
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
