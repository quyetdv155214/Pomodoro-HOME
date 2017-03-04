package com.example.quyet.podomoro;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.example.quyet.podomoro.databases.DBContext;
import com.example.quyet.podomoro.databases.models.Task;
import com.example.quyet.podomoro.service.PomodoroService;

/**
 * Created by quyet on 1/14/2017.
 */

public class PomodoroApplication extends Application {
    private static final String TAG = PomodoroApplication.class.toString();

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, PomodoroService.class);
        startService(intent);

        Log.d(TAG, "onCreate: a");
    }
}
