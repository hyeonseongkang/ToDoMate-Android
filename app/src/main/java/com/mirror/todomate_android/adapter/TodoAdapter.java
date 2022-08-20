package com.mirror.todomate_android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mirror.todomate_android.R;
import com.mirror.todomate_android.classes.Todo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoHolder>{

    private List<Todo> todos = new ArrayList<>();
    private onItemClickListener listener;

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
        holder.title.setText(currentTodo.getTitle());
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

        public TodoHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);

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

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

}
