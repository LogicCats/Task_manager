package com.example.taskmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AccountFragment extends Fragment {

    private String username; // Переменная для хранения имени пользователя
    private String email; // Переменная для хранения email

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Восстановление состояния экземпляра при необходимости
        if (savedInstanceState != null) {
            username = savedInstanceState.getString("username"); // Восстановление имени пользователя из Bundle
            email = savedInstanceState.getString("email"); // Восстановление email из Bundle
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Надуваем макет для этого фрагмента
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Если данные не переданы напрямую, читаем их из SharedPreferences
        if (username == null || email == null) {
            SharedPreferences preferences = getActivity().getSharedPreferences("user_preferences", Context.MODE_PRIVATE);
            username = preferences.getString("username", ""); // Чтение имени пользователя из SharedPreferences
            email = preferences.getString("email", ""); // Чтение email из SharedPreferences
        }

        // Отображаем имя пользователя и email
        TextView usernameTextView = view.findViewById(R.id.textViewUsername); // Поиск TextView для отображения имени пользователя
        TextView emailTextView = view.findViewById(R.id.textViewEmail); // Поиск TextView для отображения email

        usernameTextView.setText("Имя пользователя: " + (username != null ? username : "")); // Установка текста для имени пользователя
        emailTextView.setText("Email: " + (email != null ? email : "")); // Установка текста для email

        return view; // Возвращаем созданный вид
    }


    // Сохранение состояния экземпляра
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("username", username); // Сохранение имени пользователя в Bundle
        outState.putString("email", email); // Сохранение email в Bundle
    }

    // Методы установки имени пользователя и email
    public void setUsername(String username) {
        this.username = username; // Установка имени пользователя
    }

    public void setEmail(String email) {
        this.email = email; // Установка email
    }
}
