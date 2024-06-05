package com.example.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Класс DatabaseHelper представляет собой вспомогательный класс для работы с базой данных SQLite.
 * Он предоставляет методы для создания, обновления и выполнения операций с таблицей пользователей.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 12; // Версия базы данных

    private static final String DATABASE_NAME = "UserManager.db"; // Имя базы данных
    private static final String TABLE_USERS = "users"; // Имя таблицы пользователей
    private static final String COLUMN_ID = "id"; // Имя столбца ID
    private static final String COLUMN_USERNAME = "username"; // Имя столбца для имени пользователя
    private static final String COLUMN_PASSWORD = "password"; // Имя столбца для пароля пользователя
    private static final String COLUMN_EMAIL = "email"; // Имя столбца для адреса электронной почты пользователя

    // SQL-запрос для создания таблицы пользователей
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_EMAIL + " TEXT" +
                    ")";

    /**
     * Конструктор класса DatabaseHelper.
     *
     * @param context Контекст приложения.
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Метод onCreate вызывается при создании базы данных.
     * Он создает таблицу пользователей.
     *
     * @param db База данных SQLite.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE); // Создание таблицы пользователей
    }

    /**
     * Метод onUpgrade вызывается при обновлении базы данных.
     * Он удаляет старую таблицу пользователей и создает новую таблицу.
     *
     * @param db         База данных SQLite.
     * @param oldVersion Старая версия базы данных.
     * @param newVersion Новая версия базы данных.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Удаляем старую таблицу
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Создаем новую таблицу с добавлением столбца email
        String newTableCreate = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_EMAIL + " TEXT" +
                ")";
        db.execSQL(newTableCreate);
    }

    /**
     * Метод addUser добавляет нового пользователя в базу данных.
     * Хеширует пароль перед сохранением.
     *
     * @param username Имя пользователя.
     * @param password Пароль пользователя.
     * @param email    Адрес электронной почты пользователя.
     * @return true, если пользователь успешно добавлен, в противном случае false.
     */
    public boolean addUser(String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase(); // Получение доступа к базе данных для записи
        ContentValues values = new ContentValues(); // Создание объекта для хранения значений
        values.put(COLUMN_USERNAME, username); // Добавление имени пользователя
        values.put(COLUMN_PASSWORD, hashPassword(password)); // Добавление хешированного пароля
        values.put(COLUMN_EMAIL, email); // Добавление адреса электронной почты
        long result = db.insert(TABLE_USERS, null, values); // Вставка данных в таблицу
        return result != -1; // Возвращение результата операции
    }

    /**
     * Метод checkUser проверяет существование пользователя в базе данных.
     * Хеширует введенный пароль перед сравнением с хранимым значением.
     *
     * @param username Имя пользователя для проверки.
     * @param password Пароль пользователя для проверки.
     * @return true, если пользователь существует и введенный пароль совпадает, в противном случае false.
     */
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase(); // Получение доступа к базе данных для записи
        String hashedPassword = hashPassword(password); // Хеширование введенного пароля
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{username, hashedPassword}); // Выполнение запроса к базе данных
        int count = cursor.getCount(); // Получение количества результатов запроса
        cursor.close(); // Закрытие курсора
        return count > 0; // Возвращение результата операции
    }

    /**
     * Метод checkUserExists проверяет существование пользователя с указанным именем пользователя или адресом электронной почты.
     *
     * @param username Имя пользователя для проверки.
     * @param email    Адрес электронной почты для проверки.
     * @return true, если пользователь существует с указанным именем пользователя или адресом электронной почты, в противном случае false.
     */
    public boolean checkUserExists(String username, String email) {
        SQLiteDatabase db = this.getWritableDatabase(); // Получение доступа к базе данных для записи
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=? OR " + COLUMN_EMAIL + "=?", new String[]{username, email}); // Выполнение запроса к базе данных
        int count = cursor.getCount(); // Получение количества результатов запроса
        cursor.close(); // Закрытие курсора
        return count > 0; // Возвращение результата операции
    }

/**
 * Метод getUserEmail получает адрес электронной почты пользователя по его имени пользователя.
 *
 * @param username Имя пользователя.

 * @return Адрес электронной почты пользователя.
 */
public String getUserEmail(String username) {
    SQLiteDatabase db = this.getReadableDatabase(); // Получение доступа к базе данных для чтения
    String[] columns = {COLUMN_EMAIL}; // Выбор столбца для запроса
    String selection = COLUMN_USERNAME + " = ?"; // Условие выборки данных
    String[] selectionArgs = {username}; // Параметры условия выборки
    Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null); // Выполнение запроса к базе данных
    String email = null; // Инициализация переменной для адреса электронной почты
    if (cursor != null) {
        if (cursor.moveToFirst()) { // Перемещение курсора на первую строку результата
            int columnIndex = cursor.getColumnIndex(COLUMN_EMAIL); // Получение индекса столбца
            if (columnIndex != -1) {
                email = cursor.getString(columnIndex); // Получение значения столбца
            }
        }
        cursor.close(); // Закрытие курсора
    }
    return email; // Возвращение адреса электронной почты пользователя
}

    /**
     * Метод hashPassword хеширует введенный пароль с использованием алгоритма SHA-256.
     *
     * @param password Пароль для хеширования.
     * @return Хешированный пароль в виде строки.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256"); // Получение экземпляра класса для хеширования
            byte[] encodedHash = digest.digest(password.getBytes()); // Вычисление хеша для байтов пароля
            StringBuilder hexString = new StringBuilder(); // Создание объекта для хранения хеша в виде строки
            for (byte b : encodedHash) { // Преобразование каждого байта хеша в строку
                String hex = Integer.toHexString(0xff & b); // Преобразование в шестнадцатеричное представление
                if (hex.length() == 1) hexString.append('0'); // Дополнение нулем, если длина строки равна 1
                hexString.append(hex); // Добавление шестнадцатеричной строки в объект StringBuilder
            }
            return hexString.toString(); // Возвращение хеша в виде строки
        } catch (NoSuchAlgorithmException e) { // Обработка исключения отсутствия алгоритма хеширования
            throw new RuntimeException("Error hashing password", e); // Генерация исключения с сообщением об ошибке
        }
    }
}
