package com.mirror.todomate_android;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;

import static com.mirror.todomate_android.CalendarUtils.daysInWeekArray;
import static com.mirror.todomate_android.CalendarUtils.monthYearFromDate;

public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private Button previousMonth, nextMonth;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        initViews();
        setWeekView();

        previousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
                setWeekView();
            }
        });

        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
                setWeekView();
            }
        });

    }

    private void initViews() {
        calendarRecyclerView = findViewById(R.id.calendar_recyclerview);
        monthYearText = findViewById(R.id.month_year);
        previousMonth = findViewById(R.id.previous_month);
        nextMonth = findViewById(R.id.next_month);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, LocalDate date)
    {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }
}