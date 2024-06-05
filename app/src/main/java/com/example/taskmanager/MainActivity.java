package com.example.taskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.taskmanager.databinding.ActivityMainBinding;

/**
 * Основная активность приложения.
 */
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding; // Привязка для активности
    SharedPreferences usagePreferences; // SharedPreferences для хранения времени использования приложения
    long startTime; // Время запуска приложения

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Загрузка сохраненной темы перед вызовом super.onCreate
        SharedPreferences preferences = getSharedPreferences("theme_preferences", MODE_PRIVATE);
        int themeMode = preferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(themeMode);

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        usagePreferences = getSharedPreferences("app_usage_preferences", Context.MODE_PRIVATE);
        startTime = System.currentTimeMillis();

        replaceFragment(new HomeFragment()); // Замена текущего фрагмента на HomeFragment

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Обработчик для нижней навигационной панели
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.tasks) {
                replaceFragment(new TasksFragment());
            } else if (itemId == R.id.calendar) {
                replaceFragment(new CalendarFragment());
            } else if (itemId == R.id.settings) {
                replaceFragment(new SettingsFragment());
            } else if (itemId == R.id.statistics) {
                replaceFragment(new StatisticsFragment());
            }
            return true;
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateAppUsageTime(); // Обновление времени использования приложения при приостановке
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime = System.currentTimeMillis(); // Обновление времени запуска приложения
    }

    /**
     * Метод для обновления времени использования приложения.
     */
    private void updateAppUsageTime() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        int elapsedMinutes = (int) (elapsedTime / (1000 * 60));
        SharedPreferences.Editor editor = usagePreferences.edit();
        int totalUsageTime = usagePreferences.getInt("app_usage_time", 0) + elapsedMinutes;
        editor.putInt("app_usage_time", totalUsageTime);
        editor.apply();
    }

    /**
     * Метод для замены текущего фрагмента на другой.
     *
     * @param fragment Фрагмент, который нужно отобразить.
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upper_nav_menu, menu);

        // Получение элементов меню
        MenuItem fastCreationTaskItem = menu.findItem(R.id.fast_creation_task);
        MenuItem creationTaskItem = menu.findItem(R.id.creation_task);

        // Установка цвета значков
        Drawable fastCreationTaskIcon = fastCreationTaskItem.getIcon();
        if (fastCreationTaskIcon != null) {
            fastCreationTaskIcon.setTint(ContextCompat.getColor(this, R.color.primary));
            fastCreationTaskItem.setIcon(fastCreationTaskIcon);
        }

        Drawable creationTaskIcon = creationTaskItem.getIcon();
        if (creationTaskIcon != null) {
            creationTaskIcon.setTint(ContextCompat.getColor(this, R.color.primary));
            creationTaskItem.setIcon(creationTaskIcon);
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.fast_creation_task) {
            openFastCreationTaskFragment(); // Открывает фрагмент быстрого создания задачи
            return true;
        } else if (id == R.id.creation_task) {
            openCreationTaskFragment(); // Открывает фрагмент создания задачи
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Метод для открытия фрагмента быстрого создания задачи.
     */
    private void openFastCreationTaskFragment() {
        FastCreationTaskFragment fastCreationTaskFragment = new FastCreationTaskFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fastCreationTaskFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Метод для открытия фрагмента создания задачи.
     */
    private void openCreationTaskFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, new CreationTaskFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Метод для переключения видимости дополнительных деталей.
     *
     * @param view Кнопка, вызвавшая метод.
     */
    public void toggleDetailsVisibility(View view) {
        LinearLayout detailsLayout = findViewById(R.id.details_layout);
        if (detailsLayout.getVisibility() == View.VISIBLE) {
            detailsLayout.setVisibility(View.GONE);
            ((Button) view).setText("Show Details");
        } else {
            detailsLayout.setVisibility(View.VISIBLE);
            ((Button) view).setText("Hide Details");
        }
    }
}
