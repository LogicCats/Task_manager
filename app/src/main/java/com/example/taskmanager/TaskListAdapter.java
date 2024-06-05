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

// Адаптер для списка задач, наследуемый от ArrayAdapter
public class TaskListAdapter extends ArrayAdapter<Task> {

    private TaskRepository taskRepository; // Репозиторий задач для взаимодействия с данными
    private SharedPreferences sharedPreferences; // Переменная для хранения настроек завершенности задач
    private List<Task> originalTasks; // Оригинальный список задач для фильтрации

    // Карта для хранения видимости деталей каждой задачи
    private Map<Long, Boolean> detailsVisibilityMap = new HashMap<>();

    // Конструктор адаптера
    public TaskListAdapter(Context context, List<Task> tasks, TaskRepository repository) {
        super(context, 0, tasks);
        this.taskRepository = repository; // Инициализация репозитория задач
        this.sharedPreferences = context.getSharedPreferences("task_completion_status", Context.MODE_PRIVATE); // Инициализация SharedPreferences
        this.originalTasks = new ArrayList<>(tasks); // Создание копии оригинального списка задач
    }

    // Метод для получения и отображения представления каждой задачи
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position); // Получение текущей задачи

        // Проверка, если представление еще не создано
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_list_item, parent, false); // Инфляция макета элемента списка
        }

        // Получение ссылок на элементы интерфейса
        TextView taskNameTextView = convertView.findViewById(R.id.task_name);
        TextView taskTagsTextView = convertView.findViewById(R.id.task_tags);
        TextView taskDescriptionTextView = convertView.findViewById(R.id.task_description); // Добавлено
        TextView taskDateTimeTextView = convertView.findViewById(R.id.task_date_time); // Добавлено
        CheckBox taskCheckBox = convertView.findViewById(R.id.task_checkbox);

        // Установка текста для элементов интерфейса
        taskNameTextView.setText(task.getName());
        taskTagsTextView.setText(task.getTags());

        // Установка статуса завершенности из SharedPreferences
        boolean isCompleted = sharedPreferences.getBoolean(String.valueOf(task.getId()), false);
        task.setCompleted(isCompleted); // Установка завершенности задачи
        taskCheckBox.setChecked(isCompleted); // Установка статуса чекбокса

        // Установка видимости описания задачи
        if (task.isDescriptionVisible()) {
            taskDescriptionTextView.setVisibility(View.VISIBLE); // Показать описание
        } else {
            taskDescriptionTextView.setVisibility(View.GONE); // Скрыть описание
        }

        // Установка видимости даты и времени задачи
        if (task.isDateTimeVisible()) {
            taskDateTimeTextView.setVisibility(View.VISIBLE); // Показать дату и время
        } else {
            taskDateTimeTextView.setVisibility(View.GONE); // Скрыть дату и время
        }

        // Установка слушателя для изменения состояния чекбокса
        taskCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setCompleted(isChecked); // Установка завершенности задачи

                // Сохранение статуса завершенности задачи в SharedPreferences
                saveTaskCompletionStatus(task.getId(), isChecked);
            }
        });

        // Установка слушателя для клика по элементу списка
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Инвертируем видимость описания и даты и времени
                task.setDescriptionVisible(!task.isDescriptionVisible());
                task.setDateTimeVisible(!task.isDateTimeVisible());
                notifyDataSetChanged(); // Обновляем адаптер для отображения изменений
            }
        });

        return convertView; // Возвращаем представление элемента списка
    }

    // Метод для получения фильтра задач
    @Override
    public Filter getFilter() {
        return taskFilter;
    }

    // Фильтр для задач
    private Filter taskFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Task> filteredList = new ArrayList<>(); // Список для хранения отфильтрованных задач

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(originalTasks); // Если поисковый запрос пустой, возвращаем оригинальный список задач
            } else {
                String filterPattern = constraint.toString().trim(); // Убираем пробелы в начале и в конце
                filterPattern = filterPattern.toLowerCase(); // Приводим поисковый запрос к нижнему регистру

                for (Task task : originalTasks) {
                    // Приводим название задачи к нижнему регистру и затем ищем вхождение поискового запроса
                    if (task.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(task); // Добавляем задачу в отфильтрованный список
                    }
                }
            }

            FilterResults results = new FilterResults(); // Создаем объект для хранения результатов фильтрации
            results.values = filteredList; // Устанавливаем отфильтрованный список задач в результаты
            return results; // Возвращаем результаты фильтрации
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear(); // Очищаем текущий список задач
            addAll((List) results.values); // Добавляем отфильтрованные задачи в адаптер
            notifyDataSetChanged(); // Уведомляем об изменениях в адаптере
        }
    };

    // Метод для добавления новой задачи
    public void addTask(Task task) {
        originalTasks.add(task); // Добавляем задачу в оригинальный список
        add(task); // Добавляем задачу в список адаптера
    }

    // Метод для удаления задачи
    public void removeTask(Task task) {
        originalTasks.remove(task); // Удаляем задачу из оригинального списка
        remove(task); // Удаляем задачу из списка адаптера
    }

    // Метод для сохранения статуса завершенности задачи в SharedPreferences
    private void saveTaskCompletionStatus(long taskId, boolean isCompleted) {
        SharedPreferences.Editor editor = sharedPreferences.edit(); // Получаем редактор SharedPreferences
        editor.putBoolean(String.valueOf(taskId), isCompleted); // Сохраняем статус завершенности задачи
        editor.apply(); // Применяем изменения
    }
}
