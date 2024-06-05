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

    private String username;
    private String email;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Restore saved instance state
        if (savedInstanceState != null) {
            username = savedInstanceState.getString("username");
            email = savedInstanceState.getString("email");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Если данные не переданы напрямую, читаем их из SharedPreferences
        if (username == null || email == null) {
            SharedPreferences preferences = getActivity().getSharedPreferences("user_preferences", Context.MODE_PRIVATE);
            username = preferences.getString("username", "");
            email = preferences.getString("email", "");
        }

        // Display username and email
        TextView usernameTextView = view.findViewById(R.id.textViewUsername);
        TextView emailTextView = view.findViewById(R.id.textViewEmail);

        usernameTextView.setText("Имя пользователя: " + (username != null ? username : ""));
        emailTextView.setText("Email: " + (email != null ? email : ""));

        return view;
    }


    // Save instance state
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("username", username);
        outState.putString("email", email);
    }

    // Setter methods for username and email
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
