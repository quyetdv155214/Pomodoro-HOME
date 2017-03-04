package com.example.quyet.podomoro.busEvent;

/**
 * Created by quyet on 3/3/2017.
 */

public class GetDataSuccessEvent {
    private boolean isDone;

    public GetDataSuccessEvent(boolean isDone) {
        this.isDone = isDone;
    }

    public boolean isDone() {
        return isDone;
    }

}
