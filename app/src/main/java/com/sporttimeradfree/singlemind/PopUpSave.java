package com.sporttimeradfree.singlemind;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.sporttimeradfree.singlemind.sporttimer.R;

/**
 * Created by JohnGalt on 30.12.2016.
 */
public class PopUpSave extends Dialog implements View.OnClickListener {

    public Sport sport;
    public Dialog d;
    public Button btnPopUpSave;
    public EditText etSave;

    public PopUpSave(Sport sport) {
        super(sport);
        this.sport = sport;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popupsave);

        btnPopUpSave = (Button) findViewById(R.id.btnPopUpSave);
        btnPopUpSave.setOnClickListener(this);

        etSave = (EditText) findViewById(R.id.etSave);
    }

    /**
     * on Button Click code checks if all fields have valid input and if true, saves those values to SQL database
     * @param v view from where it was called
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPopUpSave:

                boolean check = true;

                int nameIndex = sport.cursor.getColumnIndex(DBHelper.KEY_NAME);
                if (sport.cursor.moveToFirst()) {
                    do {
                        if (etSave.getText().toString().equals(sport.cursor.getString(nameIndex))) {
                            check = false;
                            break;
                        }
                    } while (sport.cursor.moveToNext());
                }
                if(etSave.getText().toString().equals(""))
                    check = false;
                if (check) {
                    if (etSave.getText().toString().equals("")) {
                        sport.contentValues.put(DBHelper.KEY_NAME, sport.getResources().getString(R.string.Unknown));
                    } else
                        sport.contentValues.put(DBHelper.KEY_NAME, etSave.getText().toString());

                    if (sport.etTimes.getText().toString().equals(""))
                        sport.contentValues.put(DBHelper.KEY_TIMES_IN_SET, 0);
                    else
                        sport.contentValues.put(DBHelper.KEY_TIMES_IN_SET, Integer.parseInt(sport.etTimes.getText().toString()));

                    if (sport.etWork.getText().toString().equals(""))
                        sport.contentValues.put(DBHelper.KEY_WORK_TIME, 0);
                    else
                        sport.contentValues.put(DBHelper.KEY_WORK_TIME, Integer.parseInt(sport.etWork.getText().toString()));

                    if (sport.etRest.getText().toString().equals(""))
                        sport.contentValues.put(DBHelper.KEY_REST_TIME, 0);
                    else
                        sport.contentValues.put(DBHelper.KEY_REST_TIME, Integer.parseInt(sport.etRest.getText().toString()));

                    if (sport.etDelay.getText().toString().equals(""))
                        sport.contentValues.put(DBHelper.KEY_DELAY, 0);
                    else
                        sport.contentValues.put(DBHelper.KEY_DELAY, Integer.parseInt(sport.etDelay.getText().toString()));

                    sport.database.insert(DBHelper.TABLE_WORKOUTS, null, sport.contentValues);
                    sport.populateSpinner();
                    sport.spSavedWorkOuts.setSelection(sport.spSavedWorkOuts.getAdapter().getCount() - 1);
                    dismiss();
                } else Toast.makeText(sport, sport.getResources().getString(R.string.error), Toast.LENGTH_LONG).show();



                break;
            default:
                break;
        }
    }
}
