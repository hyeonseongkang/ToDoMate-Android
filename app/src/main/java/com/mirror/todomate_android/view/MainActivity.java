package com.mirror.todomate_android.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

import com.google.firebase.auth.FirebaseUser;
import com.mirror.todomate_android.classes.Todo;
import com.mirror.todomate_android.databinding.ActivityMainBinding;
import com.mirror.todomate_android.viewmodel.LoginViewModel;
import com.mirror.todomate_android.viewmodel.TodoListViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    public static final String TAG = "MainActivity";

    private TodoListViewModel todoListViewModel;
    private LoginViewModel loginViewModel;
    private ActivityMainBinding binding;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        todoListViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(TodoListViewModel.class);
        todoListViewModel.getAllTodos().observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todos) {
                for (Todo todo: todos) {
                    Log.d(TAG, todo.getNickName());
                    Log.d(TAG, todo.getKey());
                }
            }
        });

        loginViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);
        loginViewModel.loginCheck();
        user = loginViewModel.getUser().getValue();
        Log.d(TAG, user.getEmail() + " !!!!! " + user.getUid());



        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(binding.calendarView.getDate());
        binding.today.setText(format.format(date));

        todoListViewModel.getTodos(user.getUid(), format.format(date));
        Log.d(TAG, format.format(date));

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String year = String.valueOf(i);
                String month = String.valueOf(i1 + 1);
                String day = String.valueOf(i2);
                if (i1 < 10) {
                    month = "0" + (i1 + 1);
                }

                String date = year + "-" + month + "-" + day;
                binding.today.setText(date);
                todoListViewModel.getTodos(user.getUid(), date);
                //todoListViewModel.insertTodo(user.getUid(), date, new Todo(null, user.getEmail(), date, "testContent"));
            }
        });

    }
}