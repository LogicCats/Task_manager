package com.example.taskmanager;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreationTaskFragment extends Fragment {

    private EditText taskNameEditText; // Поле для ввода названия задачи
    private EditText taskDescriptionEditText; // Поле для ввода описания задачи
    private EditText taskTagsEditText; // Поле для ввода тегов задачи
    private CheckBox showInCalendarCheckBox; // Флажок для выбора отображения задачи в календаре
    private CheckBox notifyCheckBox; // Флажок для выбора уведомления о задаче
    private LinearLayout dateLayout; // Макет для выбора даты
    private LinearLayout timeLayout; // Макет для выбора времени
    private TextView dateTextView; // Текстовое поле для отображения выбранной даты
    private TextView timeTextView; // Текстовое поле для отображения выбранного времени
    private Button createButton; // Кнопка для создания задачи

    private Calendar selectedDateTime; // Выбранная дата и время задачи

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Надуваем макет для этого фрагмента
        View view = inflater.inflate(R.layout.fragment_creation_task, container, false);

        // Инициализация всех элементов пользовательского интерфейса
        taskNameEditText = view.findViewById(R.id.task_name);
        taskDescriptionEditText = view.findViewById(R.id.task_description);
        taskTagsEditText = view.findViewById(R.id.task_tags);
        showInCalendarCheckBox = view.findViewById(R.id.show_in_calendar);
        notifyCheckBox = view.findViewById(R.id.notify);
        dateLayout = view.findViewById(R.id.date_layout);
        timeLayout = view.findViewById(R.id.time_layout);
        dateTextView = view.findViewById(R.id.date_text);
        timeTextView = view.findViewById(R.id.time_text);
        createButton = view.findViewById(R.id.create_button);

        selectedDateTime = Calendar.getInstance(); // Инициализация календаря с текущей датой и временем

        // Установка слушателей для выбора даты и времени
        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(); // Показать диалог выбора даты
            }
        });

        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(); // Показать диалог выбора времени
            }
        });

        // Установка слушателя для кнопки создания задачи
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTask(); // Создать задачу
            }
        });

        // Установка слушателя для флажка отображения задачи в календаре
        showInCalendarCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Сохраняем выбранную дату в SharedPreferences
                    saveSelectedDate(selectedDateTime.getTimeInMillis());
                }
            }
        });

        return view; // Возвращаем созданный вид
    }

    // Метод для отображения диалога выбора даты
    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedDateTime.set(Calendar.YEAR, year); // Установка года
                selectedDateTime.set(Calendar.MONTH, monthOfYear); // Установка месяца
                selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth); // Установка дня месяца
                updateDateText(); // Обновление текста выбранной даты
            }
        }, selectedDateTime.get(Calendar.YEAR), selectedDateTime.get(Calendar.MONTH), selectedDateTime.get(Calendar.DAY_OF_MONTH)); // Установка текущей даты
        datePickerDialog.show(); // Показать диалог выбора даты
    }

    // Метод для отображения диалога выбора времени
    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
                selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay); // Установка часов
                selectedDateTime.set(Calendar.MINUTE, minute); // Установка минут
                updateTimeText(); // Обновление текста выбранного времени
            }
        }, selectedDateTime.get(Calendar.HOUR_OF_DAY), selectedDateTime.get(Calendar.MINUTE), true); // Установка текущего времени
        timePickerDialog.show(); // Показать диалог выбора времени
    }

    // Метод для обновления текста выбранной даты
    private void updateDateText() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()); // Формат даты
        dateTextView.setText(sdf.format(selectedDateTime.getTime())); // Установка текста выбранной даты
    }

    // Метод для обновления текста выбранного времени
    private void updateTimeText() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault()); // Формат времени
        timeTextView.setText(sdf.format(selectedDateTime.getTime())); // Установка текста выбранного времени
    }

    // Метод для создания задачи
    private void createTask() {
        String taskName = taskNameEditText.getText().toString().trim(); // Получение названия задачи
        String taskDescription = taskDescriptionEditText.getText().toString().trim(); // Получение описания задачи
        String taskTags = taskTagsEditText.getText().toString().trim(); // Получ
        // Получение тегов задачи
        boolean showInCalendar = showInCalendarCheckBox.isChecked(); // Получение флага отображения задачи в календаре
        boolean notify = notifyCheckBox.isChecked(); // Получение флага уведомления о задаче

        if (TextUtils.isEmpty(taskName)) { // Проверка на пустое название задачи
            Toast.makeText(requireContext(), "Введите название задачи", Toast.LENGTH_SHORT).show(); // Показ сообщения об ошибке
            return;
        }

        Task task = new Task(taskName, taskDescription, taskTags, showInCalendar, notify, false); // Создание новой задачи

        task.setDate(selectedDateTime); // Установка выбранной даты и времени для задачи

        TaskDatabaseHelper dbHelper = new TaskDatabaseHelper(requireContext()); // Инициализация помощника базы данных для задач
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Получение доступа к базе данных для записи
        ContentValues values = new ContentValues(); // Создание объекта для хранения значений
        values.put(TaskDatabaseHelper.COLUMN_NAME, task.getName()); // Добавление названия задачи
        values.put(TaskDatabaseHelper.COLUMN_DESCRIPTION, task.getDescription()); // Добавление описания задачи
        values.put(TaskDatabaseHelper.COLUMN_TAGS, task.getTags()); // Добавление тегов задачи
        values.put(TaskDatabaseHelper.COLUMN_SHOW_IN_CALENDAR, task.isShowInCalendar() ? 1 : 0); // Добавление флага отображения в календаре
        values.put(TaskDatabaseHelper.COLUMN_NOTIFY, task.isNotify() ? 1 : 0); // Добавление флага уведомления
        values.put(TaskDatabaseHelper.COLUMN_DATE, task.getDate().getTimeInMillis()); // Добавление даты и времени

        long newRowId = db.insert(TaskDatabaseHelper.TABLE_TASKS, null, values); // Вставка задачи в базу данных
        db.close(); // Закрытие базы данных

        Toast.makeText(requireContext(), "Задача создана", Toast.LENGTH_SHORT).show(); // Показ сообщения об успешном создании задачи

        if (notify) { // Проверка необходимости отправки уведомления
            long notificationTimeMillis = selectedDateTime.getTimeInMillis(); // Получение времени уведомления
            scheduleNotification(notificationTimeMillis); // Запланировать уведомление
        }

        getParentFragmentManager().popBackStack(); // Возврат к предыдущему фрагменту
    }

    // Метод для сохранения выбранной даты в SharedPreferences
    private void saveSelectedDate(long selectedDateMillis) {
        SharedPreferences preferences = requireContext().getSharedPreferences("selected_date", Context.MODE_PRIVATE); // Получение SharedPreferences
        SharedPreferences.Editor editor = preferences.edit(); // Получение редактора
        editor.putLong("selected_date_millis", selectedDateMillis); // Сохранение выбранной даты
        editor.apply(); // Применение изменений
    }

    // Метод для запланирования уведомления
    private void scheduleNotification(long notificationTimeMillis) {
        Intent notificationIntent = new Intent(requireContext(), NotificationReceiver.class); // Создание намерения для уведомления
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE); // Получение PendingIntent для уведомления

        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE); // Получение менеджера уведомлений
        if (alarmManager != null) { // Проверка на наличие менеджера уведомлений
            alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTimeMillis, pendingIntent); // Запланировать уведомление
        }
    }
}
