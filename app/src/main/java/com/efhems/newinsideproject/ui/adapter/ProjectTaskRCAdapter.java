package com.efhems.newinsideproject.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.efhems.newinsideproject.R;
import com.efhems.newinsideproject.data.local.entities.Project;
import com.efhems.newinsideproject.data.local.entities.ProjectTask;

import java.util.List;

public class ProjectTaskRCAdapter extends RecyclerView.Adapter<ProjectTaskRCAdapter.MyProjectViewHolder> {


    private Context mContext;


    private List<ProjectTask> projectTasks;
    public ProjectTaskRCAdapter(Context context) {
        this.mContext = context;
    }



    @NonNull
    @Override
    public MyProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_task_item, parent, false);
        return new MyProjectViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProjectViewHolder holder, int position) {

        ProjectTask projectTask = projectTasks.get(position);
        String projectName = projectTask.getProjectName();
        holder.tvCap.setText(String.valueOf(projectName.charAt(0)).toUpperCase());
        holder.tvProjectName.setText(projectName);
        holder.tvTaskname.setText(projectTask.getTask().getTitle());
        holder.tvTaskDesc.setText(projectTask.getTask().getDescription());
    }

    @Override
    public int getItemCount() {
        if(projectTasks == null){
            return 0;
        }else return projectTasks.size();
    }

    /*public List<ProjectTask> getProjectList() {
        return projectTasks;
    }*/

    public void setProject(List<ProjectTask> projectTasks){
        this.projectTasks = projectTasks;
        notifyDataSetChanged();
    }

    public class MyProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private final TextView tvCap;
        private final TextView tvProjectName;
        private final TextView tvTaskname;
        private final TextView tvTaskDesc;

        public MyProjectViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCap = itemView.findViewById(R.id.cap_project);
            tvProjectName = itemView.findViewById(R.id.name_Project);
            tvTaskname = itemView.findViewById(R.id.name_task);
            tvTaskDesc = itemView.findViewById(R.id.task_description);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
