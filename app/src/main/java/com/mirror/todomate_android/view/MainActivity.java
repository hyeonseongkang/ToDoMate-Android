package com.mirror.todomate_android.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.mirror.todomate_android.R;
import com.mirror.todomate_android.Todo;
import com.mirror.todomate_android.databinding.ActivityMainBinding;
import com.mirror.todomate_android.viewmodel.TodoListViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    public static final String TAG = "MainActivity";

    private TodoListViewModel todoListViewModel;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        todoListViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(TodoListViewModel.class);
        todoListViewModel.getAllTodos().observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todos) {

            }
        });

        DateFormat format = new SimpleDateFormat("yyyy MM dd");
        Date date = new Date(binding.calendarView.getDate());
        binding.today.setText(format.format(date));

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String day = i + "년 " + (i1 + 1) + "월 " + i2 + "일";
                binding.today.setText(day);
                todoListViewModel.insertTodo(new Todo(null, "testNickName", day, "testContent"));
            }
        });

    }
}