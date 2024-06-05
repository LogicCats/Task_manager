package com.example.taskmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

// Класс фрагмента для отображения условий использования
public class TermsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Загрузка макета для фрагмента из XML-файла fragment_terms
        View view = inflater.inflate(R.layout.fragment_terms, container, false);

        // Поиск кнопки "Вернуться" в загруженном макете
        Button buttonReturnToSettings = view.findViewById(R.id.buttonReturnToSettings);
        // Установка обработчика нажатия на кнопку
        buttonReturnToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // При нажатии на кнопку вызывается метод navigateBackToSettings
                navigateBackToSettings();
            }
        });

        // Возвращаем созданное представление
        return view;
    }

    // Метод для возврата к фрагменту настроек
    private void navigateBackToSettings() {
        // Создание нового экземпляра SettingsFragment
        SettingsFragment settingsFragment = new SettingsFragment();
        // Получение менеджера фрагментов из текущей активности
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        // Начало транзакции фрагмента
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // Замена текущего фрагмента на SettingsFragment
        transaction.replace(R.id.frame_layout, settingsFragment);
        // Добавление текущей транзакции в back stack для возможности возврата
        transaction.addToBackStack(null);
        // Подтверждение транзакции
        transaction.commit();
    }
}
