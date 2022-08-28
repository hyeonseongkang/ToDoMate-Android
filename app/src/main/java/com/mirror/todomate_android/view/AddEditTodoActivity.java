package com.mirror.todomate_android.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import com.mirror.todomate_android.R;
import com.mirror.todomate_android.databinding.ActivityAddEditTodoBinding;

public class AddEditTodoActivity extends AppCompatActivity {

    public static final String TAG = "EditTodoActivity";

    public static final String EXTRA_POSITION = "com.mirror.todomate_android.view.EXTRA_POSITION";
    public static final String EXTRA_KEY = "com.mirror.todomate_android.view.EXTRA_KEY";
    public static final String EXTRA_EMAIL = "com.mirror.todomate_android.view.EXTRA_EMAIL";
    public static final String EXTRA_DATE = "com.mirror.todomate_android.view.EXTRA_DATE";
    public static final String EXTRA_TITLE = "com.mirror.todomate_android.view.EXTRA_TITLE";
    public static final String EXTRA_CONTENT = "com.mirror.todomate_android.view.EXTRA_CONTENT";
    public static final String EXTRA_HOUR = "com.mirror.todomate_android.view.EXTRA_TIME";
    public static final String EXTRA_MINUTE = "com.mirror.todomate_android.view.EXTRA_MINUTE";

    private String hour;
    private String minute;

    private ActivityAddEditTodoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddEditTodoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_KEY)) {
            binding.roomName.setText("todo 수정");
            binding.date.setText(intent.getStringExtra(EXTRA_DATE));
            binding.title.setText(intent.getStringExtra(EXTRA_TITLE));
            binding.content.setText(intent.getStringExtra(EXTRA_CONTENT));
            String getHour = intent.getStringExtra(EXTRA_HOUR);
            String getMinute = intent.getStringExtra(EXTRA_MINUTE);

            if (Build.VERSION.SDK_INT < 23) {
                binding.time.setCurrentHour(Integer.parseInt(getHour));
                binding.time.setCurrentMinute(Integer.parseInt(getMinute));

                Log.d(TAG + "1", getHour);
                Log.d(TAG, getMinute);
            } else {
                binding.time.setHour(Integer.parseInt(getHour));
                binding.time.setMinute(Integer.parseInt(getMinute));

                Log.d(TAG + "2", getHour);
                Log.d(TAG, getMinute);
            }


        } else {
            binding.roomName.setText("todo 추가");
            binding.date.setText(intent.getStringExtra(EXTRA_DATE));
        }

        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_left);
            }
        });

        binding.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = binding.date.getText().toString();
                String title = binding.title.getText().toString();
                String content = binding.content.getText().toString();

                if (Build.VERSION.SDK_INT < 23) {
                    hour = String.valueOf(binding.time.getCurrentHour());
                    minute = String.valueOf(binding.time.getCurrentMinute());
                } else {
                    hour = String.valueOf(binding.time.getHour());
                    minute = String.valueOf(binding.time.getMinute());
                }


                Intent data = new Intent();
                data.putExtra(EXTRA_DATE, date);
                data.putExtra(EXTRA_TITLE, title);
                data.putExtra(EXTRA_CONTENT, content);
                data.putExtra(EXTRA_HOUR, hour);
                data.putExtra(EXTRA_MINUTE, minute);
                Log.d(TAG, hour + " " + minute);

                String key = getIntent().getStringExtra(EXTRA_KEY);
                String email = getIntent().getStringExtra(EXTRA_EMAIL);
                int position = getIntent().getIntExtra(EXTRA_POSITION, -1);


                if (key != null && email != null && position != -1) {
                    Log.d(TAG, key);
                    Log.d(TAG, email);
                    Log.d(TAG, String.valueOf(position));
                    data.putExtra(EXTRA_KEY, key);
                    data.putExtra(EXTRA_EMAIL, email);
                    data.putExtra(EXTRA_POSITION, position);
                }


                setResult(RESULT_OK, data);
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_left);
            }
        });

    }
}