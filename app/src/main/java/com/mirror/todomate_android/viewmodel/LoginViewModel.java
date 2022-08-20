package com.mirror.todomate_android.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.mirror.todomate_android.classes.Todo;
import com.mirror.todomate_android.classes.User;
import com.mirror.todomate_android.model.LoginRepository;

import java.util.List;

public class LoginViewModel extends AndroidViewModel {
    private LoginRepository repository;
    private LiveData<FirebaseUser> userData;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = new LoginRepository(application);
        userData = repository.getUser();
    }

    public LiveData<FirebaseUser> getUser() { return userData; }

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

