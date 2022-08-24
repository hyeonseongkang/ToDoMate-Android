package com.mirror.todomate_android.adapter;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.mirror.todomate_android.CalendarUtils;
import com.mirror.todomate_android.R;
import com.mirror.todomate_android.classes.Todo;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarHolder>{

    private ArrayList<LocalDate> days;
    private onItemClickListener listener;

    public CalendarAdapter(ArrayList<LocalDate> days) {
        this.days = days;
    }

    public class CalendarHolder extends RecyclerView.ViewHolder{

        private final ArrayList<LocalDate> days;
        public final View parentView;
        private final TextView dayOfMonth;

        public CalendarHolder( View itemView, ArrayList<LocalDate> days) {
            super(itemView);
            parentView = itemView.findViewById(R.id.parentView);;
            dayOfMonth = itemView.findViewById(R.id.cell_day);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getAdapterPosition(), days.get(getAdapterPosition()));
                    }
                }
            });

            this.days = days;
        }
    }

    public interface onItemClickListener {
        void onItemClick(int position, LocalDate date);
    }

    public void setOnItemClickListener(CalendarAdapter.onItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public CalendarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (days.size() > 15)
            layoutParams.height = (int) (parent.getHeight() * 0.1666666666);
        else
            layoutParams.height = (int) (parent.getHeight());
        return new CalendarHolder(view, days);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CalendarHolder holder, int position) {
        final LocalDate date = days.get(position);
        holder.parentView.setBackgroundColor(Color.WHITE);
        if (date == null)
            holder.dayOfMonth.setText("");
        else {
            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
            if (date.equals(CalendarUtils.selectedDate)) {
                holder.parentView.setBackgroundColor(Color.LTGRAY);
            }
        }

    }

    @Override
    public int getItemCount() {
        return days.size();
    }

}
