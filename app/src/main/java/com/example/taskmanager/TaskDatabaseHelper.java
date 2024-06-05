package com.example.taskmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "taskmanager.db";
    private static final int DATABASE_VERSION = 9; // Incremented version

    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TAGS = "tags";
    public static final String COLUMN_SHOW_IN_CALENDAR = "show_in_calendar";
    public static final String COLUMN_NOTIFY = "notify";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_IS_COMPLETED = "is_completed"; // New column for task completion status

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_TASKS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_TAGS + " TEXT, " +
                    COLUMN_SHOW_IN_CALENDAR + " INTEGER, " +
                    COLUMN_NOTIFY + " INTEGER, " +
                    COLUMN_DATE + " INTEGER, " +  // Добавлено поле для хранения даты
                    COLUMN_IS_COMPLETED + " INTEGER" + // New column for task completion status
                    ");";


    public TaskDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add new columns if upgrading from version 1 to version 2
            db.execSQL("ALTER TABLE " + TABLE_TASKS + " ADD COLUMN " + COLUMN_IS_COMPLETED + " INTEGER DEFAULT 0");
        }
        // Handle other upgrades if needed
    }

}
