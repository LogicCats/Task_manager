package com.example.taskmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment {

    private TaskRepository taskRepository;
    private TaskListAdapter adapter;
    private ListView tasksListView;
    private CalendarView calendarView;

    private long getSelectedDate() {
        SharedPreferences preferences = requireContext().getSharedPreferences("selected_date", Context.MODE_PRIVATE);
        return preferences.getLong("selected_date_millis", 0); // 0 - значение по умолчанию
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        taskRepository = new TaskRepository(requireContext());
        tasksListView = view.findViewById(R.id.TasksList);
        calendarView = view.findViewById(R.id.Calendar_panel);

        adapter = new TaskListAdapter(requireContext(), new ArrayList<>(), taskRepository);
        tasksListView.setAdapter(adapter);

        // Set up the calendar listener
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Создаем календарь и устанавливаем выбранную дату
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                // Обновляем список задач для выбранной даты
                updateTaskList(selectedDate);
            }
        });

        // Update task list for the current date
        updateTaskList(Calendar.getInstance());

        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Определить, нужно ли скрывать или показывать дополнительную информацию
                LinearLayout detailsLayout = view.findViewById(R.id.details_layout);
                if (detailsLayout.getVisibility() == View.GONE) {
                    detailsLayout.setVisibility(View.VISIBLE);
                } else {
                    detailsLayout.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        // Refresh the task list when returning to the fragment
        updateTaskList(Calendar.getInstance());
    }

    private void updateTaskList(Calendar selectedDate) {
        // Получаем миллисекунды из объекта Calendar для выбранной даты
        long selectedDateMillis = selectedDate.getTimeInMillis();

        // Форматируем выбранную дату для отображения в заголовке фрагмента
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = sdf.format(selectedDate.getTime());
        requireActivity().setTitle(formattedDate);

        // Получаем задачи для выбранной даты из репозитория
        List<Task> tasks = taskRepository.getTasksForDate(selectedDateMillis);

        // Очищаем адаптер и добавляем полученные задачи
        adapter.clear();
        adapter.addAll(tasks);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        long selectedDateMillis = 0;
        Bundle args = getArguments();
        if (args != null) {
            selectedDateMillis = args.getLong("selected_date_millis", 0);
        }
        if (selectedDateMillis != 0) {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.setTimeInMillis(selectedDateMillis);
            updateTaskList(selectedDate);
        }
    }


}
