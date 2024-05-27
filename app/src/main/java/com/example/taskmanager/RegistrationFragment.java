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

import com.example.taskmanager.DatabaseHelper;

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
        databaseHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextConfirmPassword = view.findViewById(R.id.editTextConfirmPassword);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        buttonRegister = view.findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        return view;
    }

    private void register() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.contains("@")) {
            Toast.makeText(getActivity(), "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Проверяем, существует ли уже пользователь с указанным именем или email
        if (databaseHelper.checkUserExists(username, email)) {
            Toast.makeText(getActivity(), "User already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        if (databaseHelper.addUser(username, password, email)) {
            Toast.makeText(getActivity(), "Registration successful", Toast.LENGTH_SHORT).show();
            // Переход на фрагмент SettingsFragment
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
