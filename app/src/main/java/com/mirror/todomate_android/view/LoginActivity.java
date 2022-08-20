package com.mirror.todomate_android.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mirror.todomate_android.R;
import com.mirror.todomate_android.User;
import com.mirror.todomate_android.databinding.ActivityLoginBinding;
import com.mirror.todomate_android.viewmodel.LoginViewModel;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = binding.userId.getText().toString();
                String pw = binding.userPassword.getText().toString();

                if (id.length() <= 0 || pw.length() <= 0) {
                    Toast.makeText(LoginActivity.this, "입력 사항을 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                loginViewModel.register(new User(id, pw));

            }
        });

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = binding.userId.getText().toString();
                String pw = binding.userPassword.getText().toString();

                if (id.length() <= 0 || pw.length() <= 0) {
                    Toast.makeText(LoginActivity.this, "입력 사항을 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                loginViewModel.login(new User(id, pw));

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        loginViewModel.loginCheck();

    }
}