package com.mirror.todomate_android.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mirror.todomate_android.classes.User;
import com.mirror.todomate_android.model.FriendSearchRepository;

import java.util.List;

public class FriendSearchViewModel extends AndroidViewModel {
    private FriendSearchRepository repository;
    private LiveData<List<User>> allUsers;

    public FriendSearchViewModel(@NonNull Application application) {
        super(application);
        repository = new FriendSearchRepository(application);
        allUsers = repository.getAllUsers();
    }

    public void getUsers() {
        repository.getUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

}
