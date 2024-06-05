
package com.example.taskmanager;

import java.util.Calendar;

public class Task {
    private long id;
    private String name;
    private String description;
    private String tags;
    private Calendar date; // Изменили тип поля на Calendar
    private boolean showInCalendar;
    private boolean notify;
    private boolean isCompleted;
    private boolean descriptionVisible;
    private boolean dateTimeVisible;

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

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }


}