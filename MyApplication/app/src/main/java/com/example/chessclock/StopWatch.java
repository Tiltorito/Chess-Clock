package com.example.chessclock;


/**
 * Created by Μπάμπης Μπιλλίνης on 29/8/2017.
 */

public class StopWatch {

    // The remained time in the StopWatch in milliseconds.
    private long mTimeLeft;

    // The CountDownThread that will reduce the milliseconds.
    private CountDownThread thread;

    /**
     * Creates a StopWatch with specific remained time.
     * @param timeLeft the remained time in the stopwatch.
     */
    public StopWatch(long timeLeft) {
        mTimeLeft = timeLeft;
        thread = new CountDownThread();
    }

    /**
     * Starts the StopWatch
     */
    public void start() {
        if(thread.getState() == Thread.State.NEW || thread.getState() == Thread.State.TERMINATED) {
            thread.start();
        }
        else if(thread.isPaused()) {
            thread.resumeThread();
        }
    }

    /**
     * Stops the StopWatch
     */
    public void stop() {
        if(!(thread.getState() == Thread.State.NEW) && !thread.isPaused()) {
            thread.pauseThread();
        }
    }

    /**
     * Check whether the StopWatch is stopped
     * @return true if the StopWatch is stopped or has ended, false if it still countdown
     */
    public boolean isStopped() {
        return thread.isPaused() || hasEnded();
    }

    /**
     * Returns the remaining time in the StopWatch in milliseconds.
     * @return the remaining time in the StopWatch in milliseconds.
     */
    public long getTimeLeft() {
        return mTimeLeft;
    }

    /**
     * Returns the remaining time in the stopWatch in seconds.
     * @return Returns the remaining time in the stopWatch in seconds
     */
    public long getSecondsLeft() {
        return mTimeLeft / 1000;
    }

    /**
     * Returns the remaining time in the StopWatch in Minutes.
     * @return Returns the remaining time in the StopWatch in Minutes.
     */
    public long getMinutesLeft() {
        return getSecondsLeft() / 60;
    }

    /**
     * Checks whether the StopWatch has ended.
     * @return true if the remaining time is equal to 0 (zero), false otherwise.
     */
    public boolean hasEnded() {
        return mTimeLeft == 0;
    }

    /**
     * Adds milliseconds in the StopWatch.
     * @param milliseconds the number of milliseconds to be added.
     */
    public void addMilliseconds(long milliseconds) {
        mTimeLeft += milliseconds;
    }

    /**
     * Add seconds in the StopWatch
     * @param seconds the number of seconds to be added.
     */
    public void addSeconds(long seconds) {
        addMilliseconds(seconds * 1000);
    }

    /**
     * Add minutes in the StopWatch
     * @param minutes the number of minutes to be added.
     */
    public void addMinutes(int minutes) {
        addSeconds(minutes * 60);
    }

    private class CountDownThread extends Thread {
        private boolean paused;

        @Override
        public void run() {
            while(true) {
                if(!paused) {
                    mTimeLeft -= 1000;
                    if(mTimeLeft < 0) {
                        mTimeLeft = 0;
                        return; // terminate the thread if the stopwatch ended
                    }
                }
                try {
                    Thread.sleep(1000);
                }
                catch(Exception e) {

                }
            }
        }

        public void pauseThread() {
            paused = true;
        }

        public void resumeThread() {
            paused = false;
        }

        public boolean isPaused() {
            return paused;
        }
    }
}



