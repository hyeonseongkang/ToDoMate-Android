package com.mirror.todomate_android.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mirror.todomate_android.classes.Todo;
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

    public LiveData<List<Todo>> getAllTodos() { return allTodos; }

    public void insertTodo(String id, String date, Todo todo) { repository.insertTodo(id, date, todo);}

    public void updateTodo(String id, String date, Todo todo, int position) {
        repository.updateTodo(id, date, todo, position);
    }

    public void deleteTodo(String id, String date, Todo todo, int position) {
        repository.deleteTodo(id, date, todo, position);
    }

    public void getTodos(String id, String date) {
        repository.getTodos(id, date);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getToday() { return repository.getToday();}



}
