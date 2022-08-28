package com.mirror.todomate_android.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mirror.todomate_android.R;
import com.mirror.todomate_android.classes.Todo;
import com.mirror.todomate_android.viewmodel.TodoListViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoHolder>{

    private List<Todo> todos = new ArrayList<>();
    private onItemClickListener listener;
    private onItemCheckedListener checkedListener;
    private String myUid;

    public TodoAdapter(String myUid) {
        this.myUid = myUid;
    }

    @NonNull
    @NotNull
    @Override
    public TodoHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);
        return new TodoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TodoHolder holder, int position) {
        Todo currentTodo = todos.get(position);
        holder.todo_complete_view.setVisibility(View.GONE);

        if (myUid.equals(currentTodo.getUid())) {
            holder.todo_complete_view.setVisibility(View.VISIBLE);
            holder.complete.setOnCheckedChangeListener(null);
            holder.complete.setChecked(currentTodo.isComplete());
            holder.complete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    currentTodo.setComplete(b);
                }
            });
        }

        holder.title.setText(currentTodo.getTitle());
        holder.time.setText(currentTodo.getHour() + ":" + currentTodo .getMinute());
        holder.content.setText(currentTodo.getContent());

    }

    @Override
    public int getItemCount() {
        return todos == null ? 0 : todos.size() ;
    }

    public void setTodos(List<Todo> todos) {
        this.todos = todos;
        notifyDataSetChanged();
    }

    public Todo getTodoAt(int position) {
        return todos.get(position);
    }

    class TodoHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView time;
        private TextView content;
        private CheckBox complete;
        private LinearLayout todo_complete_view;

        public TodoHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            content = itemView.findViewById(R.id.content);
            complete = itemView.findViewById(R.id.complete);
            todo_complete_view = itemView.findViewById(R.id.todo_complete_view);


            complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (checkedListener != null && position != RecyclerView.NO_POSITION) {
                        checkedListener.onItemChecked(todos.get(position), position);
                    }
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(todos.get(position), position);
                    }
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(Todo todo, int position);
    }

    public interface onItemCheckedListener {
        void onItemChecked(Todo todo, int position);
    }


    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemCheckedListener(onItemCheckedListener checkedListener) {this.checkedListener = checkedListener;}

}
