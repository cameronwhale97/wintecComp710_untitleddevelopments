package com.untitleddevelopments.wintecdegreeplanner.admin;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.untitleddevelopments.wintecdegreeplanner.DB.DBHelper;
import com.untitleddevelopments.wintecdegreeplanner.DB.DBManager;
import com.untitleddevelopments.wintecdegreeplanner.DB.Stream;
import com.untitleddevelopments.wintecdegreeplanner.DB.Student;
import com.untitleddevelopments.wintecdegreeplanner.R;
import com.untitleddevelopments.wintecdegreeplanner.about_screen;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;


/**
 * This activity adds a new student in the database.
 */
public class AdminAddStudentActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{

    /**
     * Button to sve student in DB
     */
    private ImageButton btnAdd;

    /**
     * Button to show top menu
     */
    private ImageButton btnMenu;

    /**
     * form fields
     */
    private EditText etFName;
    private EditText etLName;
    private EditText etStudntID;
    private Spinner  spStreams;
    private EditText etStartDate;


    ArrayList<Stream> streams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_student);

        btnAdd = findViewById(R.id.btnAdd);
        btnMenu = findViewById(R.id.btnMenu);

        // setting event handlers for buttons
        btnAdd.setOnClickListener(this);
        btnMenu.setOnClickListener(this);

        // setting up student form UI references from XML
        etFName     = findViewById(R.id.etFName);
        etLName     = findViewById(R.id.etLName);
        etStudntID  = findViewById(R.id.etStudntID);
        etStartDate = findViewById(R.id.etStartDate);
        spStreams   = findViewById(R.id.spStreams);


        populateStreamsInDropdownList();
    }


    private void populateStreamsInDropdownList() {

        streams = Stream.getAllStreams();

        List<String> streamNames = getStreamNames();

        // Reference: stackoverflow.com
        // https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list



        // Create an adapter to describe how the items are displayed,
        // adapters are used in several places in android.
        // There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, streamNames);

        // Set the spinners adapter to the previously created one.
        spStreams.setAdapter(adapter);
    }

    private List<String> getStreamNames() {
        List<String> streamNames = new ArrayList<String>();


        for(int i=0; i<streams.size(); i++) {
            streamNames.add(streams.get(i).getName());
        }

        return streamNames;
    }

    private boolean isStudentFormComplete() {


        // ensure the first name is filled
        if (etFName.getText().toString().length() == 0 ) {
            Toast.makeText(this, "Please fill the first name of student!", Toast.LENGTH_LONG).show();
            return false;
        }

        // ensure the last name is filled
        if (etLName.getText().toString().length() == 0 ) {
            Toast.makeText(this, "Please fill the last name of student!", Toast.LENGTH_LONG).show();
            return false;
        }

        // ensure the student id is filled
        if (etStudntID.getText().toString().length() == 0 ) {
            Toast.makeText(this, "Please fill the student ID!", Toast.LENGTH_LONG).show();
            return false;
        }

        // ensure that start date is filled
        if (etStartDate.getText().toString().length() == 0 ) {
            Toast.makeText(this, "Please fill the start date!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private boolean saveStudentInDatabase() {



        // before saving student, ensure that the form is filled completely.
        if(!isStudentFormComplete()) {
            return false;
        }

        String streamName = spStreams.getSelectedItem().toString();

        int streamID = getStreamIdFromName(streamName);

        Toast.makeText(this, "streamID: " + streamID, Toast.LENGTH_LONG).show();

        /**
         *
         * saving student in database
         *
         */
         ContentValues contentStudent = new ContentValues();
         contentStudent.put(DBHelper.STUDENT_FIRSTNAME, etFName.getText().toString());
         contentStudent.put(DBHelper.STUDENT_SURNAME, etLName.getText().toString() );
         contentStudent.put(DBHelper.STUDENT_STUDENTID, etStudntID.getText().toString() );
         contentStudent.put(DBHelper.STUDENT_PHOTOURI, "");
         contentStudent.put(DBHelper.STUDENT_STARTDATE, etStartDate.getText().toString() );
         contentStudent.put(DBHelper.STUDENT_STATUS, 1);
         contentStudent.put(DBHelper.STUDENT_STREAM_ID, streamID);

         DBManager.getInstance().openDatabase();

         return DBManager.getInstance().insert(DBHelper.TBL_STUDENT, contentStudent);
    }


    /**
     * function gets stream ID from database for given stream name
     */
    private int getStreamIdFromName(String streamName) {


        String query = "SELECT * FROM " + DBHelper.TBL_STREAM +
                " WHERE " + DBHelper.STREAM_NAME + " = '" + streamName + "'";

        Log.d(TAG, "getStreamIdFromName: " + query);

        DBManager.getInstance().openDatabase();
        Cursor cursor = DBManager.getInstance().getDetails(query);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            // return stream id for given streamName
            return cursor.getInt(cursor.getColumnIndex(DBHelper.STREAM_ID));
        }


       // this should not happen!!!
       return 0;
    }

    /**
     * function creates the popup menu from xml and sets its event handler.
     * @param view    View/anchor on which the popup menu will be shown.
     */
    private void showTopMenu(View view) {

        Toast.makeText(this, "Top menu", Toast.LENGTH_LONG).show();

        // creating popup menu and setting up event handler
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);

        // loading menu from xml
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.admin_top_menu, popup.getMenu());
        popup.show();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            // save student in DB
            case R.id.btnAdd:
                saveStudentInDatabase();
                Student.getAllStudents();
                break;

            // show top menu
            case R.id.btnMenu:
                showTopMenu(view);
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miReturnToMain:
                Toast.makeText(this, "Return To Main", Toast.LENGTH_LONG).show();

                return true;

            case R.id.miAbout:
                startActivity(new Intent(this, about_screen.class));
                return true;

            default:
                return false;
        }
    }

}//AdminAddStudentActivity
