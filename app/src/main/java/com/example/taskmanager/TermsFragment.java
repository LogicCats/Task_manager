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

public class TermsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Загрузка макета для фрагмента
        View view = inflater.inflate(R.layout.fragment_terms, container, false);

        // Настройка обработчика нажатия кнопки "Вернуться"
        Button buttonReturnToSettings = view.findViewById(R.id.buttonReturnToSettings);
        buttonReturnToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBackToSettings();
            }
        });

        return view;
    }

    // Метод для возврата к настройкам
    private void navigateBackToSettings() {
        SettingsFragment settingsFragment = new SettingsFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, settingsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
