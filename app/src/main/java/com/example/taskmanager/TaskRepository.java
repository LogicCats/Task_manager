package com.example.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskRepository {
    private TaskDatabaseHelper dbHelper;

    public TaskRepository(Context context) {
        dbHelper = new TaskDatabaseHelper(context);
    }

    public void addTask(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskDatabaseHelper.COLUMN_NAME, task.getName());
        values.put(TaskDatabaseHelper.COLUMN_DESCRIPTION, task.getDescription());
        values.put(TaskDatabaseHelper.COLUMN_TAGS, task.getTags());
        values.put(TaskDatabaseHelper.COLUMN_SHOW_IN_CALENDAR, task.isShowInCalendar() ? 1 : 0);
        values.put(TaskDatabaseHelper.COLUMN_NOTIFY, task.isNotify() ? 1 : 0);
        values.put(TaskDatabaseHelper.COLUMN_DATE, task.getDate().getTimeInMillis()); // Сохраняем дату в миллисекундах
        values.put(TaskDatabaseHelper.COLUMN_IS_COMPLETED, task.isCompleted() ? 1 : 0);

        db.insert(TaskDatabaseHelper.TABLE_TASKS, null, values);
        db.close();
    }

    public List<Task> getAllTasks() {
        return getTasksForDate(null); // Возвращаем все задачи без фильтрации по дате
    }

    public List<Task> getTasksForDate(Long dateMillis) {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = null;
        String[] selectionArgs = null;
        if (dateMillis != null) {
            // Фильтруем задачи по дате
            selection = TaskDatabaseHelper.COLUMN_DATE + " = ?";
            selectionArgs = new String[]{String.valueOf(dateMillis)};
        }

        Cursor cursor = db.query(TaskDatabaseHelper.TABLE_TASKS, null, selection, selectionArgs, null, null, null);

        tasks = getTasksFromCursor(cursor);

        db.close();
        return tasks;
    }

    public void deleteAllTasks() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TaskDatabaseHelper.TABLE_TASKS, null, null);
        db.close();
    }

    private List<Task> getTasksFromCursor(Cursor cursor) {
        List<Task> tasks = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_DESCRIPTION));
                String tags = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_TAGS));
                boolean showInCalendar = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_SHOW_IN_CALENDAR)) == 1;
                boolean notify = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_NOTIFY)) == 1;
                boolean isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_IS_COMPLETED)) == 1;
                long dateMillis = cursor.getLong(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_DATE));
                Task task = new Task(name, description, tags, showInCalendar, notify);
                task.setId(id);
                task.setCompleted(isCompleted);
                Calendar date = Calendar.getInstance();
                date.setTimeInMillis(dateMillis);
                task.setDate(date);
                tasks.add(task);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return tasks;
    }

    public void updateTask(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskDatabaseHelper.COLUMN_NAME, task.getName());
        values.put(TaskDatabaseHelper.COLUMN_DESCRIPTION, task.getDescription());
        values.put(TaskDatabaseHelper.COLUMN_TAGS, task.getTags());
        values.put(TaskDatabaseHelper.COLUMN_SHOW_IN_CALENDAR, task.isShowInCalendar() ? 1 : 0);
        values.put(TaskDatabaseHelper.COLUMN_NOTIFY, task.isNotify() ? 1 : 0);
        values.put(TaskDatabaseHelper.COLUMN_DATE, task.getDate().getTimeInMillis()); // Сохраняем дату в миллисекундах
        values.put(TaskDatabaseHelper.COLUMN_IS_COMPLETED, task.isCompleted() ? 1 : 0);

        db.update(TaskDatabaseHelper.TABLE_TASKS, values, TaskDatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(task.getId())});
        db.close();
    }
}
