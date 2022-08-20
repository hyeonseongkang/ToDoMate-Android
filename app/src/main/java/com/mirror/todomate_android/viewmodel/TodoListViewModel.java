package com.mirror.todomate_android.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
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

    public void insertTodo(String id, String date, Todo todo) { repository.insertTodo(id, date, todo);}

    public void deleteTodo(String id, String date, Todo todo, int position) {
        repository.deleteTodo(id, date, todo, position);
    }

    public void getTodos(String id, String date) {
        repository.getTodos(id, date);
    }

    public LiveData<List<Todo>> getAllTodos() { return allTodos; }
}
