package com.example.taskmanager;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Фрагмент для регистрации нового пользователя.
 */
public class RegistrationFragment extends Fragment {

    private DatabaseHelper databaseHelper;

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private EditText editTextEmail;
    private Button buttonRegister;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Инициализация объекта для работы с базой данных
        databaseHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Загрузка макета для фрагмента
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        // Настройка полей ввода и кнопки
        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextConfirmPassword = view.findViewById(R.id.editTextConfirmPassword);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        buttonRegister = view.findViewById(R.id.buttonRegister);

        // Установка обработчика нажатия кнопки "Register"
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(); // Вызов метода для регистрации пользователя
            }
        });

        return view;
    }

    /**
     * Метод для регистрации нового пользователя.
     */
    private void register() {
        // Получение введенных данных
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        // Проверка на заполнение всех полей
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Проверка на корректность email адреса
        if (!email.contains("@")) {
            Toast.makeText(getActivity(), "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Проверка на совпадение паролей
        if (!password.equals(confirmPassword)) {
            Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Проверка на существование пользователя с таким именем или email
        if (databaseHelper.checkUserExists(username, email)) {
            Toast.makeText(getActivity(), "User already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        // Добавление нового пользователя в базу данных
        if (databaseHelper.addUser(username, password, email)) {
            Toast.makeText(getActivity(), "Registration successful", Toast.LENGTH_SHORT).show();
            // Переход на фрагмент SettingsFragment после успешной регистрации
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new SettingsFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            Toast.makeText(getActivity(), "Registration failed", Toast.LENGTH_SHORT).show();
        }
    }
}
