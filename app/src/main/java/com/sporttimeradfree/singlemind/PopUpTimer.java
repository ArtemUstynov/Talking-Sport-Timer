package com.sporttimeradfree.singlemind;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sporttimeradfree.singlemind.sporttimer.R;

import java.util.Locale;

/**
 * Created by JohnGalt on 30.12.2016.
 */
public class PopUpTimer extends Dialog implements View.OnClickListener, TextToSpeech.OnInitListener {

    public Sport sport;
    Button btnPause, btnCancel;
    TextView tvTimer, tvState;
    int timesInSet, workTime, restTime, delay;
    CountDownTimerWithPause countDelay, countWork, countRest;

    int sumTimesInSet = 1;

    TextToSpeech tts;
    boolean canTalk;

    String oldText = "";

    RelativeLayout theLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popuptimer);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnPause = (Button) findViewById(R.id.btnPause);
        btnPause.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        theLayout = (RelativeLayout) findViewById(R.id.theLayout);

        tvTimer = (TextView) findViewById(R.id.tvTimer);
        tvState = (TextView) findViewById(R.id.tvState);

        tts = new TextToSpeech(sport, this);

        tvTimer.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            /**
             * Watches for text change and triggers TTS to say it out laud
             * @param s
             * @param start
             * @param before
             * @param count
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (sport.aSwitch.isChecked()) {

                    if (tvState.getText().toString().equals(sport.getResources().getString(R.string.prepare)) && tvTimer.getText().toString().equals("0")
                            && !oldText.equals(tvTimer.getText().toString())) {
                        TextToSpeechFunction(sport.getResources().getString(R.string.Start));
                    }

                    if (tvState.getText().toString().equals(sport.getResources().getString(R.string.workout)) && tvTimer.getText().toString().equals("0")
                            && !oldText.equals(tvTimer.getText().toString())) {
                        TextToSpeechFunction(sport.getResources().getString(R.string.Stop));
                    }

                    if (tvState.getText().toString().equals(sport.getResources().getString(R.string.resting)) && tvTimer.getText().toString().equals("0")
                            && !oldText.equals(tvTimer.getText().toString())) {
                        TextToSpeechFunction(sport.getResources().getString(R.string.Start));
                    }

                    if (tvTimer.getText().toString().equals("1:00")
                            && !oldText.equals(tvTimer.getText().toString())) {
                        TextToSpeechFunction(sport.getResources().getString(R.string.OneMinute));
                    }

                    if (tvTimer.getText().toString().equals("2:00")
                            && !oldText.equals(tvTimer.getText().toString())) {
                        TextToSpeechFunction(sport.getResources().getString(R.string.TwoMinute));
                    }

                    if (tvTimer.getText().toString().equals("3:00")
                            && !oldText.equals(tvTimer.getText().toString())) {
                        TextToSpeechFunction(sport.getResources().getString(R.string.ThreeMinute));
                    }

                    if (tvTimer.getText().toString().equals("4:00")
                            && !oldText.equals(tvTimer.getText().toString())) {
                        TextToSpeechFunction(sport.getResources().getString(R.string.FourMinute));
                    }

                    if (tvTimer.getText().toString().equals("5:00")
                            && !oldText.equals(tvTimer.getText().toString())) {
                        TextToSpeechFunction(sport.getResources().getString(R.string.OneMinute));
                    }
                    if (tvTimer.getText().toString().equals("30")
                            && !oldText.equals(tvTimer.getText().toString())) {
                        TextToSpeechFunction(sport.getResources().getString(R.string.thirty));
                    }
                    if (tvTimer.getText().toString().equals("15")
                            && !oldText.equals(tvTimer.getText().toString())) {
                        TextToSpeechFunction(sport.getResources().getString(R.string.fifteen));
                    }

                    if (tvTimer.getText().toString().equals("10")
                            && !oldText.equals(tvTimer.getText().toString())) {
                        TextToSpeechFunction(sport.getResources().getString(R.string.ten));
                    }

                    if (tvTimer.getText().toString().equals("5")
                            && !oldText.equals(tvTimer.getText().toString())) {
                        TextToSpeechFunction(sport.getResources().getString(R.string.five));
                    }

                    if (tvTimer.getText().toString().equals("4")
                            && !oldText.equals(tvTimer.getText().toString())) {
                        TextToSpeechFunction(sport.getResources().getString(R.string.four));
                    }
                    if (tvTimer.getText().toString().equals("3")
                            && !oldText.equals(tvTimer.getText().toString())) {
                        TextToSpeechFunction(sport.getResources().getString(R.string.three));
                    }
                    if (tvTimer.getText().toString().equals("2")
                            && !oldText.equals(tvTimer.getText().toString())) {
                        TextToSpeechFunction(sport.getResources().getString(R.string.two));
                    }
                    if (tvTimer.getText().toString().equals("1")
                            && !oldText.equals(tvTimer.getText().toString())) {
                        TextToSpeechFunction(sport.getResources().getString(R.string.one));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        this.setCancelable(false);
    }

    /**
     * Loads data for countdown timer to work with
     */
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

    /**
     * cancels all Timers
     */
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
                if (countDelay != null)
                    countDelay.cancel();
                if (countRest != null)
                    countRest.cancel();
                if (countWork != null)
                    countWork.cancel();
                dismiss();
                break;
        }
    }

    /**
     * Responsible for layout changes, after timer was started
     */
    private void timerBegin() {
        countDelay = new CountDownTimerWithPause(delay * 1000, 100, true) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvState.setText(sport.getResources().getString(R.string.prepare));
                theLayout.setBackgroundColor( Color.argb(255,240,244,195));
                minutesHeadle(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                timerMaster();
                countDelay.cancel();
            }
        };
        countDelay.create();

    }
    /**
     * Responsible for layout changes, when timer is working
     */
    private void timerWork() {
        countWork = new CountDownTimerWithPause(workTime * 1000, 100, true) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvState.setText(sport.getResources().getString(R.string.workout));
                theLayout.setBackgroundColor( Color.argb(255,197,255,165));
                minutesHeadle(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                sumTimesInSet++;
                timerMaster();
                if (sumTimesInSet > timesInSet * 2 - 1) {
                    TextToSpeechFunction(sport.getResources().getString(R.string.Finish));
                }
            }
        };
        countWork.create();

    }
    /**
     * Responsible for layout changes, for rest cycle
     */
    private void timerRest() {
        countRest = new CountDownTimerWithPause(restTime * 1000, 100, true) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvState.setText(sport.getResources().getString(R.string.resting));
                theLayout.setBackgroundColor( Color.argb(255,244,154,154));
                minutesHeadle(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                sumTimesInSet++;
                timerMaster();
            }
        };
        countRest.create();
    }

    /**
     * Responsible for finishing the timer work
     */
    private void timerMaster() {
        if (sumTimesInSet <= timesInSet * 2 - 1) {
            if (sumTimesInSet % 2 != 0) {
                timerWork();
            } else timerRest();
        }
        if (sumTimesInSet > timesInSet) {
            tvState.setText(sport.getResources().getString(R.string.Finish));
            theLayout.setBackgroundColor( Color.argb(255,197,202,233));
        }
    }

    public void TextToSpeechFunction(int numberToPronaunce) {
        String string = String.valueOf(numberToPronaunce);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(string, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(string, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void TextToSpeechFunction(int numberToPronaunce, String say) {
        String string = String.valueOf(numberToPronaunce);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(string + say, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(string + say, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void TextToSpeechFunction(String say) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(say, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(say, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            if (Locale.getDefault().getLanguage().startsWith("ru")) {
                Locale locale = new Locale("RU");
                tts.setLanguage(locale);
            }
            else{ tts.setLanguage(Locale.US);}
        }
    }

    /**
     * handles  behavior of a timer for time > than 1 minute
     * @param millisUntilFinished
     */
    private void minutesHeadle(long millisUntilFinished) {
        if (millisUntilFinished / 60000 >= 1) {
            long midl = millisUntilFinished % 60000 / 1000;
            String midlSign = "";
            if (midl >= 10)
                midlSign = ":";
            else midlSign = ":0";
            tvTimer.setText(millisUntilFinished / 60000 + midlSign + millisUntilFinished % 60000 / 1000);
        } else
            tvTimer.setText(String.valueOf(millisUntilFinished / 1000));//+":"+(millisUntilFinished-millisUntilFinished/10)

        oldText = tvTimer.getText().toString();

        if (sumTimesInSet > timesInSet * 2 - 1) {
            tvState.setText(sport.getResources().getString(R.string.Finish));
        }
    }
}
