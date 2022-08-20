package com.mirror.todomate_android.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mirror.todomate_android.classes.Todo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TodoListRepository {

    public static final String TAG = "TodoListRepository";

    private DatabaseReference myRef;
    private MutableLiveData<List<Todo>> allTodos;
    private List<Todo> todos;

    public TodoListRepository(Application application) {
        myRef = FirebaseDatabase.getInstance().getReference("todos");
        todos = new ArrayList<>();
        allTodos = new MutableLiveData<>();
       // init();
    }

    public LiveData<List<Todo>> getAllTodos() {
        return allTodos;
    }

    public void insertTodo(String id, String date, Todo todo) {
        String key = myRef.push().getKey();
        todo.setKey(key);
        myRef.child(id).child(date).child(key).setValue(todo);
    }

    public void getTodos(String id, String date) {
        myRef.child(id).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                todos.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Todo todo = snapshot1.getValue(Todo.class);
                    todos.add(todo);
                    Log.d(TAG, todo.getNickName());
                    Log.d(TAG, todo.getKey());
                }
                allTodos.setValue(todos);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void init() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    todos.add(snapshot1.getValue(Todo.class));
                }
                allTodos.setValue(todos);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}
