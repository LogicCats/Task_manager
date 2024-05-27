package com.example.taskmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.List;

public class CalendarFragment extends Fragment {

    private TaskRepository taskRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Инициализация TaskRepository
        taskRepository = new TaskRepository(requireContext());

        // Находим ListView в макете
        ListView tasksListView = view.findViewById(R.id.TasksList);

        // Создаем адаптер для списка задач
        TaskListAdapter adapter = new TaskListAdapter(requireContext(), taskRepository.getAllTasks());

        // Устанавливаем адаптер для ListView
        tasksListView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        // Обновление списка задач при возврате к фрагменту
        updateTaskList();
    }

    private void updateTaskList() {
        // Получение списка задач из репозитория
        List<Task> tasks = taskRepository.getAllTasks();
        // Обновление списка задач в фрагменте календаря
        // Например, вы можете использовать RecyclerView или ListView для отображения списка задач
        // Здесь можно добавить логику для обновления пользовательского интерфейса вашего фрагмента
    }
}
