package com.sporttimer.singlemind;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.sporttimer.singlemind.sporttimer.R;

/**
 * Created by JohnGalt on 30.12.2016.
 */
public class PopUpTimer extends Dialog implements View.OnClickListener {

    public Sport sport;
    Button btnPause, btnCancel;
    TextView tvTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popuptimer);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnPause = (Button) findViewById(R.id.btnPause);
        btnPause.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        tvTimer = (TextView) findViewById(R.id.tvTimer);
    }

    public PopUpTimer(Sport context) {
        super(context);
        this.sport = context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPause:
                dismiss();
                break;
            case R.id.btnCancel:
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }
}
