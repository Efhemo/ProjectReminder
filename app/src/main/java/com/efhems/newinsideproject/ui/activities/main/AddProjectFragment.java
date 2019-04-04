package com.efhems.newinsideproject.ui.activities.main;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.efhems.newinsideproject.AppExecutors;
import com.efhems.newinsideproject.R;
import com.efhems.newinsideproject.data.ProjectReminderDatabase;
import com.efhems.newinsideproject.data.local.entities.Project;
import com.efhems.newinsideproject.ui.activities.addTask.AddTaskActivity;

import static com.efhems.newinsideproject.ui.activities.projectList.MyProjectActivity.CURRENT_PROJECT;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddProjectFragment extends DialogFragment {


    private EditText editTextProjectName;
    private EditText editTextProjectDesc;
    private Button btnOk;

    private static OnClickOkButtonListener onClickOkButtonListener;

    interface OnClickOkButtonListener{
        void onClickOkButton(EditText editTextProjectName, EditText editTextProjectDesc);
    }

    public AddProjectFragment() {
        // Required empty public constructor
    }

    public static AddProjectFragment newInstance(OnClickOkButtonListener onClickOkButtonListener) {
        AddProjectFragment frag = new AddProjectFragment();
        AddProjectFragment.onClickOkButtonListener = onClickOkButtonListener;
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layoutView = inflater.inflate(R.layout.fragment_add_project,container,false);
        editTextProjectName = layoutView.findViewById(R.id.edt_projectName);
        editTextProjectDesc = layoutView.findViewById(R.id.edt_projectDescription);
        btnOk = layoutView.findViewById(R.id.btn_ok);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickOkButtonListener.onClickOkButton(editTextProjectName, editTextProjectDesc);
                //makeProject();
            }
        });

        return layoutView;
    }


    @Override
    public void onResume() {
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.37);

        getDialog().getWindow().setLayout(width, height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        super.onResume();
    }

}
