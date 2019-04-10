package com.efhems.newinsideproject.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.efhems.newinsideproject.R;
import com.efhems.newinsideproject.data.local.entities.Task;

import java.util.List;

public class TasksReminderRecyclerAdapter extends RecyclerView.Adapter<TasksReminderRecyclerAdapter.MyProjectViewHolder> {

    private List<Task> taskList;

    private OnTaskClickListener onTaskClickListener;
    Context context;

    public interface OnTaskClickListener {
        void onTaskClick(Task task, TextView tvPriority);

        void onMoreVertClick(Task task, ImageView moreVert);
    }

    public TasksReminderRecyclerAdapter(Context context, OnTaskClickListener onTaskClickListener) {
        this.onTaskClickListener = onTaskClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public MyProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_reminder_layout, parent, false);
        return new MyProjectViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProjectViewHolder holder, int position) {

        Task task = taskList.get(holder.getAdapterPosition());

        holder.tvTask.setText(task.getTitle());
        holder.tvDesc.setText(task.getDescription());

        int priority = task.getPriority();

        holder.tvPriority.setText(String.valueOf(priority));
        if (priority == 1) {
            holder.tvPriority.setBackgroundResource(R.drawable.red_fill_selected_bg);
            holder.tvPriority.setTextAppearance(context, R.style.selected_priority_red);
        } else if (priority == 2) {
            holder.tvPriority.setBackgroundResource(R.drawable.green_fill_selected_bg);
            holder.tvPriority.setTextAppearance(context, R.style.selected_priority_green);
        } else if (priority == 3) {
            holder.tvPriority.setBackgroundResource(R.drawable.blue_fill_selected_bg);
            holder.tvPriority.setTextAppearance(context, R.style.selected_priority_blue);
        } else {
            holder.tvPriority.setBackgroundResource(R.drawable.blue_fill_selected_bg);
            holder.tvPriority.setTextAppearance(context, R.style.unselected_priority);
        }

    }

    @Override
    public int getItemCount() {
        if (taskList == null) {
            return 0;
        } else return taskList.size();
    }

    public void setTasks(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public class MyProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTask, tvDesc, tvPriority;
        ImageView imgPopUpMenu;
        ConstraintLayout constraintLayout;

        public MyProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            //itemView.setOnClickListener(this);
            constraintLayout = itemView.findViewById(R.id.container_task);
            constraintLayout.setOnClickListener(this);
            tvPriority = itemView.findViewById(R.id.tv_priority);
            tvTask = itemView.findViewById(R.id.tv_task);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            imgPopUpMenu = itemView.findViewById(R.id.more_btn);
            imgPopUpMenu.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();

            switch (id){
                case R.id.container_task:
                    onTaskClickListener.onTaskClick(taskList.get(getAdapterPosition()), tvPriority);
                    break;
                case R.id.more_btn:
                    onTaskClickListener.onMoreVertClick(taskList.get(getAdapterPosition()),imgPopUpMenu);
                    break;


            }
        }
    }
}

