package com.example.quyet.podomoro.databases;

import android.content.Context;

import com.example.quyet.podomoro.databases.models.Task;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


/**
 * Created by quyet on 2/28/2017.
 */

public class DBContext2 {
    private Realm realm;

    public DBContext2(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }
    public void addTask(Task task) {
        realm.beginTransaction();
        realm.copyToRealm(task);
        realm.commitTransaction();
    }

    public List<Task> allTasks() {
        RealmResults<Task> listTask = realm.where(Task.class).findAll();
        return listTask;
    }
    public void addOrUpdate(Task task) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(task);
        realm.commitTransaction();
    }

    public void deleteTask(Task task) {
        realm.beginTransaction();
        RealmResults<Task> delTask = realm.where(Task.class).equalTo("local_id", task.getLocal_id()).findAll();
        if (delTask != null)
            for (Task t :
                    delTask) {
                t.deleteFromRealm();
            }
        realm.commitTransaction();
    }
}
