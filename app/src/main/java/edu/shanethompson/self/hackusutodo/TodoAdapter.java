package edu.shanethompson.self.hackusutodo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 *
 * Created by shanethompson on 4/8/16.
 *
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private List<Todo> todoList;

    public TodoAdapter(List<Todo> todoList) {
        this.todoList = todoList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView taskTextView;
        public TextView checkTextView;

        public ViewHolder(LinearLayout itemView) {
            super(itemView);
            taskTextView = (TextView) itemView.findViewById(R.id.task_text);
            checkTextView = (TextView) itemView.findViewById(R.id.checkmark_text);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout view = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_todo_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Todo item = todoList.get(position);
        holder.taskTextView.setText(item.getTask());
        if(item.isComplete()) {
            holder.checkTextView.setVisibility(View.VISIBLE);
        } else {
            holder.checkTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }
}
