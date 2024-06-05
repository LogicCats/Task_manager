package com.example.taskmanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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

    private EditText taskNameEditText;
    private EditText taskDescriptionEditText;
    private EditText taskTagsEditText;
    private CheckBox showInCalendarCheckBox;
    private CheckBox notifyCheckBox;
    private LinearLayout dateLayout;
    private LinearLayout timeLayout;
    private TextView dateTextView;
    private TextView timeTextView;
    private Button createButton;

    private Calendar selectedDateTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creation_task, container, false);

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

        selectedDateTime = Calendar.getInstance();

        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTask();
            }
        });

        showInCalendarCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Сохраняем выбранную дату в SharedPreferences
                    saveSelectedDate(selectedDateTime.getTimeInMillis());
                }
            }
        });

        return view;
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedDateTime.set(Calendar.YEAR, year);
                selectedDateTime.set(Calendar.MONTH, monthOfYear);
                selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateText();
            }
        }, selectedDateTime.get(Calendar.YEAR), selectedDateTime.get(Calendar.MONTH), selectedDateTime.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
                selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedDateTime.set(Calendar.MINUTE, minute);
                updateTimeText();
            }
        }, selectedDateTime.get(Calendar.HOUR_OF_DAY), selectedDateTime.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void updateDateText() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateTextView.setText(sdf.format(selectedDateTime.getTime()));
    }

    private void updateTimeText() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        timeTextView.setText(sdf.format(selectedDateTime.getTime()));
    }

    private void createTask() {
        String taskName = taskNameEditText.getText().toString().trim();
        String taskDescription = taskDescriptionEditText.getText().toString().trim();
        String taskTags = taskTagsEditText.getText().toString().trim();
        boolean showInCalendar = showInCalendarCheckBox.isChecked();
        boolean notify = notifyCheckBox.isChecked();

        if (TextUtils.isEmpty(taskName)) {
            Toast.makeText(requireContext(), "Введите название задачи", Toast.LENGTH_SHORT).show();
            return;
        }

        Task task = new Task(taskName, taskDescription, taskTags, showInCalendar, notify, false);

        // Устанавливаем дату задачи
        task.setDate(selectedDateTime.getTimeInMillis());

        TaskDatabaseHelper dbHelper = new TaskDatabaseHelper(requireContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskDatabaseHelper.COLUMN_NAME, task.getName());
        values.put(TaskDatabaseHelper.COLUMN_DESCRIPTION, task.getDescription()); // добавляем описание
        values.put(TaskDatabaseHelper.COLUMN_TAGS, task.getTags());
        values.put(TaskDatabaseHelper.COLUMN_SHOW_IN_CALENDAR, task.isShowInCalendar() ? 1 : 0);
        values.put(TaskDatabaseHelper.COLUMN_NOTIFY, task.isNotify() ? 1 : 0);
        values.put(TaskDatabaseHelper.COLUMN_DATE, task.getDate()); // сохраняем дату

        long newRowId = db.insert(TaskDatabaseHelper.TABLE_TASKS, null, values);
        db.close();

        Toast.makeText(requireContext(), "Задача создана", Toast.LENGTH_SHORT).show();
        getParentFragmentManager().popBackStack();
    }

    private void saveSelectedDate(long selectedDateMillis) {
        SharedPreferences preferences = requireContext().getSharedPreferences("selected_date", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("selected_date_millis", selectedDateMillis);
        editor.apply();
    }


}
