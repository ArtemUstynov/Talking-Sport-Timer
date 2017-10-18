package com.sporttimeradfree.singlemind;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.sporttimeradfree.singlemind.DBHelper;
import com.sporttimeradfree.singlemind.PopUpSave;
import com.sporttimeradfree.singlemind.PopUpTimer;
import com.sporttimeradfree.singlemind.sporttimer.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class Sport extends AppCompatActivity implements View.OnClickListener {

    EditText  etTimes, etWork, etRest,  etDelay;
    Button btnSave, btnStart, btnDelete;
    PopUpSave popSave;
    PopUpTimer popTimer;
    Spinner spSavedWorkOuts;
    public DBHelper dbHelper;
    public SQLiteDatabase database;
    public ContentValues contentValues;
    public Cursor cursor;
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);


        etTimes = (EditText) findViewById(R.id.etTimes);
        etWork = (EditText) findViewById(R.id.etWork);
        etRest = (EditText) findViewById(R.id.etRest);
        etDelay = (EditText) findViewById(R.id.etDelay);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnSave.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        aSwitch = (Switch) findViewById(R.id.aSwitch);

        popSave = new PopUpSave(this);
        popTimer = new PopUpTimer(this);

        spSavedWorkOuts = (Spinner) findViewById(R.id.spSavedWorkauts);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        contentValues = new ContentValues();
        cursor = database.query(DBHelper.TABLE_WORKOUTS, null, null, null, null, null, null);

        populateSpinner();

        SharedPreferences sp = getSharedPreferences("timer_prefs", Activity.MODE_PRIVATE);
        aSwitch.setChecked(sp.getBoolean("switch_on", true));
/**
 * Pulls data from DB ant puts it in corresponding editText Views
 */
        spSavedWorkOuts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                cursor.moveToFirst();
                if (cursor.moveToFirst()) {
                    int idName = cursor.getColumnIndex(DBHelper.KEY_NAME);
                    int timesIndex = cursor.getColumnIndex(DBHelper.KEY_TIMES_IN_SET);
                    int workIndex = cursor.getColumnIndex(DBHelper.KEY_WORK_TIME);
                    int restIndex = cursor.getColumnIndex(DBHelper.KEY_REST_TIME);
                    int delayIndex = cursor.getColumnIndex(DBHelper.KEY_DELAY);
                    do {
                        if (cursor.getString(idName).equals(spSavedWorkOuts.getSelectedItem().toString())) {
                            etTimes.setText(String.valueOf(cursor.getString(timesIndex)));
                            etWork.setText(String.valueOf(cursor.getString(workIndex)));
                            etRest.setText(String.valueOf(cursor.getString(restIndex)));
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
        /**
         * Checks if TTS is installed
         */
        if(!isPackageInstalled("com.google.android.tts",getPackageManager())){
            new AlertDialog.Builder(this)
                    .setTitle(this.getResources().getString(R.string.info))
                    .setMessage(this.getResources().getString(R.string.missing))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.tts")));
                        }
                    })
                    .setNegativeButton(this.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .show();
        }
    }

    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

        @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                popSave.show();
                break;
            case R.id.btnStart:
                popTimer.show();
                popTimer.tvTimer.setText(etDelay.getText());
                break;
            case R.id.btnDelete:
                if (spSavedWorkOuts != null && spSavedWorkOuts.getSelectedItem() != null)
                    database.delete(DBHelper.TABLE_WORKOUTS, DBHelper.KEY_NAME + "= '" + spSavedWorkOuts.getSelectedItem().toString()+"'", null);

                populateSpinner();
                break;
        }
    }

    /**
     * Populates spinner
     */
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


    @Override
    public void onDestroy() {

        if(popTimer.tts!=null)
            popTimer.tts.shutdown();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sp = getSharedPreferences("timer_prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("switch_on", aSwitch.isChecked());
        editor.commit();


    }

}
