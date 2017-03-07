package com.example.quyet.podomoro.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.quyet.podomoro.R;
import com.example.quyet.podomoro.activities.TaskActivity;
import com.example.quyet.podomoro.busEvent.TimerCommand;
import com.example.quyet.podomoro.busEvent.TimerCommandEvent;
import com.example.quyet.podomoro.busEvent.TimerSignal;
import com.example.quyet.podomoro.busEvent.TimerSignalDone;
import com.example.quyet.podomoro.ultil.Constant;
import com.example.quyet.podomoro.ultil.Utils;
import com.github.lzyzsd.circleprogress.DonutProgress;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimerFragment extends Fragment {

    private String title;
    @BindView(R.id.bt_start)
    Button btStart;
    @BindView(R.id.bt_stop)
    Button btStop;
    @BindView(R.id.bt_pause)
    Button btPause;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.pg_timer)
    DonutProgress pgTimer;
    private float timerPercent= 0f;
    private int startTime =Constant.TIME_POMODORO-1000;

    public void setTitle(String title) {
        this.title = title;
    }

    public TimerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_timer, container, false);
        if (getActivity() instanceof TaskActivity) {
            ((TaskActivity) getActivity()).getSupportActionBar().setTitle(title);
        }
        ButterKnife.bind(this, view);
        setUI();
        addListener();
        EventBus.getDefault().register(this);
        return view;
    }
    private void setUI() {
        tvTime.setText(covertTime(new TimerSignal(startTime)));
        pgTimer.setText("");
        pgTimer.setProgress(timerPercent);

    }

    public void addListener() {
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimerCommandEvent timerCommandEvent = new TimerCommandEvent(TimerCommand.START_TIMER);
                btStart.setEnabled(false);
                btStop.setEnabled(true);
                btPause.setEnabled(true);
                EventBus.getDefault().post(timerCommandEvent);
            }
        });
        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btStart.setEnabled(true);
                btStop.setEnabled(false);
                btPause.setEnabled(false);
                EventBus.getDefault().post(new TimerCommandEvent(TimerCommand.STOP_TIMER));

            }
        });

        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btStart.setEnabled(true);
                btPause.setEnabled(false);
                EventBus.getDefault().post(new TimerCommandEvent(TimerCommand.PAUSE_TIMER));

            }
        });
    }
    @Subscribe(sticky = true)
    public void onDonePomodoro(TimerSignalDone timerSignalDone)
    {
        tvTime.setText(R.string.time_to_break);
    }

    @Subscribe(sticky = true)
    public void onSignalTimer(TimerSignal timerSignal) {
        timerPercent= covertTimeToPercent(timerSignal);
        pgTimer.setProgress(timerPercent);
        tvTime.setText(covertTime(timerSignal));
    }

    private float covertTimeToPercent(TimerSignal timerSignal) {
        float i = Constant.TIME_POMODORO - timerSignal.getTime();
        float per = (i/Constant.TIME_POMODORO) *100;
//        Log.d(TAG, "covertTimeToPercent: "+i);
        return per;
    }

    private String covertTime(TimerSignal timerSignal) {
        int time = timerSignal.getTime();
        long minutes = TimeUnit.MILLISECONDS
                .toMinutes(time);
        time -= TimeUnit.MINUTES.toMillis(minutes);
        time = time / 1000;
        return String.format("%02d : %02d", minutes, time);

    }

}
