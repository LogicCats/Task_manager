package com.example.taskmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;

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
    private Button buttonChangeNotification;
    private Button buttonAboutApp; // Добавляем кнопку для версии приложения
    private Button buttonTermsOfService;
    private Button buttonPrivacyPolicy;
    private Button buttonAccount; // Добавляем кнопку для перехода к аккаунту

    private Button buttonGuide; // Добавляем кнопку для краткого обучения

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        buttonRegister = view.findViewById(R.id.accountRegistration);
        buttonLogin = view.findViewById(R.id.accountLogin);
        buttonChangeTheme = view.findViewById(R.id.ChangeTheme);
        buttonWipeData = view.findViewById(R.id.wipe_data);
        buttonChangeNotification = view.findViewById(R.id.ChangeNotification);
        buttonAboutApp = view.findViewById(R.id.about_app); // Инициализируем кнопку для версии приложения
        buttonTermsOfService = view.findViewById(R.id.terms_of_service);
        buttonPrivacyPolicy = view.findViewById(R.id.politics);
        buttonAccount = view.findViewById(R.id.account_button); // Инициализируем кнопку для перехода к аккаунту
        buttonGuide = view.findViewById(R.id.guide); // Инициализируем кнопку для краткого обучения

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegistrationFragment();
            }
        });

        buttonTermsOfService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToTermsFragment();
            }
        });

        buttonPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToPrivacyPolicyFragment();
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

        buttonChangeNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppNotificationSettings();
            }
        });

        buttonAboutApp.setOnClickListener(new View.OnClickListener() { // Устанавливаем обработчик нажатия для кнопки версии приложения
            @Override
            public void onClick(View v) {
                showAppVersion();
            }
        });

        buttonAccount.setOnClickListener(new View.OnClickListener() { // Обработчик нажатия для кнопки аккаунта
            @Override
            public void onClick(View v) {
                navigateToAccountFragment();
            }
        });

        buttonGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToGuideFragment();
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

    private void navigateToAccountFragment() {
        // Проверяем, существует ли уже AccountFragment
        AccountFragment accountFragment = (AccountFragment) getActivity().getSupportFragmentManager().findFragmentByTag("AccountFragment");

        // Если не существует, создаем новый
        if (accountFragment == null) {
            accountFragment = new AccountFragment();
        }

        // Переход к AccountFragment
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, accountFragment, "AccountFragment");
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

        // Удаление всех задач из базы данных
        TaskRepository taskRepository = new TaskRepository(requireContext());
        taskRepository.deleteAllTasks();

        // Удаление информации о завершенности задач из SharedPreferences
        SharedPreferences taskCompletionPreferences = requireActivity().getSharedPreferences("task_completion_status", Context.MODE_PRIVATE);
        SharedPreferences.Editor taskCompletionEditor = taskCompletionPreferences.edit();
        taskCompletionEditor.clear();
        taskCompletionEditor.apply();

        // Перезапуск приложения
        requireActivity().recreate();
    }

    private void openAppNotificationSettings() {
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("android.provider.extra.APP_PACKAGE", requireActivity().getPackageName());
        startActivity(intent);
    }

    private void showAppVersion() {
        // Получаем версию приложения из манифеста
        String versionName = "";
        try {
            versionName = requireActivity().getPackageManager().getPackageInfo(requireActivity().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Отображаем версию приложения в диалоговом окне
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Версия приложения")
                .setMessage("Версия: " + versionName)
                .setPositiveButton("OK", null)
                .show();
    }

    private void navigateToTermsFragment() {
        TermsFragment termsFragment = new TermsFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, termsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToPrivacyPolicyFragment() {
        PrivacyPolicyFragment privacyPolicyFragment = new PrivacyPolicyFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, privacyPolicyFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToGuideFragment() {
        GuideFragment guideFragment = new GuideFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, guideFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
