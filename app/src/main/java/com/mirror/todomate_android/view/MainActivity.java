package com.mirror.todomate_android.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;

import com.google.firebase.auth.FirebaseUser;
import com.mirror.todomate_android.adapter.TodoAdapter;
import com.mirror.todomate_android.classes.Todo;
import com.mirror.todomate_android.databinding.ActivityMainBinding;
import com.mirror.todomate_android.viewmodel.LoginViewModel;
import com.mirror.todomate_android.viewmodel.TodoListViewModel;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    public static final String TAG = "MainActivity";

    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    private TodoListViewModel todoListViewModel;
    private LoginViewModel loginViewModel;
    private ActivityMainBinding binding;
    private FirebaseUser user;

    String selected_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.mainRecyclerView.setHasFixedSize(true);

        TodoAdapter todoAdapter = new TodoAdapter();
        binding.mainRecyclerView.setAdapter(todoAdapter);

        todoAdapter.setOnItemClickListener(new TodoAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Todo todo, int position) {
                Intent intent = new Intent(MainActivity.this, AddEditTodoActivity.class);
                intent.putExtra(AddEditTodoActivity.EXTRA_POSITION, position);
                intent.putExtra(AddEditTodoActivity.EXTRA_KEY, todo.getKey());
                intent.putExtra(AddEditTodoActivity.EXTRA_EMAIL, todo.getEmail());
                intent.putExtra(AddEditTodoActivity.EXTRA_DATE, todo.getDate());
                intent.putExtra(AddEditTodoActivity.EXTRA_TITLE, todo.getTitle());
                intent.putExtra(AddEditTodoActivity.EXTRA_CONTENT, todo.getContent());
                editLauncher.launch(intent);
            }
        });

        todoListViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(TodoListViewModel.class);
        todoListViewModel.getAllTodos().observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todos) {
                todoAdapter.setTodos(todos);
            }
        });


        loginViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);
        loginViewModel.loginCheck();
        user = loginViewModel.getUser().getValue();


        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(binding.calendarView.getDate());
        binding.today.setText(format.format(date));
        selected_date = format.format(date);
        todoListViewModel.getTodos(user.getUid(), format.format(date));
        Log.d(TAG, format.format(date));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Todo todo = todoAdapter.getTodoAt(viewHolder.getAdapterPosition());
                todoListViewModel.deleteTodo(user.getUid(), todo.getDate(), todo, position);
            }
        }).attachToRecyclerView(binding.mainRecyclerView);

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
                selected_date = date;
                //todoListViewModel.insertTodo(user.getUid(), date, new Todo(null, user.getEmail(), date, "testContent"));
            }
        });

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditTodoActivity.class);
                intent.putExtra(AddEditTodoActivity.EXTRA_DATE, selected_date);
                addLauncher.launch(intent);
            }
        });

    }


    ActivityResultLauncher<Intent> editLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();

                        int position = intent.getIntExtra(AddEditTodoActivity.EXTRA_POSITION, -1);
                        String key = intent.getStringExtra(AddEditTodoActivity.EXTRA_KEY);
                        String email = intent.getStringExtra(AddEditTodoActivity.EXTRA_EMAIL);
                        String date = intent.getStringExtra(AddEditTodoActivity.EXTRA_DATE);
                        String title = intent.getStringExtra(AddEditTodoActivity.EXTRA_TITLE);
                        String content = intent.getStringExtra(AddEditTodoActivity.EXTRA_CONTENT);

                        // //String key, String title, String email, String date, String content
                        Todo todo = new Todo(key, title, email, date, content);

                        Log.d(TAG, user.getUid());
                        Log.d(TAG, key);
                        Log.d(TAG, date);
                        todoListViewModel.updateTodo(user.getUid(), date, todo, position);


                    }

                }
            });

    ActivityResultLauncher<Intent> addLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();

                        String date = intent.getStringExtra(AddEditTodoActivity.EXTRA_DATE);
                        String title = intent.getStringExtra(AddEditTodoActivity.EXTRA_TITLE);
                        String content = intent.getStringExtra(AddEditTodoActivity.EXTRA_CONTENT);

                        //String key, String title, String email, String date, String content
                        todoListViewModel.insertTodo(user.getUid(), date, new Todo(null, title, user.getEmail(), date, content));

                    }

                }
            });
}