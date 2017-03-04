package com.example.quyet.podomoro.busEvent;

/**
 * Created by quyet on 3/4/2017.
 */

public class TimerSignal {
    private long time;

    public TimerSignal(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}
