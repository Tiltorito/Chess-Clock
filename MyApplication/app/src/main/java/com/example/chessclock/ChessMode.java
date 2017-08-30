package com.example.chessclock;

/**
 * Created by Μπάμπης Μπιλλίνης on 30/8/2017.
 */

public class ChessMode {
    private String title;
    private long minutes;
    private long seconds;
    private long extraTime;

    public ChessMode(String title, long minutes, long seconds, long extraTime) {
        this.title = title;
        this.minutes = minutes;
        this.seconds = seconds;
        this.extraTime = extraTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getMinutes() {
        return minutes;
    }

    public void setMinutes(long minutes) {
        this.minutes = minutes;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    public long getExtraTime() {
        return extraTime;
    }

    public void setExtraTime(long extraTime) {
        this.extraTime = extraTime;
    }

    public String getTimeToString() {
        StringBuilder str = new StringBuilder(16);
        str.append(minutes).append(":").append(String.format("%02d",seconds)).append(" | ").append(extraTime);
        return str.toString();
    }
}
