package com.mirror.todomate_android.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import com.mirror.todomate_android.R;
import com.mirror.todomate_android.databinding.ActivityCalendarBinding;
import com.mirror.todomate_android.databinding.ActivityMainBinding;

public class CalendarActivity extends AppCompatActivity {

    public final static String TAG = "CalendarActivity";

    ActivityCalendarBinding binding;

    String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalendarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.fadein, R.anim.none);

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String year = String.valueOf(i);
                String month = String.valueOf(i1 + 1);
                String day = String.valueOf(i2);
                if (i1 < 10) {
                    month = "0" + (i1 + 1);
                }

                date = year + "-" + month + "-" + day;

            }
        });


        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent data = new Intent();
                data.putExtra("date", date);

                setResult(RESULT_OK, data);
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout);
            }
        });


    }
}