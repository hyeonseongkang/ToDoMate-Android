package com.mirror.todomate_android.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;

import com.mirror.todomate_android.R;
import com.mirror.todomate_android.databinding.ActivityCalendarBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CalendarActivity extends AppCompatActivity {

    public final static String TAG = "CalendarActivity";

    ActivityCalendarBinding binding;

    String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalendarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.fadein_up, R.anim.none);

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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                if (date == null || date.equals("")) {
                    LocalDate now = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String today = now.format(formatter);

                    date = today;
                }

                Intent data = new Intent();
                data.putExtra("date", date);
                Log.d(TAG, date);

                setResult(RESULT_OK, data);
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_up);
            }
        });


    }
}