package com.example.taskmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * Фрагмент для входа в приложение.
 */
public class LoginFragment extends Fragment {

    private DatabaseHelper databaseHelper; // Помощник для работы с базой данных

    private EditText editTextUsername; // Поле для ввода имени пользователя
    private EditText editTextPassword; // Поле для ввода пароля
    private Button buttonLogin; // Кнопка для входа

    /**
     * Вызывается при создании фрагмента.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity()); // Инициализация помощника базы данных
    }

    /**
     * Создает и настраивает пользовательский интерфейс фрагмента.
     *
     * @param inflater           объект, который может преобразовать XML-файл макета в объекты View.
     * @param container          родительский объект View, к которому будет присоединен макет фрагмента.
     * @param savedInstanceState объект, содержащий данные о состоянии фрагмента.
     * @return View, представляющий пользовательский интерфейс фрагмента.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonLogin = view.findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(); // Вызов метода входа
            }
        });

        return view;
    }

    /**
     * Метод для входа в приложение.
     */
    private void login() {
        String username = editTextUsername.getText().toString().trim(); // Получение имени пользователя
        String password = editTextPassword.getText().toString().trim(); // Получение пароля

        // Проверка существования пользователя в базе данных
        if (databaseHelper.checkUser(username, password)) {
            Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();

            // Получение адреса электронной почты пользователя из базы данных
            String email = databaseHelper.getUserEmail(username);

            // Сохранение имени пользователя и адреса электронной почты в SharedPreferences
            SharedPreferences preferences = getActivity().getSharedPreferences("user_preferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("username", username);
            editor.putString("email", email);
            editor.apply();

            // Переход к AccountFragment
            AccountFragment accountFragment = new AccountFragment();
            accountFragment.setUsername(username);
            accountFragment.setEmail(email);

            // Замена текущего фрагмента на AccountFragment
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, accountFragment, "AccountFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            Toast.makeText(getActivity(), "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
