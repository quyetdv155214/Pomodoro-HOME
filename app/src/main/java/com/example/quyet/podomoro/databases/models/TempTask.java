package com.example.quyet.podomoro.databases.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by quyet on 2/28/2017.
 */

public class TempTask extends RealmObject{
    @PrimaryKey
    @SerializedName("local_id")
    private String local_id;

    private boolean edited;
    private boolean deleted;
    private boolean added;
    public TempTask(String local_id, boolean edited, boolean deleted, boolean added) {
        this.local_id = local_id;
        this.edited = edited;
        this.added = added;
        this.deleted = deleted;
    }
    public TempTask(String local_id) {
        this(local_id, false, false, false);
    }

    public TempTask(String local_id, boolean added) {
        this(local_id,false, false, added);
    }

    public TempTask() {
    }

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
