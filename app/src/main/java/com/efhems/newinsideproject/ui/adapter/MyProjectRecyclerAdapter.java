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

import java.util.List;

public class MyProjectRecyclerAdapter extends RecyclerView.Adapter<MyProjectRecyclerAdapter.MyProjectViewHolder> {
    private Context mContext;

    private OnProjectClickListener onProjectClickListener;
    private List<Project> projectList;
    public MyProjectRecyclerAdapter(Context context, OnProjectClickListener onProjectClickListener) {
        this.mContext = context;
        this.onProjectClickListener = onProjectClickListener;
    }

    public interface OnProjectClickListener{
        void onProjectClick(Project project);
    }

    @NonNull
    @Override
    public MyProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_project_recycler_layout, parent, false);
        return new MyProjectViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProjectViewHolder holder, int position) {

        Project project = projectList.get(position);
        String projectName = project.getNameOfProject();
        holder.tvProjectName.setText(projectName);
        holder.tvHeader.setText(String.valueOf(projectName.charAt(0)).toUpperCase());
        holder.tvProjectDesc.setText(project.getDescOfProject());
    }

    @Override
    public int getItemCount() {
        if(projectList == null){
            return 0;
        }else return projectList.size();
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProject(List<Project> projectList){
        this.projectList = projectList;
        notifyDataSetChanged();
    }

    public class MyProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView container_cardView;
        TextView tvProjectName, tvHeader;
        TextView tvProjectDesc;

        public MyProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            container_cardView = itemView.findViewById(R.id.container_cardView);
            tvProjectName = itemView.findViewById(R.id.tv_projectName);
            tvProjectDesc = itemView.findViewById(R.id.tv_projectDesc);
            tvHeader = itemView.findViewById(R.id.tv_contain);
            container_cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onProjectClickListener.onProjectClick(projectList.get(getAdapterPosition()));
        }
    }
}
