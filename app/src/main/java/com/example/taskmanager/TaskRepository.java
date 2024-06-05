package com.example.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// Класс репозитория задач для управления данными в базе данных
public class TaskRepository {
    private TaskDatabaseHelper dbHelper; // Вспомогательный класс для работы с базой данных

    // Конструктор, инициализирующий dbHelper
    public TaskRepository(Context context) {
        dbHelper = new TaskDatabaseHelper(context);
    }

    // Метод для добавления новой задачи в базу данных
    public void addTask(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Получаем базу данных для записи

        // Создаем объект ContentValues для хранения значений полей задачи
        ContentValues values = new ContentValues();
        values.put(TaskDatabaseHelper.COLUMN_NAME, task.getName()); // Добавляем название задачи
        values.put(TaskDatabaseHelper.COLUMN_DESCRIPTION, task.getDescription()); // Добавляем описание задачи
        values.put(TaskDatabaseHelper.COLUMN_TAGS, task.getTags()); // Добавляем теги задачи
        values.put(TaskDatabaseHelper.COLUMN_SHOW_IN_CALENDAR, task.isShowInCalendar() ? 1 : 0); // Добавляем флаг отображения в календаре
        values.put(TaskDatabaseHelper.COLUMN_NOTIFY, task.isNotify() ? 1 : 0); // Добавляем флаг уведомления
        values.put(TaskDatabaseHelper.COLUMN_DATE, task.getDate().getTimeInMillis()); // Сохраняем дату в миллисекундах
        values.put(TaskDatabaseHelper.COLUMN_IS_COMPLETED, task.isCompleted() ? 1 : 0); // Добавляем статус завершенности задачи

        // Вставляем значения в таблицу задач
        db.insert(TaskDatabaseHelper.TABLE_TASKS, null, values);
        db.close(); // Закрываем базу данных
    }

    // Метод для получения всех задач
    public List<Task> getAllTasks() {
        return getTasksForDate(null); // Возвращаем все задачи без фильтрации по дате
    }

    // Метод для получения задач по конкретной дате
    public List<Task> getTasksForDate(Long dateMillis) {
        List<Task> tasks = new ArrayList<>(); // Создаем список для хранения задач
        SQLiteDatabase db = dbHelper.getReadableDatabase(); // Получаем базу данных для чтения

        String selection = null; // Строка для хранения условий выборки
        String[] selectionArgs = null; // Массив для хранения аргументов выборки
        if (dateMillis != null) {
            // Если дата указана, фильтруем задачи по дате
            selection = TaskDatabaseHelper.COLUMN_DATE + " = ?";
            selectionArgs = new String[]{String.valueOf(dateMillis)};
        }

        // Выполняем запрос к базе данных
        Cursor cursor = db.query(TaskDatabaseHelper.TABLE_TASKS, null, selection, selectionArgs, null, null, null);

        // Получаем задачи из курсора
        tasks = getTasksFromCursor(cursor);

        db.close(); // Закрываем базу данных
        return tasks; // Возвращаем список задач
    }

    // Метод для удаления всех задач
    public void deleteAllTasks() {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Получаем базу данных для записи
        db.delete(TaskDatabaseHelper.TABLE_TASKS, null, null); // Удаляем все записи из таблицы задач
        db.close(); // Закрываем базу данных
    }

    // Метод для получения задач из курсора
    private List<Task> getTasksFromCursor(Cursor cursor) {
        List<Task> tasks = new ArrayList<>(); // Создаем список для хранения задач
        if (cursor.moveToFirst()) { // Проверяем, есть ли данные в курсоре
            do {
                // Получаем значения полей из курсора
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_DESCRIPTION));
                String tags = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_TAGS));
                boolean showInCalendar = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_SHOW_IN_CALENDAR)) == 1;
                boolean notify = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_NOTIFY)) == 1;
                boolean isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_IS_COMPLETED)) == 1;
                long dateMillis = cursor.getLong(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_DATE));

                // Создаем объект Task с полученными значениями
                Task task = new Task(name, description, tags, showInCalendar, notify);
                task.setId(id); // Устанавливаем ID задачи
                task.setCompleted(isCompleted); // Устанавливаем статус завершенности
                Calendar date = Calendar.getInstance(); // Создаем объект Calendar
                date.setTimeInMillis(dateMillis); // Устанавливаем дату задачи
                task.setDate(date); // Устанавливаем дату в задаче
                tasks.add(task); // Добавляем задачу в список

            } while (cursor.moveToNext()); // Переходим к следующей записи в курсоре
        }
        cursor.close(); // Закрываем курсор
        return tasks; // Возвращаем список задач
    }

    // Метод для обновления задачи
    public void updateTask(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Получаем базу данных для записи

        // Создаем объект ContentValues для хранения значений полей задачи
        ContentValues values = new ContentValues();
        values.put(TaskDatabaseHelper.COLUMN_NAME, task.getName()); // Обновляем название задачи
        values.put(TaskDatabaseHelper.COLUMN_DESCRIPTION, task.getDescription()); // Обновляем описание задачи
        values.put(TaskDatabaseHelper.COLUMN_TAGS, task.getTags()); // Обновляем теги задачи
        values.put(TaskDatabaseHelper.COLUMN_SHOW_IN_CALENDAR, task.isShowInCalendar() ? 1 : 0); // Обновляем флаг отображения в календаре
        values.put(TaskDatabaseHelper.COLUMN_NOTIFY, task.isNotify() ? 1 : 0); // Обновляем флаг уведомления
        values.put(TaskDatabaseHelper.COLUMN_DATE, task.getDate().getTimeInMillis()); // Обновляем дату в миллисекундах
        values.put(TaskDatabaseHelper.COLUMN_IS_COMPLETED, task.isCompleted() ? 1 : 0); // Обновляем статус завершенности задачи

        // Обновляем запись в таблице задач по ID задачи
        db.update(TaskDatabaseHelper.TABLE_TASKS, values, TaskDatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(task.getId())});
        db.close(); // Закрываем базу данных
    }
}
