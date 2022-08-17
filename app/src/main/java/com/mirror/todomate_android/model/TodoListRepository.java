package com.mirror.todomate_android.model;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mirror.todomate_android.Todo;

import java.util.List;

public class TodoListRepository {

    private LiveData<List<Todo>> allTodos;

    public TodoListRepository(Application application) {

        allTodos = new MutableLiveData<>();
    }

    public LiveData<List<Todo>> getAllTodos() {
        return allTodos;
    }
}
