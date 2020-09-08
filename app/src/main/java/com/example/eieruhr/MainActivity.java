package com.example.eieruhr;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private final int MIN = 1; // eine Minute
    private final int MAX = 10; // 10 Minuten

    private final String START = "Start";
    private final String STOPP = "Stopp";
    private int eggTime = MIN * 1000;
    private CountDownTimer timer;
    private MediaPlayer mediaPlayer;
    private boolean toggle = true; // Status des Button
    private TextView timerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(this,R.raw.horn);

        SeekBar seekBar = findViewById(R.id.seekBarTime);
        seekBar.setProgress(MIN);
        seekBar.setMax(MAX);

        timerView = findViewById(R.id.textTimeView);
        timerView.setText(formatTime(eggTime));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int sekunden, boolean fromUser) {

                if(sekunden < MIN){
                    seekBar.setProgress(MIN); // Eier unter einer Minute können Salmonellen enthalten!
                }else if(sekunden > MAX){
                    seekBar.setProgress(MAX); // 10 Minuten Eier sollten hart sein.
                }

                eggTime = sekunden * 60 * 1000;



                timerView.setText(formatTime(eggTime));
            }



            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @SuppressLint("DefaultLocale")
    private String formatTime(long millis) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }

    public void buttonPressed(View view){

        final Button button = findViewById(R.id.toggleButton);

        if(toggle) {
            button.setText(STOPP);
            timer = new CountDownTimer(eggTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.i("Topf", "Blubber blubber noch " + millisUntilFinished + " ms bis fertig.");

                    timerView.setText(formatTime(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    mediaPlayer.start();
                    button.setText(START);
                }
            }.start();

            toggle = false;
        }else{

            timer.cancel();

            // TODO: alles wieder zurücksetzen

            toggle = true;
            button.setText(START);
        }
    }

}