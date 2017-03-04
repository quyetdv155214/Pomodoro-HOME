package com.example.quyet.podomoro.busEvent;

/**
 * Created by quyet on 3/3/2017.
 */

public class FailureNetworkEvent {
    private boolean isFailed;

    public FailureNetworkEvent(boolean isFailed) {
        this.isFailed = isFailed;
    }

    public boolean isFailed() {
        return isFailed;
    }

    public void setFailed(boolean failed) {
        isFailed = failed;
    }
}
