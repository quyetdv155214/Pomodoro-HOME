package com.example.quyet.podomoro.busmodel;

/**
 * Created by quyet on 3/3/2017.
 */

public class DataSetChangeEvent {
    private boolean isChanged;

    public DataSetChangeEvent(boolean isChanged) {
        this.isChanged = isChanged;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }
}
