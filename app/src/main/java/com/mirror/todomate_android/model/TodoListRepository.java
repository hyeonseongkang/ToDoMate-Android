package com.mirror.todomate_android.model;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mirror.todomate_android.classes.Todo;
import com.mirror.todomate_android.classes.UserProfile;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TodoListRepository {

    public static final String TAG = "TodoListRepository";

    private DatabaseReference myRef;
    private MutableLiveData<List<Todo>> allTodos;
    private MutableLiveData<String> currentUser;
    private List<Todo> todos;

    public TodoListRepository(Application application) {
        myRef = FirebaseDatabase.getInstance().getReference("todos");
        todos = new ArrayList<>();
        allTodos = new MutableLiveData<>();
        currentUser = new MutableLiveData<>();
       // init();
    }

    public LiveData<List<Todo>> getAllTodos() {
        return allTodos;
    }

    public LiveData<String> getCurrentUser() { return currentUser; }

    public void insertTodo(String id, String date, Todo todo) {
        String key = myRef.push().getKey();
        todo.setKey(key);
        myRef.child(id).child(date).child(key).setValue(todo);
        todos.add(todo);
        allTodos.setValue(todos);
    }

    public void updateTodo(String id, String date, Todo todo, int position) {
        Log.d(TAG, id);
        Log.d(TAG, date);
        Log.d(TAG, todo.getKey());
        myRef.child(id).child(date).child(todo.getKey()).setValue(todo);
        todos.get(position).setTitle(todo.getTitle());
        todos.get(position).setContent(todo.getContent());
        todos.get(position).setHour(todo.getHour());
        todos.get(position).setMinute(todo.getMinute());
        allTodos.setValue(todos);
    }

    public void deleteTodo(String id, String date, Todo todo, int position) {
        myRef.child(id).child(date).child(todo.getKey()).removeValue();
        todos.remove(position);
        allTodos.setValue(todos);
    }

    public void getTodos(String id, String date) {
        myRef.child(id).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                todos.clear();
                //Log.d(TAG, String.valueOf(snapshot.getValue()));
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Todo todo = snapshot1.getValue(Todo.class);
                    todos.add(todo);
                }

                DatabaseReference profilesRef = FirebaseDatabase.getInstance().getReference("profiles");
                profilesRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        UserProfile userProfile = snapshot.getValue(UserProfile.class);
                        if (userProfile != null)
                            currentUser.setValue(userProfile.getNickName());
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

                allTodos.setValue(todos);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getToday() {
        // 현재 날짜 구하기
        LocalDate now = LocalDate.now();
        // 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 포맷 적용
        String today = now.format(formatter);
        return today;
    }
}
