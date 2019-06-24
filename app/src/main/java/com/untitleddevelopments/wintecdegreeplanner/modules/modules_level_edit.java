package com.untitleddevelopments.wintecdegreeplanner.modules;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.untitleddevelopments.wintecdegreeplanner.DB.DBHelper;
import com.untitleddevelopments.wintecdegreeplanner.DB.DBManager;
import com.untitleddevelopments.wintecdegreeplanner.global.Globals;
import com.untitleddevelopments.wintecdegreeplanner.DB.Module;

import com.untitleddevelopments.wintecdegreeplanner.R;
import com.untitleddevelopments.wintecdegreeplanner.ui.StuPlan.OptionMenuActivity;

import static android.support.constraint.Constraints.TAG;

public class modules_level_edit extends OptionMenuActivity implements View.OnClickListener{

    EditText nzqaLevel;
    int currentModuleId;
    ImageButton saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules_level_edit);

        currentModuleId = Globals.getModule_ID();
        Module currentModule = new Module(currentModuleId);

        nzqaLevel = findViewById(R.id.module_edit_level);
        saveBtn = findViewById(R.id.editLevelButton);

        nzqaLevel.setText(String.valueOf(currentModule.getNZQALevel()));

        saveBtn.setOnClickListener(this);
    }

    private boolean isModuleFormComplete(){

        if (nzqaLevel.getText().toString().length() == 0 ) {
            Toast.makeText(this, "Please fill the Level of the module!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private boolean saveModuleInDatabase() {

        if(!isModuleFormComplete()) {
            return false;
        }


        ContentValues contentModule = new ContentValues();
        contentModule.put(DBHelper.MODULE_NZQALEVEL, nzqaLevel.getText().toString());

        boolean updatedOK = false;
        DBManager.getInstance().openDatabase();
        updatedOK = DBManager.getInstance().update(
                DBHelper.TBL_MODULE,
                contentModule,
                DBHelper.MODULE_ID + "=" + currentModuleId,
                null);

        String myMsg = updatedOK ? " Update Success!" : " Not Deleted - bugger";
        Log.e(TAG,  myMsg);

        return true;
    }

    @Override
    public void onClick(View v) {
        boolean status = saveModuleInDatabase();

        if(status) {
            Toast.makeText(this, "Module Saved Successfully", Toast.LENGTH_LONG).show();
        }

        Intent i = new Intent(this, modules_main.class);
        startActivity(i);
    }
}
