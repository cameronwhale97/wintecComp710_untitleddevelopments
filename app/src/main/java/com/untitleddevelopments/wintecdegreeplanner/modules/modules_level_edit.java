package com.untitleddevelopments.wintecdegreeplanner.modules;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.untitleddevelopments.wintecdegreeplanner.R;

public class modules_level_edit extends AppCompatActivity {

    EditText nzqaLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules_level_edit);

        nzqaLevel = (EditText) findViewById(R.id.module_edit_level);
    }
}