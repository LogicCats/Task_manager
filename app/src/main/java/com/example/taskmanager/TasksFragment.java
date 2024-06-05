package com.example.taskmanager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends Fragment {

    private ListView tasksListView;
    private TaskListAdapter taskListAdapter;
    private TaskRepository taskRepository;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        tasksListView = view.findViewById(R.id.TasksList);
        searchView = view.findViewById(R.id.SearchViewTasks);

        // Initialize TaskRepository
        taskRepository = new TaskRepository(getContext());

        // Pass TaskRepository to TaskListAdapter
        List<Task> tasks = taskRepository.getAllTasks();
        taskListAdapter = new TaskListAdapter(getContext(), tasks, taskRepository);
        tasksListView.setAdapter(taskListAdapter);

        // Set up the search functionality
        setupSearch();

        return view;
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the list based on the entered text
                taskListAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTaskList();
    }

    private void updateTaskList() {
        // Get all tasks from the repository
        List<Task> tasks = taskRepository.getAllTasks();

        // Clear the adapter
        taskListAdapter.clear();

        // Add only incomplete tasks to the adapter
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                taskListAdapter.add(task);
            }
        }

        // Notify the adapter that the data set has changed
        taskListAdapter.notifyDataSetChanged();
    }
}
