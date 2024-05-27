package com.example.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
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
        values.put(TaskDatabaseHelper.COLUMN_TAGS, task.getTags());
        values.put(TaskDatabaseHelper.COLUMN_SHOW_IN_CALENDAR, task.isShowInCalendar() ? 1 : 0);

        db.insert(TaskDatabaseHelper.TABLE_TASKS, null, values);
        db.close();
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(TaskDatabaseHelper.TABLE_TASKS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_NAME));
                String tags = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_TAGS));
                boolean showInCalendar = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_SHOW_IN_CALENDAR)) == 1;

                Task task = new Task(name, tags, showInCalendar);
                task.setId(id);
                tasks.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return tasks;
    }

    // Другие методы для работы с задачами, если необходимо
}
