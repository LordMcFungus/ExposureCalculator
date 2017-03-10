package com.example.informatik.exposurecalculator;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class TimerActivity extends AppCompatActivity {

    private int timerTime;
    private int timerTimeInMilliseconds;
    private ProgressBar progressBar;
    private CountDownTimer timer;
    private int progress;
    private Button timerStartButton;
    private Button timerResetButton;
    private TextView mTextField;
    private Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        //Extramessage mit der Timer-Zeit auslesen
        Intent intent = getIntent();
        timerTime =  intent.getIntExtra(MainActivity.TIMER_MESSAGE, 0);


        //Progressbar für den timer initialisieren
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setMax(1000);
        progressBar.setProgress(1000);

        //Timeranzeige auf Zeit setzen
        mTextField = (TextView) findViewById(R.id.timer_text);
        mTextField.setText(new SecondsToStringConverter().convertSecondsToString(timerTime));

        //Farben der Buttons ändern
        timerStartButton = (Button) findViewById(R.id.reset_timer_button);
        timerStartButton.getBackground().setColorFilter(0xFF85A0C3, PorterDuff.Mode.MULTIPLY);
        timerResetButton = (Button) findViewById(R.id.start_timer_button);
        timerResetButton.getBackground().setColorFilter(0xFF85A0C3, PorterDuff.Mode.MULTIPLY);
    }

    public void onStartTimer(View view) {
        //Restbutton aktivieren und start timer button deaktivieren, damit kein zweiter timer gliechzeitig gestartet werden kann
        timerResetButton = (Button) findViewById(R.id.reset_timer_button);
        timerResetButton.setEnabled(true);
        timerStartButton = (Button) findViewById(R.id.start_timer_button);
        timerStartButton.setEnabled(false);
        timerTimeInMilliseconds = timerTime * 1000;

        //Countdowntimer Starten
        timer = new CountDownTimer(timerTimeInMilliseconds, 50) {

            // Bei jeden tick den text aktualisieren und den Progressbar um anzahl promille zurüksetzten wie zeit gelaufen ist.
            public void onTick(long millisUntilFinished) {
                int time = Math.round(millisUntilFinished / 1000);
                mTextField.setText(new SecondsToStringConverter().convertSecondsToString(time));
                float tmp = millisUntilFinished;
                float tmp2 = timerTimeInMilliseconds;
                progress = Math.round((tmp / tmp2 ) *1000);
                progressBar.setProgress(progress);
            }

            //Anzeigen dass timer beendet ist
            public void onFinish() {
                mTextField.setText("Time's up!");
                progressBar.setProgress(0);

                //Alarmton abspielen
                try {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    ringtone.play();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Could not play Ringtone", Toast.LENGTH_SHORT).show();
                }



            }
        }.start();
    }

    public void onResetTimer(View view) {
        // Timer beenden
        timer.cancel();
        //Resetbutton deaktivieren Startbutton aktivieren
        timerResetButton.setEnabled(false);
        timerStartButton.setEnabled(true);

        //Text auf ursprünglichje ziet zurücksetzen, progressbar zurücksetzten und falls alarm aktiv ist, sound beenden
        mTextField = (TextView) findViewById(R.id.timer_text);
        mTextField.setText(new SecondsToStringConverter().convertSecondsToString(timerTime));
        progressBar.setProgress(1000);
        if (ringtone != null) {ringtone.stop();}
    }

    // Wird der Android backbutton gedrückt wird der Timer und allenfalls der Alarmsound beendet.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(timer != null) {timer.cancel();}
        if (ringtone != null) {ringtone.stop();}
    }
}
