package com.example.chessclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.view.View;
import android.widget.Button;

public class ClockActivity extends AppCompatActivity {

    private Button firstPlayerButton;
    private Button secondPlayerButton;
    private StopWatch firstPlayerWatch;
    private StopWatch secondPlayerWatch;
    private UpdateButtonsThread updateThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        firstPlayerButton = (Button)findViewById(R.id.firstPlayerButton);
        secondPlayerButton = (Button)findViewById(R.id.secondPlayerButton);
        Intent intent = getIntent();

        firstPlayerWatch = new StopWatch(intent.getIntExtra("first",300000));
        secondPlayerWatch = new StopWatch(intent.getIntExtra("second",300000));

        updateThread = new UpdateButtonsThread();
        updateThread.start();

        firstPlayerButton.setOnClickListener(new ClickListener(firstPlayerWatch, secondPlayerWatch, secondPlayerButton));
        secondPlayerButton.setOnClickListener(new ClickListener(secondPlayerWatch, firstPlayerWatch, firstPlayerButton));
    }

    private class ClickListener implements View.OnClickListener {
        private Button buttonToEnable;
        private StopWatch myStopWatch;
        private StopWatch enemyStopWatch;

        public ClickListener(StopWatch myStopWatch, StopWatch enemyStopWatch, Button buttonToEnable) {
            this.myStopWatch = myStopWatch;
            this.buttonToEnable = buttonToEnable;
            this.enemyStopWatch = enemyStopWatch;
        }

        @Override
        public void onClick(View view) {
            if(view.isEnabled()) {
                myStopWatch.stop();
                view.setEnabled(false);
                buttonToEnable.setEnabled(true);
                enemyStopWatch.start();
            }
        }
    }
    private class UpdateButtonsThread extends Thread {

        private long player1Seconds;
        private long player2Seconds;
        private Runnable runnable =  new Runnable() {
            @Override
            public void run() {
                firstPlayerButton.setText(String.valueOf(player1Seconds / 60) + " : " + String.format("%02d",player1Seconds % 60));
                secondPlayerButton.setText(String.valueOf(player2Seconds / 60) + " : " + String.format("%02d",player2Seconds % 60));
            }
        };

        @Override
        public void run() {
            while(!interrupted()) {
                player1Seconds = firstPlayerWatch.getSecondsLeft();
                player2Seconds = secondPlayerWatch.getSecondsLeft();
                runOnUiThread(runnable);
                try {
                    Thread.sleep(250);
                }
                catch(InterruptedException e) {
                    return;
                }
            }
        }
    }
}
