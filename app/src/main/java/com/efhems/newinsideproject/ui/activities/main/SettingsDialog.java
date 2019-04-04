package com.efhems.newinsideproject.ui.activities.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.efhems.newinsideproject.R;

public class SettingsDialog extends BottomSheetDialogFragment implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "SettingsDialog";
    SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private RadioButton radioButton;
    public static final String IS_CHECKED = "isChecked";
    public static final String RADIO_BUTTON_ID = "prefId";
    public static final String RB_VALUE = "rb_value";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_dialog, container, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        initViews(view);
        showPreference(view);
        return view;
    }

    private void initViews(View view) {
        RadioGroup group = view.findViewById(R.id.radio_group);
        group.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        radioButton = group.findViewById(checkedId);
        switch (checkedId) {
            case R.id.rb_five_minute:
                if (radioButton.isChecked()) {
                    editor = preferences.edit();
                    editor.putBoolean(IS_CHECKED, true);
                    editor.putInt(RADIO_BUTTON_ID, checkedId);
                    editor.putInt(RB_VALUE, 300000);
                    editor.apply();
                } else {
                    editor.putBoolean(IS_CHECKED, false);
                    editor.putInt(RADIO_BUTTON_ID, 0);
                    editor.putInt(RB_VALUE, 0);
                    editor.apply();
                }
                break;

            case R.id.rb_ten_minute:
                if (radioButton.isChecked()) {
                    editor = preferences.edit();
                    editor.putBoolean(IS_CHECKED, true);
                    editor.putInt(RADIO_BUTTON_ID, checkedId);
                    editor.putInt(RB_VALUE, 600000);
                    editor.apply();
                } else {
                    editor.putBoolean(IS_CHECKED, false);
                    editor.putInt(RADIO_BUTTON_ID, 0);
                    editor.putInt(RB_VALUE, 0 );
                    editor.apply();
                }
                break;

            case R.id.rb_fifteen_minute:
                if (radioButton.isChecked()) {
                    editor = preferences.edit();
                    editor.putBoolean(IS_CHECKED, true);
                    editor.putInt(RADIO_BUTTON_ID, checkedId);
                    editor.putInt(RB_VALUE,900000 );
                    editor.apply();
                } else {
                    editor.putBoolean(IS_CHECKED, false);
                    editor.putInt(RADIO_BUTTON_ID, 0);
                    editor.putInt(RB_VALUE,0);
                    editor.apply();
                }
                break;

            case R.id.rb_twenty_minute:
                if (radioButton.isChecked()) {
                    editor = preferences.edit();
                    editor.putBoolean(IS_CHECKED, true);
                    editor.putInt(RADIO_BUTTON_ID, checkedId);
                    editor.putInt(RB_VALUE,1200000);
                    editor.apply();
                } else {
                    editor.putBoolean(IS_CHECKED, false);
                    editor.putInt(RADIO_BUTTON_ID, 0);
                    editor.putInt(RB_VALUE, 0);
                    editor.apply();
                }
                break;

            default:
        }
    }


    private void showPreference(View view) {
        if (preferences.contains(IS_CHECKED) && preferences.contains(RADIO_BUTTON_ID)) {
            radioButton = view.findViewById(preferences.getInt(RADIO_BUTTON_ID, 0));
            radioButton.setChecked(preferences.getBoolean(IS_CHECKED, false));

        } else {
            Log.d(TAG, "showPreference: " + preferences.getBoolean(IS_CHECKED, false));
        }
    }
}

