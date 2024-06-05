package com.example.taskmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

/**
 * FastCreationTaskFragment - фрагмент для быстрого создания задачи.
 */
public class FastCreationTaskFragment extends Fragment {

    private EditText taskNameEditText; // Поле для ввода названия задачи
    private EditText taskTagsEditText; // Поле для ввода тегов задачи
    private CheckBox showInCalendarAndListCheckBox; // Флажок для выбора отображения задачи в календаре и списке
    private Button cancelButton; // Кнопка для отмены создания задачи
    private Button createButton; // Кнопка для создания задачи
    private TaskRepository taskRepository; // Репозиторий задач

    private CheckBox showInCalendarCheckBox; // Флажок для выбора отображения задачи в календаре

    private Calendar selectedDateTime; // Переменная для хранения выбранной даты

    /**
     * Метод вызывается при создании представления фрагмента.
     *
     * @param inflater           объект для создания представления из макета
     * @param container          если непусто, это родительское представление, в которое фрагмент вставляется
     * @param savedInstanceState если ненулевой, это предыдущее состояние фрагмента
     * @return View объект, представляющий пользовательский интерфейс фрагмента
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fast_creation_task, container, false); // Надуваем макет фрагмента

        // Инициализация всех элементов пользовательского интерфейса
        taskNameEditText = view.findViewById(R.id.task_name);
        taskTagsEditText = view.findViewById(R.id.task_tags);
        showInCalendarAndListCheckBox = view.findViewById(R.id.show_in_calendar_and_list);
        cancelButton = view.findViewById(R.id.cancel_button);
        createButton = view.findViewById(R.id.create_button);
        showInCalendarCheckBox = view.findViewById(R.id.show_in_calendar_and_list);

        taskRepository = new TaskRepository(getContext()); // Инициализация репозитория задач

        // Установка слушателя для кнопки отмены
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack(); // Возврат к предыдущему фрагменту
            }
        });

        // Установка слушателя для кнопки создания задачи
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = taskNameEditText.getText().toString(); // Получение названия задачи
                String taskTags = taskTagsEditText.getText().toString(); // Получение тегов задачи
                boolean showInCalendarAndList = showInCalendarAndListCheckBox.isChecked(); // Проверка выбора отображения в календаре и списке

                if (taskName.isEmpty()) { // Проверка наличия названия задачи
                    Toast.makeText(getContext(), "Введите название задачи", Toast.LENGTH_SHORT).show(); // Вывод сообщения об ошибке
                    return; // Прерывание выполнения метода
                }

                // Предполагаем, что уведомление по умолчанию выключено для быстрого создания
                Task task = new Task(taskName, null, taskTags, showInCalendarAndList, false); // Создание задачи
                taskRepository.addTask(task); // Добавление задачи в репозиторий

                if (showInCalendarAndList) { // Проверка отображения в календаре и списке
                    addToTaskList(task); // Добавление задачи в список
                }

                Toast.makeText(getContext(), "Задача создана", Toast.LENGTH_SHORT).show(); // Вывод сообщения об успешном создании
                getParentFragmentManager().popBackStack(); // Возврат к предыдущему фрагменту
            }
        });

        // Установка слушателя для флажка отображения в календаре
        showInCalendarCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // Проверка выбора флажка
                    // Получаем текущую дату
                    selectedDateTime = Calendar.getInstance();
                    // Сохраняем выбранную дату в SharedPreferences
                    saveSelectedDate(selectedDateTime.getTimeInMillis());
                }
            }
        });

        return view; // Возвращаем созданный вид
    }

/**
 * Метод для добавления задачи в список задач.
 *
 * @param task добавляем
 * @param task добавляемая задача
 */
private void addToTaskList(Task task) {
    ListView taskListView = getActivity().findViewById(R.id.TasksList); // Получение списка задач из активности
    if (taskListView != null) { // Проверка наличия списка задач
        TaskListAdapter adapter = (TaskListAdapter) taskListView.getAdapter(); // Получение адаптера списка задач
        if (adapter != null) { // Проверка наличия адаптера
            adapter.addTask(task); // Добавление задачи в адаптер
            adapter.notifyDataSetChanged(); // Уведомление адаптера об изменениях
        } else {
            Toast.makeText(getContext(), "Ошибка при добавлении задачи в список", Toast.LENGTH_SHORT).show(); // Вывод сообщения об ошибке
        }
    }
}

    /**
     * Метод для сохранения выбранной даты в SharedPreferences.
     *
     * @param selectedDateMillis миллисекунды выбранной даты
     */
    private void saveSelectedDate(long selectedDateMillis) {
        SharedPreferences preferences = requireContext().getSharedPreferences("selected_date", Context.MODE_PRIVATE); // Получение объекта SharedPreferences
        SharedPreferences.Editor editor = preferences.edit(); // Получение редактора SharedPreferences
        editor.putLong("selected_date_millis", selectedDateMillis); // Сохранение выбранной даты
        editor.apply(); // Применение изменений
    }
}
