package com.example.chessclock;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ClockActivity extends FragmentActivity {

    private Button firstPlayerButton;
    private Button secondPlayerButton;
    private ImageView resetButton;
    private ImageView menuButton;
    private ImageView pauseButton;

    private StopWatch firstPlayerWatch;
    private StopWatch secondPlayerWatch;

    private boolean paused;

    // The thread that updating the UI
    private UpdateButtonsThread updateThread;

    private long EXTRA_TIME_PER_MOVE; // extra time per move in seconds
    private long TIME_PER_PLAYER; //  time per player in milliseconds
    private static boolean isFirstTime; // this value is used in order to not add extra time the first time.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        firstPlayerButton = (Button)findViewById(R.id.firstPlayerButton);
        secondPlayerButton = (Button)findViewById(R.id.secondPlayerButton);
        pauseButton = (ImageView)findViewById(R.id.pauseButton);
        pauseButton.setVisibility(View.INVISIBLE);

        resetButton = (ImageView)findViewById(R.id.resetButton);
        menuButton = (ImageView)findViewById(R.id.menuButton);
        Intent intent = getIntent();

        // Getting times from Intent
        TIME_PER_PLAYER = intent.getLongExtra("TIME_PER_PLAYER", 300000);
        EXTRA_TIME_PER_MOVE = intent.getLongExtra("EXTRA_TIME_PER_MOVE", 0);


        // Setting up the clocks!
        firstPlayerWatch = new StopWatch(TIME_PER_PLAYER);
        secondPlayerWatch = new StopWatch(TIME_PER_PLAYER);

        // Adds listeners on both player's buttons
        firstPlayerButton.setOnClickListener(new ClickListener(firstPlayerWatch, secondPlayerWatch, secondPlayerButton));
        secondPlayerButton.setOnClickListener(new ClickListener(secondPlayerWatch, firstPlayerWatch, firstPlayerButton));

        // Adds listeners on the rest of the buttons
        resetButton.setOnClickListener(new ResetClickListener());
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(updateThread != null) {
                    updateThread.interrupt();
                    updateThread = null;
                }
                finish();
            }
        });

        pauseButton.setOnClickListener(new PauseClickListener());
    }


    /**
     * Overriding the unPause because we want to stop the stopWatches when the activity is not on the foreground
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Kill the thread which updates the UI
        if(updateThread != null) {
            updateThread.interrupt();
            updateThread = null;
        }

        // if the pause button was pressed, un-pause the game
        if(paused) {
            pauseButton.callOnClick();
        }

        // make the pause button invisible, because the app will begin again on the "Start" state
        pauseButton.setVisibility(View.INVISIBLE);

        // stop the watches
        firstPlayerWatch.stop();
        secondPlayerWatch.stop();
    }


    /**
     * Overriding the onResume because we want to set the firstTime flag and also create the thread which updates the ui
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Creating and starting the thread which updates the UI (the buttons)
        updateThread = new UpdateButtonsThread();
        updateThread.start();

        /**
         * After the activity goes onPause or onStop state and finally comes again onResume, I want the users to be able to choose from which clock to start again.
         * If the onResume is called from onPause or onStop then, probably 1 button was disabled.
         * Enable both buttons, we set them independently if they were enabled before, cuz we don't actually care.
         */
        firstPlayerButton.setEnabled(true);
        secondPlayerButton.setEnabled(true);

        // reset/set the flag
        isFirstTime = true;
    }

    /**
     * This listener will be added on the pauseImageView. This ImageView will pause the game for both players.
     * After un-pause the watch of the player who had previously the turn will start the count down again automatically.
     */
    private class PauseClickListener implements View.OnClickListener {
        private boolean firstPlayerButtonStatus;
        @Override
        public void onClick(View view) {
            // Toggle the flag
            paused = !paused;

            /**
             * This cast is safe, because we add the PauseClickListener only on 1 ImageView. So we can safely cast it to ImageView.
             * The cast is used in order to be able to call the setImageResource method to change the image of the ImageView.
             */
            ImageView img = (ImageView)view;

            // If now is paused (after toggle)
            if(paused) {
                // Change the image from "pause" to "play"
                img.setImageResource(R.drawable.ic_play_arrow_48dp);

                //Check if it was the firstPlayer turn
                firstPlayerButtonStatus = firstPlayerButton.isEnabled();

                if(firstPlayerButtonStatus) { // if it was the turn of the first player
                    firstPlayerButton.setClickable(false); // make his button unClickable (keep in mind, other player button is disabled).
                    firstPlayerWatch.stop(); // stop the first player's watch.
                }
                else { // if not first player's button is enabled is on the second.
                    secondPlayerButton.setClickable(false); // read above, same logic
                    secondPlayerWatch.stop(); // stop second player's watch.
                }
            }
            else { // If now is un-paused (after toggle)
                // Change the image from "play" to "pause"
                img.setImageResource(R.drawable.ic_pause_48dp);

                // if before was the turn of the firstPlayer
                if(firstPlayerButtonStatus) {
                    firstPlayerWatch.start(); // start the timer
                    firstPlayerButton.setClickable(true); // make the button clickable again
                }
                else {
                    // see above for comments, same logic
                    secondPlayerWatch.start();
                    secondPlayerButton.setClickable(true);
                }

            }
        }
    }


    /**
     * This listener is added on the resetImageView. This ImageView resets both player watches to have the initial time.
     */
    private class ResetClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            // if it was paused, un-paused it
            if(paused) {
                pauseButton.callOnClick();
            }

            // hide the pause button
            pauseButton.setVisibility(View.INVISIBLE);

            // stopping the watches
            firstPlayerWatch.stop();
            secondPlayerWatch.stop();

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

    /**
     * This listener is added on both player's buttons.
     * It pauses the current player watch, makes his button disabled and add the extra seconds per Move.
     * After that it enable the enemy player's button and start his watch.
     */
    private class ClickListener implements View.OnClickListener {
        private Button buttonToEnable;      // Enemy player's button, to enable
        private StopWatch myStopWatch;      // Current player watch
        private StopWatch enemyStopWatch;   // enemy player's stopWatch

        public ClickListener(StopWatch myStopWatch, StopWatch enemyStopWatch, Button buttonToEnable) {
            this.myStopWatch = myStopWatch;
            this.buttonToEnable = buttonToEnable;
            this.enemyStopWatch = enemyStopWatch;
        }

        @Override
        public void onClick(View view) {
            if(view.isEnabled()) {
                if(isFirstTime) { // Do NOT give extra time on the firstMove, why? (That's how I want it :P).
                    isFirstTime = false; // turn off the flag
                    pauseButton.setVisibility(View.VISIBLE); // if the firstMove is played we can enable the pauseButton, and let users pause their game.
                }
                else {
                    myStopWatch.addSeconds(EXTRA_TIME_PER_MOVE); // if it wasn't the first move, then give on the currentPlayer the extra seconds.
                }
                // stop current stopWatch
                myStopWatch.stop();

                // disable the view, this has a result the button to not be clickable and also change the colour of the button.
                view.setEnabled(false);

                // enable enemy's button, and watch
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
