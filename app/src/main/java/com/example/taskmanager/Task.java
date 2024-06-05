
package com.example.taskmanager;

public class Task {
    private long id;
    private String name;
    private String description; // Добавляем описание
    private String tags;

    private long date;
    private boolean showInCalendar;
    private boolean notify; // Добавляем оповещение
    private boolean isCompleted; // Новое поле для отслеживания выполнения задачи


    private boolean descriptionVisible; // добавлено поле для отслеживания видимости описания
    private boolean dateTimeVisible; // добавлено поле для отслеживания видимости даты и времени

    private boolean completed;

    public Task(String name, String tags, String s, boolean showInCalendar, boolean notify, boolean b) {
        this.name = name;
        this.tags = tags;
        this.showInCalendar = showInCalendar;
    }

    // Геттер и сеттер для видимости описания
    public boolean isDescriptionVisible() {
        return descriptionVisible;
    }

    public void setDescriptionVisible(boolean descriptionVisible) {
        this.descriptionVisible = descriptionVisible;
    }

    // Геттер и сеттер для видимости даты и времени
    public boolean isDateTimeVisible() {
        return dateTimeVisible;
    }

    public void setDateTimeVisible(boolean dateTimeVisible) {
        this.dateTimeVisible = dateTimeVisible;
    }

    // Добавляем конструктор для создания задачи с описанием, оповещением и статусом выполнения
    public Task(String name, String description, String tags, boolean showInCalendar, boolean notify) {
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.showInCalendar = showInCalendar;
        this.notify = notify;
        // Устанавливаем isCompleted по умолчанию в false
        this.isCompleted = false;
    }


    // Геттеры и сеттеры для описания, оповещения и статуса выполнения
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

}