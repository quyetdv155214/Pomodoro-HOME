package com.example.quyet.podomoro.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.quyet.podomoro.busEvent.TimerCommand;
import com.example.quyet.podomoro.busEvent.TimerCommandEvent;
import com.example.quyet.podomoro.busEvent.TimerSignal;
import com.example.quyet.podomoro.busEvent.TimerSignalDone;
import com.example.quyet.podomoro.ultil.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.sql.Time;

/**
 * Created by quyet on 3/4/2017.
 */

public class PomodoroService extends Service {
    private static final String TAG = "Service";
    private CountDownTimer countDownTimer;
    private int timeUntilFinished = Constant.TIME_POMODORO ;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);

    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeUntilFinished, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeUntilFinished = (int) millisUntilFinished;
                EventBus.getDefault().postSticky(new TimerSignal((int) (millisUntilFinished - 1000)));
            }
            @Override
            public void onFinish() {
                EventBus.getDefault().postSticky(new TimerSignalDone(true));
            }
        };
        countDownTimer.start();
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        EventBus.getDefault().postSticky(new TimerSignal(timeUntilFinished-1000));
    }

    private void stopTimer() {
        timeUntilFinished = Constant.TIME_POMODORO ;
        pauseTimer();
        countDownTimer.onFinish();
    }
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onCommand(TimerCommandEvent timerCommandEvent) {

        TimerCommand tc = timerCommandEvent.getTimerCommand();
        switch (tc) {
            case START_TIMER:
                startTimer();
                break;
            case PAUSE_TIMER:
                pauseTimer();
                break;
            case STOP_TIMER:
                stopTimer();
                break;

        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
