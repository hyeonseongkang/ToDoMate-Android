package com.mirror.todomate_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarHolder>{

    private final List<String> daysOfMonth;
    private final OnItemListener onItemListener;

    public CalendarAdapter(List<String> daysOfMonth, OnItemListener onItemListener) {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
    }

    public class CalendarHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView dayOfMonth;
        private final CalendarAdapter.OnItemListener onItemListener;

        public CalendarHolder(View itemView, CalendarAdapter.OnItemListener onItemListener) {
            super(itemView);
            dayOfMonth = itemView.findViewById(R.id.cell_day);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemListener.onItemClick(getAdapterPosition(), (String) dayOfMonth.getText());
        }
    }


    @NonNull
    @NotNull
    @Override
    public CalendarAdapter.CalendarHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.1666666666);
        return new CalendarHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CalendarAdapter.CalendarHolder holder, int position) {
        holder.dayOfMonth.setText(daysOfMonth.get(position));
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public interface OnItemListener {
        void onItemClick(int position, String dayText);
    }
}
