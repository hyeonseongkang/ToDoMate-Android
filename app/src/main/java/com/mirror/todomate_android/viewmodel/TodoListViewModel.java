package com.mirror.todomate_android.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mirror.todomate_android.Todo;
import com.mirror.todomate_android.model.TodoListRepository;

import java.util.List;

public class TodoListViewModel extends AndroidViewModel {

    private TodoListRepository repository;
    private LiveData<List<Todo>> allTodos;

    public TodoListViewModel(@NonNull Application application) {
        super(application);
        repository = new TodoListRepository(application);
        allTodos = repository.getAllTodos();
    }
}
