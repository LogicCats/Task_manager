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

// Класс фрагмента для отображения списка задач
public class TasksFragment extends Fragment {

    private ListView tasksListView; // ListView для отображения задач
    private TaskListAdapter taskListAdapter; // Адаптер для списка задач
    private TaskRepository taskRepository; // Репозиторий для работы с задачами
    private SearchView searchView; // Поле поиска для фильтрации задач

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Создаем и инициализируем View для фрагмента
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        // Инициализируем ListView для отображения задач
        tasksListView = view.findViewById(R.id.TasksList);
        // Инициализируем SearchView для поиска задач
        searchView = view.findViewById(R.id.SearchViewTasks);

        // Инициализируем TaskRepository для доступа к данным задач
        taskRepository = new TaskRepository(getContext());

        // Получаем все задачи из репозитория
        List<Task> tasks = taskRepository.getAllTasks();
        // Инициализируем адаптер для списка задач, передавая контекст, задачи и репозиторий
        taskListAdapter = new TaskListAdapter(getContext(), tasks, taskRepository);
        // Устанавливаем адаптер для ListView
        tasksListView.setAdapter(taskListAdapter);

        // Настраиваем функциональность поиска
        setupSearch();

        // Возвращаем созданное View
        return view;
    }

    // Метод для настройки функциональности поиска
    private void setupSearch() {
        // Устанавливаем слушатель для изменения текста в поле поиска
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Возвращаем false, так как не обрабатываем отправку запроса
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Фильтруем список задач на основе введенного текста
                taskListAdapter.getFilter().filter(newText);
                // Возвращаем true, так как обработали изменение текста
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Обновляем список задач при возвращении к фрагменту
        updateTaskList();
    }

    // Метод для обновления списка задач
    private void updateTaskList() {
        // Получаем все задачи из репозитория
        List<Task> tasks = taskRepository.getAllTasks();

        // Очищаем адаптер
        taskListAdapter.clear();

        // Добавляем в адаптер только незавершенные задачи
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                taskListAdapter.add(task);
            }
        }

        // Уведомляем адаптер об изменении данных
        taskListAdapter.notifyDataSetChanged();
    }
}
