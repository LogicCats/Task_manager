package com.example.taskmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "taskmanager.db"; // Имя базы данных
    private static final int DATABASE_VERSION = 9; // Версия базы данных (увеличивается при изменениях)

    public static final String TABLE_TASKS = "tasks"; // Имя таблицы для задач
    public static final String COLUMN_ID = "_id"; // Имя столбца с идентификатором задачи
    public static final String COLUMN_NAME = "name"; // Имя столбца с названием задачи
    public static final String COLUMN_DESCRIPTION = "description"; // Имя столбца с описанием задачи
    public static final String COLUMN_TAGS = "tags"; // Имя столбца с тегами задачи
    public static final String COLUMN_SHOW_IN_CALENDAR = "show_in_calendar"; // Имя столбца с отображением в календаре
    public static final String COLUMN_NOTIFY = "notify"; // Имя столбца с оповещением о задаче
    public static final String COLUMN_DATE = "date"; // Имя столбца с датой выполнения задачи
    public static final String COLUMN_IS_COMPLETED = "is_completed"; // Новый столбец для статуса завершения задачи

    private static final String TABLE_CREATE = // SQL запрос для создания таблицы
            "CREATE TABLE " + TABLE_TASKS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_TAGS + " TEXT, " +
                    COLUMN_SHOW_IN_CALENDAR + " INTEGER, " +
                    COLUMN_NOTIFY + " INTEGER, " +
                    COLUMN_DATE + " INTEGER, " +  // Добавлено поле для хранения даты
                    COLUMN_IS_COMPLETED + " INTEGER" + // Новый столбец для статуса завершения задачи
                    ");";


    public TaskDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); // Вызов конструктора родительского класса
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE); // Создание таблицы при первом запуске приложения
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) { // Обработка обновлений базы данных
            // Добавление новых столбцов при обновлении с версии 1 до версии 2
            db.execSQL("ALTER TABLE " + TABLE_TASKS + " ADD COLUMN " + COLUMN_IS_COMPLETED + " INTEGER DEFAULT 0");
        }
        // Обработка других обновлений, если необходимо
    }

}
