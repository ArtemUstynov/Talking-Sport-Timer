package com.sporttimer.singlemind;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.sporttimer.singlemind.sporttimer.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Sport extends AppCompatActivity implements View.OnClickListener {

    EditText etSets, etTimes, etWork, etRest, etBetween, etDelay;
    Button btnSave, btnStart, btnDelete;
    PopUpSave popSave;
    PopUpTimer popTimer;
    Spinner spSavedWorkOuts;
    public DBHelper dbHelper;
    public SQLiteDatabase database;
    public ContentValues contentValues;
    public Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);

        etSets = (EditText) findViewById(R.id.etSets);
        etTimes = (EditText) findViewById(R.id.etTimes);
        etWork = (EditText) findViewById(R.id.etWork);
        etRest = (EditText) findViewById(R.id.etRest);
        etBetween = (EditText) findViewById(R.id.etBetween);
        etDelay = (EditText) findViewById(R.id.etDelay);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnSave.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        popSave = new PopUpSave(this);
        popTimer = new PopUpTimer(this);

        spSavedWorkOuts = (Spinner) findViewById(R.id.spSavedWorkauts);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        contentValues = new ContentValues();
        cursor = database.query(DBHelper.TABLE_WORKOUTS, null, null, null, null, null, null);

        populateSpinner();
        spSavedWorkOuts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             //   Log.d("mlog", spSavedWorkOuts.getSelectedItem().toString());
                cursor.moveToFirst();
                if (cursor.moveToFirst()) {
                    int idName = cursor.getColumnIndex(DBHelper.KEY_NAME);
                    int setsIndex = cursor.getColumnIndex(DBHelper.KEY_SETS);
                    int timesIndex = cursor.getColumnIndex(DBHelper.KEY_TIMES_IN_SET);
                    int workIndex = cursor.getColumnIndex(DBHelper.KEY_WORK_TIME);
                    int restIndex = cursor.getColumnIndex(DBHelper.KEY_REST_TIME);
                    int betweenIndex = cursor.getColumnIndex(DBHelper.KEY_BETWEEN_SETS);
                    int delayIndex = cursor.getColumnIndex(DBHelper.KEY_DELAY);
                    do {
                        if (cursor.getString(idName).equals(spSavedWorkOuts.getSelectedItem().toString())) {
                            etSets.setText(String.valueOf(cursor.getString(setsIndex)));
                            etTimes.setText(String.valueOf(cursor.getString(timesIndex)));
                            etWork.setText(String.valueOf(cursor.getString(workIndex)));
                            etRest.setText(String.valueOf(cursor.getString(restIndex)));
                            etBetween.setText(String.valueOf(cursor.getString(betweenIndex)));
                            etDelay.setText(String.valueOf(cursor.getString(delayIndex)));
                        }
                    }
                    while (cursor.moveToNext());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                popSave.show();
                popSave.etSave.setText(etSets.getText());
                break;
            case R.id.btnStart:
                popTimer.show();
                popTimer.tvTimer.setText(etDelay.getText());
                Log.d("mlog", String.valueOf(spSavedWorkOuts.getSelectedItemPosition()));
                break;
            case R.id.btnDelete:
//                Log.d("mlog ", spSavedWorkOuts.getSelectedItem().toString());
                if (spSavedWorkOuts != null && spSavedWorkOuts.getSelectedItem() != null)
                    //if(!spSavedWorkOuts.getSelectedItem().toString().equals(""));
                    database.delete(DBHelper.TABLE_WORKOUTS, DBHelper.KEY_NAME + "= '" + spSavedWorkOuts.getSelectedItem().toString()+"'", null);

                populateSpinner();
                break;
        }
    }

    public void populateSpinner() {
        cursor = database.query(DBHelper.TABLE_WORKOUTS, null, null, null, null, null, null);

        ArrayList<String> workoutNames = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            do {
                workoutNames.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, workoutNames);
        spSavedWorkOuts.setAdapter(adapter);
    }
}
