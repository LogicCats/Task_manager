package com.example.taskmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends Fragment {

    private ListView tasksListView;
    private TaskListAdapter taskListAdapter;
    private TaskRepository taskRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        tasksListView = view.findViewById(R.id.TasksList);
        taskListAdapter = new TaskListAdapter(getContext(), new ArrayList<>());
        tasksListView.setAdapter(taskListAdapter);

        taskRepository = new TaskRepository(getContext());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTaskList();
    }

    private void updateTaskList() {
        List<Task> tasks = taskRepository.getAllTasks();
        taskListAdapter.clear();
        taskListAdapter.addAll(tasks);
        taskListAdapter.notifyDataSetChanged();
    }

}
