package com.mirror.todomate_android;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarHolder>{

    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener) {
        this.days = days;
        this.onItemListener = onItemListener;
    }

    public class CalendarHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ArrayList<LocalDate> days;
        public final View parentView;
        private final TextView dayOfMonth;
        private final OnItemListener onItemListener;

        public CalendarHolder( View itemView, OnItemListener onItemListener, ArrayList<LocalDate> days) {
            super(itemView);
            parentView = itemView.findViewById(R.id.parentView);;
            dayOfMonth = itemView.findViewById(R.id.cell_day);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
            this.days = days;
        }

        @Override
        public void onClick(View view) {
            onItemListener.onItemClick(getAdapterPosition(), days.get(getAdapterPosition()));
        }
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
        return new CalendarHolder(view, onItemListener, days);
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
       // holder.dayOfMonth.setText(days.get(position));
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface OnItemListener {
        void onItemClick(int position, LocalDate date);
    }
}
