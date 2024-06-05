package com.example.taskmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class StatisticsFragment extends Fragment {

    private TaskRepository taskRepository;
    private SharedPreferences sharedPreferences;

    private TextView totalTasksTextView;
    private TextView completedTasksTextView;
    private TextView pendingTasksTextView;
    private TextView tasksWithNotificationsTextView;
    private TextView appUsageTimeTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        taskRepository = new TaskRepository(getContext());
        sharedPreferences = getContext().getSharedPreferences("app_usage_preferences", Context.MODE_PRIVATE);

        totalTasksTextView = view.findViewById(R.id.total_tasks);
        completedTasksTextView = view.findViewById(R.id.completed_tasks);
        pendingTasksTextView = view.findViewById(R.id.pending_tasks);
        tasksWithNotificationsTextView = view.findViewById(R.id.tasks_with_notifications);
        appUsageTimeTextView = view.findViewById(R.id.app_usage_time);

        updateStatistics();

        return view;
    }

    private void updateStatistics() {
        List<Task> tasks = taskRepository.getAllTasks();
        int totalTasks = tasks.size();
        int completedTasks = 0;
        int tasksWithNotifications = 0;

        for (Task task : tasks) {
            if (task.isCompleted()) {
                completedTasks++;
            }
            if (task.isNotify()) {
                tasksWithNotifications++;
            }
        }

        int pendingTasks = totalTasks - completedTasks;
        int appUsageTime = sharedPreferences.getInt("app_usage_time", 0); // Assume usage time is stored in minutes

        totalTasksTextView.setText("Total tasks: " + totalTasks);
        completedTasksTextView.setText("Completed tasks: " + completedTasks);
        pendingTasksTextView.setText("Pending tasks: " + pendingTasks);
        tasksWithNotificationsTextView.setText("Tasks with notifications: " + tasksWithNotifications);
        appUsageTimeTextView.setText("App usage time: " + appUsageTime + " minutes");
    }
}
