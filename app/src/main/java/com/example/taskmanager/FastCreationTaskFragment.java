package com.example.taskmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class FastCreationTaskFragment extends Fragment {

    private EditText taskNameEditText;
    private EditText taskTagsEditText;
    private CheckBox showInCalendarAndListCheckBox;
    private Button cancelButton;
    private Button createButton;
    private TaskRepository taskRepository;

    private CheckBox showInCalendarCheckBox;

    private Calendar selectedDateTime; // Добавим переменную для хранения выбранной даты

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fast_creation_task, container, false);

        taskNameEditText = view.findViewById(R.id.task_name);
        taskTagsEditText = view.findViewById(R.id.task_tags);
        showInCalendarAndListCheckBox = view.findViewById(R.id.show_in_calendar_and_list);
        cancelButton = view.findViewById(R.id.cancel_button);
        createButton = view.findViewById(R.id.create_button);
        showInCalendarCheckBox = view.findViewById(R.id.show_in_calendar_and_list);

        taskRepository = new TaskRepository(getContext());

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = taskNameEditText.getText().toString();
                String taskTags = taskTagsEditText.getText().toString();
                boolean showInCalendarAndList = showInCalendarAndListCheckBox.isChecked();

                if (taskName.isEmpty()) {
                    Toast.makeText(getContext(), "Введите название задачи", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Assuming notify is false by default for fast creation
                Task task = new Task(taskName, null, taskTags, showInCalendarAndList, false);
                taskRepository.addTask(task);

                if (showInCalendarAndList) {
                    addToTaskList(task);
                }

                Toast.makeText(getContext(), "Задача создана", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            }
        });

        showInCalendarCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Получаем выбранную дату из календаря
                    selectedDateTime = Calendar.getInstance();
                    // Сохраняем выбранную дату в SharedPreferences
                    saveSelectedDate(selectedDateTime.getTimeInMillis());
                }
            }
        });

        return view;
    }

    private void addToTaskList(Task task) {
        ListView taskListView = getActivity().findViewById(R.id.TasksList);
        if (taskListView != null) {
            TaskListAdapter adapter = (TaskListAdapter) taskListView.getAdapter();
            if (adapter != null) {
                adapter.addTask(task);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Ошибка при добавлении задачи в список", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveSelectedDate(long selectedDateMillis) {
        SharedPreferences preferences = requireContext().getSharedPreferences("selected_date", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("selected_date_millis", selectedDateMillis);
        editor.apply();
    }
}
