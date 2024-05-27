package com.example.taskmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class FastCreationTaskFragment extends Fragment {

    private EditText taskNameEditText;
    private EditText taskTagsEditText;
    private CheckBox showInCalendarAndListCheckBox;
    private Button cancelButton;
    private Button createButton;
    private TaskRepository taskRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fast_creation_task, container, false);

        taskNameEditText = view.findViewById(R.id.task_name);
        taskTagsEditText = view.findViewById(R.id.task_tags);
        showInCalendarAndListCheckBox = view.findViewById(R.id.show_in_calendar_and_list);
        cancelButton = view.findViewById(R.id.cancel_button);
        createButton = view.findViewById(R.id.create_button);

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

                Task task = new Task(taskName, taskTags, showInCalendarAndList);
                taskRepository.addTask(task);

                if (showInCalendarAndList) {
                    addToTaskList(task);
                }

                Toast.makeText(getContext(), "Задача создана", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
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
}
