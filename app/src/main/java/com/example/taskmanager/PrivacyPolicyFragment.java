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

/**
 * Фрагмент для отображения политики конфиденциальности.
 */
public class PrivacyPolicyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Загрузка макета для фрагмента
        View view = inflater.inflate(R.layout.fragment_privacy_policy, container, false);

        // Настройка обработчика нажатия кнопки "Вернуться"
        Button buttonReturnToSettings = view.findViewById(R.id.buttonReturnToSettings);
        buttonReturnToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBackToSettings(); // Вызов метода для возврата к настройкам
            }
        });

        return view;
    }

    // Метод для возврата к настройкам
    private void navigateBackToSettings() {
        SettingsFragment settingsFragment = new SettingsFragment(); // Создание нового фрагмента настроек
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager(); // Получение менеджера фрагментов
        FragmentTransaction transaction = fragmentManager.beginTransaction(); // Начало транзакции фрагмента
        transaction.replace(R.id.frame_layout, settingsFragment); // Замена текущего фрагмента на фрагмент настроек
        transaction.addToBackStack(null); // Добавление транзакции в стек возврата
        transaction.commit(); // Завершение транзакции
    }
}
