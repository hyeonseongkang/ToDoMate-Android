package com.mirror.todomate_android.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mirror.todomate_android.User;
import com.mirror.todomate_android.model.LoginRepository;

import java.util.List;

public class LoginViewModel extends AndroidViewModel {
    private LoginRepository repository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = new LoginRepository(application);
    }

    public void login(User user) {
        repository.login(user);
    }

    public void register(User user) {
        repository.register(user);
    }

    public void logout() {
        repository.logout();
    }

    public void loginCheck() {
        repository.loginCheck();
    }

}

