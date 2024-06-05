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

    private TaskRepository taskRepository; // Репозиторий для работы с задачами
    private SharedPreferences sharedPreferences; // Общие настройки SharedPreferences

    private TextView totalTasksTextView; // TextView для общего числа задач
    private TextView completedTasksTextView; // TextView для числа завершенных задач
    private TextView pendingTasksTextView; // TextView для числа невыполненных задач
    private TextView tasksWithNotificationsTextView; // TextView для числа задач с уведомлениями
    private TextView appUsageTimeTextView; // TextView для времени использования приложения

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Загрузка макета для фрагмента
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        // Инициализация репозитория задач
        taskRepository = new TaskRepository(getContext());
        // Получение экземпляра SharedPreferences для хранения времени использования приложения
        sharedPreferences = getContext().getSharedPreferences("app_usage_preferences", Context.MODE_PRIVATE);

        // Инициализация TextView из макета
        totalTasksTextView = view.findViewById(R.id.total_tasks);
        completedTasksTextView = view.findViewById(R.id.completed_tasks);
        pendingTasksTextView = view.findViewById(R.id.pending_tasks);
        tasksWithNotificationsTextView = view.findViewById(R.id.tasks_with_notifications);
        appUsageTimeTextView = view.findViewById(R.id.app_usage_time);

        // Обновление статистики при отображении фрагмента
        updateStatistics();

        return view;
    }

    // Метод для обновления статистики
    private void updateStatistics() {
        // Получение списка всех задач из репозитория
        List<Task> tasks = taskRepository.getAllTasks();
        // Вычисление общего числа задач
        int totalTasks = tasks.size();
        // Инициализация счетчиков завершенных задач и задач с уведомлениями
        int completedTasks = 0;
        int tasksWithNotifications = 0;

        // Перебор списка задач для подсчета завершенных задач и задач с уведомлениями
        for (Task task : tasks) {
            if (task.isCompleted()) {
                completedTasks++;
            }
            if (task.isNotify()) {
                tasksWithNotifications++;
            }
        }

        // Вычисление числа невыполненных задач
        int pendingTasks = totalTasks - completedTasks;
        // Получение времени использования приложения из SharedPreferences (предполагаем, что время хранится в минутах)
        int appUsageTime = sharedPreferences.getInt("app_usage_time", 0);

        // Установка текста в TextView для отображения статистики
        totalTasksTextView.setText("Всего тасков: " + totalTasks);
        completedTasksTextView.setText("Выполнено тасков: " + completedTasks);
        pendingTasksTextView.setText("Таски в процессе: " + pendingTasks);
        tasksWithNotificationsTextView.setText("Тасков с уведомлением: " + tasksWithNotifications);
        appUsageTimeTextView.setText("Время, проведённое в приложении: " + appUsageTime + " минут(ы)");
    }

}
