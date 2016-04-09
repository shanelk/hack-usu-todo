package edu.shanethompson.self.hackusutodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 *
 * Created by shanethompson on 4/8/16.
 *
 */

public class TodoAdapter extends ArrayAdapter<Todo> {
    private int layoutId;
    private TodoClickedListener listener;

    public TodoAdapter(Context context, int resource, List<Todo> objects, TodoClickedListener listener) {
        super(context, resource, objects);
        layoutId = resource;
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layoutId, null);
            viewHolder = new ViewHolder();
            viewHolder.taskText = (TextView) convertView.findViewById(R.id.task_text);
            viewHolder.checkText = (TextView) convertView.findViewById(R.id.checkmark_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Todo item = getItem(position);
        viewHolder.taskText.setText(item.getTask());
        viewHolder.taskText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.todoClicked(position);
            }
        });
        if(item.isComplete()) {
            viewHolder.checkText.setVisibility(View.VISIBLE);
        } else {
            viewHolder.checkText.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    public static class ViewHolder {
        public TextView taskText;
        public TextView checkText;
    }

    interface TodoClickedListener {
        void todoClicked(int position);
    }
}
