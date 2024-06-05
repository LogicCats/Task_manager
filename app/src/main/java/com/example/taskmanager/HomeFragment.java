package com.example.taskmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;

/**
 * Фрагмент, отображающий домашнюю страницу приложения.
 */
public class HomeFragment extends Fragment {

    private TaskRepository taskRepository; // Репозиторий задач

    /**
     * Создает и настраивает пользовательский интерфейс фрагмента.
     *
     * @param inflater           объект, который может преобразовать XML-файл макета в объекты View.
     * @param container          родительский объект View, к которому будет присоединен макет фрагмента.
     * @param savedInstanceState объект, содержащий данные о состоянии фрагмента.
     * @return View, представляющий пользовательский интерфейс фрагмента.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize TaskRepository
        taskRepository = new TaskRepository(requireContext());

        // Find ListView in the layout
        ListView tasksListView = view.findViewById(R.id.TasksList);

        // Create adapter for the task list
        TaskListAdapter adapter = new TaskListAdapter(requireContext(), taskRepository.getAllTasks(), taskRepository);

        // Set the adapter for ListView
        tasksListView.setAdapter(adapter);

        return view;
    }


    /**
     * Вызывается при возврате к фрагменту.
     */
    @Override
    public void onResume() {
        super.onResume();
        // Обновление списка задач при возврате к фрагменту
        updateTaskList();
    }

    /**
     * Обновляет список задач.
     */
    private void updateTaskList() {
        // Получение списка задач из репозитория
        List<Task> tasks = taskRepository.getAllTasks();
        // Обновление списка задач в домашнем фрагменте
        // Например, вы можете использовать RecyclerView или ListView для отображения списка задач
        // Здесь можно добавить логику для обновления пользовательского интерфейса вашего фрагмента
        Toast.makeText(requireContext(), "Task list updated", Toast.LENGTH_SHORT).show();
    }
}
