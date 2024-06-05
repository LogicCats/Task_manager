package com.example.taskmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskListAdapter extends ArrayAdapter<Task> {

    private TaskRepository taskRepository;
    private SharedPreferences sharedPreferences; // Добавляем переменную для SharedPreferences
    private List<Task> originalTasks; // Для сохранения оригинального списка задач

    private Map<Long, Boolean> detailsVisibilityMap = new HashMap<>();

    public TaskListAdapter(Context context, List<Task> tasks, TaskRepository repository) {
        super(context, 0, tasks);
        this.taskRepository = repository;
        // Инициализируем SharedPreferences
        this.sharedPreferences = context.getSharedPreferences("task_completion_status", Context.MODE_PRIVATE);
        // Создаем копию оригинального списка задач
        this.originalTasks = new ArrayList<>(tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_list_item, parent, false);
        }

        TextView taskNameTextView = convertView.findViewById(R.id.task_name);
        TextView taskTagsTextView = convertView.findViewById(R.id.task_tags);
        TextView taskDescriptionTextView = convertView.findViewById(R.id.task_description); // Добавлено
        TextView taskDateTimeTextView = convertView.findViewById(R.id.task_date_time); // Добавлено
        CheckBox taskCheckBox = convertView.findViewById(R.id.task_checkbox);

        taskNameTextView.setText(task.getName());
        taskTagsTextView.setText(task.getTags());

        // Устанавливаем статус завершенности из SharedPreferences
        boolean isCompleted = sharedPreferences.getBoolean(String.valueOf(task.getId()), false);
        task.setCompleted(isCompleted);
        taskCheckBox.setChecked(isCompleted);

        // Устанавливаем видимость описания и даты и времени
        if (task.isDescriptionVisible()) {
            taskDescriptionTextView.setVisibility(View.VISIBLE);
        } else {
            taskDescriptionTextView.setVisibility(View.GONE);
        }

        if (task.isDateTimeVisible()) {
            taskDateTimeTextView.setVisibility(View.VISIBLE);
        } else {
            taskDateTimeTextView.setVisibility(View.GONE);
        }

        taskCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setCompleted(isChecked);

                // Сохраняем статус завершенности задачи в SharedPreferences
                saveTaskCompletionStatus(task.getId(), isChecked);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Инвертируем видимость описания и даты и времени и обновляем состояние задачи
                task.setDescriptionVisible(!task.isDescriptionVisible());
                task.setDateTimeVisible(!task.isDateTimeVisible());
                notifyDataSetChanged(); // обновляем адаптер для отображения изменений
            }
        });

        return convertView;
    }



    @Override
    public Filter getFilter() {
        return taskFilter;
    }

    private Filter taskFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Task> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(originalTasks); // Если поисковый запрос пустой, возвращаем оригинальный список задач
            } else {
                String filterPattern = constraint.toString().trim(); // Убираем пробелы в начале и в конце
                filterPattern = filterPattern.toLowerCase(); // Приводим поисковый запрос к нижнему регистру

                for (Task task : originalTasks) {
                    // Приводим название задачи к нижнему регистру и затем ищем вхождение поискового запроса
                    if (task.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(task);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear(); // Очищаем текущий список задач
            addAll((List) results.values); // Добавляем отфильтрованные задачи
            notifyDataSetChanged(); // Уведомляем об изменениях в адаптере
        }
    };


    public void addTask(Task task) {
        originalTasks.add(task); // Добавляем задачу в оригинальный список
        add(task); // Добавляем задачу в список адаптера
    }

    public void removeTask(Task task) {
        originalTasks.remove(task); // Удаляем задачу из оригинального списка
        remove(task); // Удаляем задачу из списка адаптера
    }

    private void saveTaskCompletionStatus(long taskId, boolean isCompleted) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(String.valueOf(taskId), isCompleted);
        editor.apply();
    }
}
