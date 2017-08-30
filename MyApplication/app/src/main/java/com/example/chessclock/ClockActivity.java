package com.example.chessclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ClockActivity extends AppCompatActivity {

    private Button firstPlayerButton;
    private Button secondPlayerButton;

    private ImageView resetButton;
    private ImageView menuButton;

    private StopWatch firstPlayerWatch;
    private StopWatch secondPlayerWatch;

    // The thread that updating the UI
    private UpdateButtonsThread updateThread;

    private long EXTRA_TIME_PER_MOVE; // extra time per move in seconds
    private long TIME_PER_PLAYER; //  time per player in milliseconds
    private static boolean isFirstTime; // this value is used in order to not add extra time the first time.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        isFirstTime = true;

        firstPlayerButton = (Button)findViewById(R.id.firstPlayerButton);
        secondPlayerButton = (Button)findViewById(R.id.secondPlayerButton);

        resetButton = (ImageView)findViewById(R.id.resetButton);
        menuButton = (ImageView)findViewById(R.id.menuButton);
        Intent intent = getIntent();

        TIME_PER_PLAYER = intent.getLongExtra("TIME_PER_PLAYER", 300000);

        // Setting up the clocks!
        firstPlayerWatch = new StopWatch(TIME_PER_PLAYER);
        secondPlayerWatch = new StopWatch(TIME_PER_PLAYER);

        EXTRA_TIME_PER_MOVE = intent.getLongExtra("EXTRA_TIME_PER_MOVE",0);

        updateThread = new UpdateButtonsThread();
        updateThread.start();

        firstPlayerButton.setOnClickListener(new ClickListener(firstPlayerWatch, secondPlayerWatch, secondPlayerButton));
        secondPlayerButton.setOnClickListener(new ClickListener(secondPlayerWatch, firstPlayerWatch, firstPlayerButton));

        resetButton.setOnClickListener(new ResetClickListener());

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private class ResetClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // stopping the watches
            firstPlayerWatch.stop();
            secondPlayerWatch.stop();

            // Destroy and re-creating the thread that updates the ui
            updateThread.interrupt();
            updateThread = new UpdateButtonsThread();
            updateThread.start();

            // making both buttons clickable again
            firstPlayerButton.setClickable(true);
            secondPlayerButton.setClickable(true);

            // enabling both buttons
            firstPlayerButton.setEnabled(true);
            secondPlayerButton.setEnabled(true);

            // resetting the flag
            isFirstTime = true;

            // resetting the timers on watches
            firstPlayerWatch.setTimeLeft(TIME_PER_PLAYER);
            secondPlayerWatch.setTimeLeft(TIME_PER_PLAYER);
        }
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
                if(!isFirstTime) {
                    myStopWatch.addSeconds(EXTRA_TIME_PER_MOVE);
                }
                else {
                    isFirstTime = false;
                }
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
        private Runnable updateRunnable =  new Runnable() {
            @Override
            public void run() {
                firstPlayerButton.setText(String.valueOf(player1Seconds / 60) + " : " + String.format("%02d",player1Seconds % 60));
                secondPlayerButton.setText(String.valueOf(player2Seconds / 60) + " : " + String.format("%02d",player2Seconds % 60));
            }
        };

        private void updateUI() {
            player1Seconds = firstPlayerWatch.getSecondsLeft();
            player2Seconds = secondPlayerWatch.getSecondsLeft();
            runOnUiThread(updateRunnable);
        }

        @Override
        public void run() {
            while(!interrupted() && (!firstPlayerWatch.hasEnded() && !secondPlayerWatch.hasEnded())) {
                updateUI();
                try {
                    Thread.sleep(250);
                }
                catch(InterruptedException e) {
                    return;
                }
            }
            updateUI();
            firstPlayerButton.setClickable(false);
            secondPlayerButton.setClickable(false);
        }
    }
}
