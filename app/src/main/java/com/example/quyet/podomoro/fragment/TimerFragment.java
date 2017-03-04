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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    @BindView(R.id.tv_time)
    TextView tvTime;

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
        addListener();
        EventBus.getDefault().register(this);
        return view;
    }

    public void addListener() {
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimerCommandEvent timerCommandEvent = new TimerCommandEvent(TimerCommand.START_TIMER);
                EventBus.getDefault().post(timerCommandEvent);
            }
        });
    }

    @Subscribe(sticky = true)
    public void onSignalTimer(TimerSignal timerSignal) {
        Log.d(TAG, String.format("onSignalTimer: %s", timerSignal));
        tvTime.setText(timerSignal.toString());
    }

}
