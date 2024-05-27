package com.example.taskmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class SettingsFragment extends Fragment {

    private Button buttonRegister;
    private Button buttonLogin;
    private Button buttonChangeTheme;
    private Button buttonWipeData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        buttonRegister = view.findViewById(R.id.accountRegistration);
        buttonLogin = view.findViewById(R.id.accountLogin);
        buttonChangeTheme = view.findViewById(R.id.ChangeTheme);
        buttonWipeData = view.findViewById(R.id.wipe_data);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegistrationFragment();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLoginFragment();
            }
        });

        buttonChangeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showThemeDialog();
            }
        });

        buttonWipeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWipeDataDialog();
            }
        });

        return view;
    }

    private void navigateToRegistrationFragment() {
        RegistrationFragment registrationFragment = new RegistrationFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, registrationFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToLoginFragment() {
        LoginFragment loginFragment = new LoginFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, loginFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showThemeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Выберите тему")
                .setItems(new String[]{"Светлая", "Тёмная"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                saveThemePreference(AppCompatDelegate.MODE_NIGHT_NO);
                                break;
                            case 1:
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                saveThemePreference(AppCompatDelegate.MODE_NIGHT_YES);
                                break;
                        }
                    }
                })
                .show();
    }

    private void saveThemePreference(int mode) {
        SharedPreferences preferences = requireActivity().getSharedPreferences("theme_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("theme_mode", mode);
        editor.apply();
    }

    private void showWipeDataDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Подтверждение")
                .setMessage("Вы уверены, что хотите удалить все данные и сбросить настройки?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wipeData();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void wipeData() {
        // Удаление всех данных SharedPreferences
        SharedPreferences preferences = requireActivity().getSharedPreferences("theme_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Здесь добавьте удаление других данных, если необходимо

        // Перезапуск приложения
        requireActivity().recreate();
    }
}
