package com.example.quyet.podomoro.busEvent;

/**
 * Created by quyet on 3/4/2017.
 */

public class TimerCommandEvent {
    private TimerCommand timerCommand;

    public TimerCommandEvent(TimerCommand timerCommand) {
        this.timerCommand = timerCommand;
    }

    public TimerCommand getTimerCommand() {
        return timerCommand;
    }
}
