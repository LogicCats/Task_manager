package com.example.taskmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TaskListAdapter extends ArrayAdapter<Task> {

    public TaskListAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_list_item, parent, false);
        }

        TextView taskNameTextView = convertView.findViewById(R.id.task_name);
        TextView taskTagsTextView = convertView.findViewById(R.id.task_tags);

        taskNameTextView.setText(task.getName());
        taskTagsTextView.setText(task.getTags());

        return convertView;
    }

    public void addTask(Task task) {
        add(task);
    }
}
