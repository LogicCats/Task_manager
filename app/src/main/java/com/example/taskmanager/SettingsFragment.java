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

/**
 * Фрагмент для настроек приложения.
 */
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
        // Загружаем макет для фрагмента настроек
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Инициализируем кнопки
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

        // Устанавливаем обработчики нажатия для кнопок
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegistrationFragment(); // Переход к фрагменту регистрации
            }
        });

        buttonTermsOfService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToTermsFragment(); // Переход к фрагменту с условиями обслуживания
            }
        });

        buttonPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToPrivacyPolicyFragment(); // Переход к фрагменту с политикой конфиденциальности
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLoginFragment(); // Переход к фрагменту входа в систему
            }
        });

        buttonChangeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showThemeDialog(); // Отображение диалога выбора темы
            }
        });

        buttonWipeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWipeDataDialog(); // Отображение диалога сброса данных
            }
        });

        buttonChangeNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppNotificationSettings(); // Открытие настроек уведомлений приложения
            }
        });

        buttonAboutApp.setOnClickListener(new View.OnClickListener() { // Устанавливаем обработчик нажатия для кнопки версии приложения
            @Override
            public void onClick(View v) {
                showAppVersion(); // Отображение версии приложения
            }
        });

        buttonAccount.setOnClickListener(new View.OnClickListener() { // Обработчик нажатия для кнопки аккаунта
            @Override
            public void onClick(View v) {
                navigateToAccountFragment(); // Переход к фрагменту аккаунта
            }
        });

        buttonGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToGuideFragment(); // Переход к фрагменту краткого обучения
            }
        });

        return view;
    }

    // Метод для перехода к фрагменту регистрации
    private void navigateToRegistrationFragment() {
        RegistrationFragment registrationFragment = new RegistrationFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, registrationFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Метод для перехода к фрагменту входа в систему
    private void navigateToLoginFragment() {
        LoginFragment loginFragment = new LoginFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, loginFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Метод для перехода к фрагменту аккаунта
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

    // Метод для отображения диалога выбора темы
    private void showThemeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Выберите тему")
                .setItems(new String[]{"Светлая", "Тёмная"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                saveThemePreference(AppCompatDelegate.MODE_NIGHT_NO); // Сохраняем предпочтение темы
                                break;
                            case 1:
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                saveThemePreference(AppCompatDelegate.MODE_NIGHT_YES); // Сохраняем предпочтение темы
                                break;
                        }
                    }
                })
                .show();
    }

    // Метод для сохранения предпочтения темы
    private void saveThemePreference(int mode) {
        SharedPreferences preferences = requireActivity().getSharedPreferences("theme_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("theme_mode", mode);
        editor.apply();
    }

    // Метод для отображения диалога сброса данных
    private void showWipeDataDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Подтверждение")
                .setMessage("Вы уверены, что хотите удалить все данные и сбросить настройки?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wipeData(); // Вызываем метод сброса данных
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    // Метод для сброса данных
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

    // Метод для открытия настроек уведомлений приложения
    private void openAppNotificationSettings() {
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("android.provider.extra.APP_PACKAGE", requireActivity().getPackageName());
        startActivity(intent);
    }

    // Метод для отображения версии приложения
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

    // Метод для перехода к фрагменту с условиями обслуживания
    private void navigateToTermsFragment() {
        TermsFragment termsFragment = new TermsFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, termsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Метод для перехода к фрагменту с политикой конфиденциальности
    private void navigateToPrivacyPolicyFragment() {
        PrivacyPolicyFragment privacyPolicyFragment = new PrivacyPolicyFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, privacyPolicyFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Метод для перехода к фрагменту краткого обучения
    private void navigateToGuideFragment() {
        GuideFragment guideFragment = new GuideFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, guideFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
