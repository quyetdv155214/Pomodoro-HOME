package com.example.quyet.podomoro.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.quyet.podomoro.busEvent.TimerCommandEvent;
import com.example.quyet.podomoro.busEvent.TimerSignal;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by quyet on 3/4/2017.
 */

public class PomodoroService extends Service {
    private static final String TAG = "Service";
    private CountDownTimer countDownTimer;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);

    }
    private void startTimer(){
        countDownTimer  = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "Tick");
                EventBus.getDefault().postSticky(new TimerSignal(millisUntilFinished));
            }
            @Override
            public void onFinish() {
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    @Subscribe
    public void onCommand(TimerCommandEvent timerCommandEvent)
    {
        Log.d(TAG, "onCommand: hura");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
