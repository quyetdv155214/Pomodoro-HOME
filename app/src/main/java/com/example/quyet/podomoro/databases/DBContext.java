package com.example.quyet.podomoro.databases;

import android.content.Context;
import android.util.Log;

import com.example.quyet.podomoro.databases.models.Color;
import com.example.quyet.podomoro.databases.models.Task;
import com.example.quyet.podomoro.databases.models.TempTask;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;

import static android.content.ContentValues.TAG;

/**
 * Created by quyet on 2/8/2017.
 */

public class DBContext {


    public static DBContext instance;
    private Realm realm;

    public DBContext(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    public DBContext() {
    }

    //    public void addTask(Task task) {
//        realm.beginTransaction();
//        realm.copyToRealm(task);
//        realm.commitTransaction();
//    }
    public List<TempTask> getTemps() {
        RealmResults<TempTask> temps = realm.where(TempTask.class).findAll();
        return temps;
    }

    public List<Task> getTasks() {
        RealmResults<Task> listTask = realm.where(Task.class).findAll();
//        for (Task t :
//                listTask) {
//            Log.d(TAG, String.format("getTasks: %s", t.toString()));
//        }

        return listTask;
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

    public void getTempTask() {

    }

    public void deleteTempTask(TempTask tempTask) {
        realm.beginTransaction();
        RealmResults<TempTask> delTemps = realm.where(TempTask.class).equalTo("local_id", tempTask.getLocal_id()).findAll();
        if (delTemps != null)
            for (TempTask t :
                    delTemps) {
                t.deleteFromRealm();
            }
        realm.commitTransaction();
    }

    //
    public <T> void addOrUpdate(T t) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate((RealmObject) t);
        realm.commitTransaction();
    }

    public <T> void add(T t) {
        realm.beginTransaction();
        realm.copyToRealm((RealmObject) t);
        realm.commitTransaction();
    }


    public List<Color> allColor() {
        List<Color> colors = new ArrayList<>();
        colors.add(new Color("#4A148C"));
        colors.add(new Color("#E040FB"));
        colors.add(new Color("#D500F9"));
        colors.add(new Color("#00897B"));
        colors.add(new Color("#1DE9B6"));
        colors.add(new Color("#D4E157"));
        colors.add(new Color("#76FF03"));
        colors.add(new Color("#69F0AE"));
        colors.add(new Color("#F9A825"));
        colors.add(new Color("#616161"));
        return colors;
    }

}
