package com.example.quyet.podomoro.busEvent;

/**
 * Created by quyet on 3/7/2017.
 */

public class TimerSignalDone {
    boolean isDone;

    public boolean isDone() {
        return isDone;
    }

    public TimerSignalDone(boolean isDone) {

        this.isDone = isDone;
    }
}
