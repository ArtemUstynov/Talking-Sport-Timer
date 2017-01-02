package com.sporttimer.singlemind;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.sporttimer.singlemind.sporttimer.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by JohnGalt on 30.12.2016.
 */
public class PopUpTimer extends Dialog implements View.OnClickListener {

    public Sport sport;
    Button btnPause, btnCancel;
    TextView tvTimer, tvState;
    int timesInSet, workTime, restTime, delay;
    CountDownTimerWithPause countDelay, countWork, countRest;

    int sumTimesInSet = 1;

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
        tvState = (TextView) findViewById(R.id.tvState);

    }

    public void load() {
        if (!sport.etTimes.getText().toString().equals(""))
            timesInSet = Integer.parseInt(sport.etTimes.getText().toString());
        else timesInSet = 0;

        if (!sport.etWork.getText().toString().equals(""))
            workTime = Integer.parseInt(sport.etWork.getText().toString());
        else workTime = 0;

        if (!sport.etRest.getText().toString().equals(""))
            restTime = Integer.parseInt(sport.etRest.getText().toString());
        else restTime = 0;

        if (!sport.etDelay.getText().toString().equals(""))
            delay = Integer.parseInt(sport.etDelay.getText().toString());
        else delay = 0;
    }

    public PopUpTimer(Sport context) {
        super(context);
        this.sport = context;
    }

    @Override
    public void onStart() {
        load();
        timerBegin();
        sumTimesInSet = 1;
    }

    @Override
    public void onStop() {
        if (countDelay != null)
            countDelay.cancel();
        if (countRest != null)
            countRest.cancel();
        if (countWork != null)
            countWork.cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPause:
                if (btnPause.getText().toString().equals(sport.getResources().getString(R.string.Pause))) {
                    if (countDelay != null && countDelay.isRunning())
                        countDelay.pause();
                    if (countRest != null && countRest.isRunning())
                        countRest.pause();
                    if (countWork != null && countWork.isRunning())
                        countWork.pause();
                    btnPause.setText(sport.getResources().getString(R.string.Resume));
                } else {
                    if (countDelay != null && countDelay.isPaused())
                        countDelay.resume();
                    if (countRest != null && countRest.isPaused())
                        countRest.resume();
                    if (countWork != null && countWork.isPaused())
                        countWork.resume();
                    btnPause.setText(sport.getResources().getString(R.string.Pause));
                }
                break;
            case R.id.btnCancel:
                dismiss();
                break;
        }
    }

    private void timerBegin() {
        Log.d("mog", "BEGIN");
        countDelay = new CountDownTimerWithPause(delay * 1000, 100, true) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvState.setText(sport.getResources().getString(R.string.prepare));
                tvTimer.setText(String.valueOf(millisUntilFinished / 1000));//+":"+(millisUntilFinished-millisUntilFinished/10)
            }

            @Override
            public void onFinish() {
                timerMaster();
                countDelay.cancel();
            }
        };
        countDelay.create();

    }

    private void timerWork() {
        countWork = new CountDownTimerWithPause(workTime * 1000, 100, true) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvState.setText(sport.getResources().getString(R.string.workout));
                tvTimer.setText(String.valueOf(millisUntilFinished / 1000));//+":"+(millisUntilFinished-millisUntilFinished/10)
            }

            @Override
            public void onFinish() {
                sumTimesInSet++;
                timerMaster();
                Log.d("mlog", "FINISH");
            }
        };
        countWork.create();

    }

    private void timerRest() {
        countRest = new CountDownTimerWithPause(restTime * 1000, 100, true) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvState.setText(sport.getResources().getString(R.string.resting));
                tvTimer.setText(String.valueOf(millisUntilFinished / 1000));//+":"+(millisUntilFinished-millisUntilFinished/10)
            }

            @Override
            public void onFinish() {
                sumTimesInSet++;
                timerMaster();
                Log.d("mlog", "FINISH");
            }
        };
        countRest.create();
    }

    private void timerMaster() {
        if (sumTimesInSet <= timesInSet * 2 - 1) {
            if (sumTimesInSet % 2 != 0) {
                timerWork();
            } else timerRest();
        }
    }

}
