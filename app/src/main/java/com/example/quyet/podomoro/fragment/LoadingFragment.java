package com.example.quyet.podomoro.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quyet.podomoro.R;
import com.example.quyet.podomoro.busEvent.FailureNetworkEvent;
import com.example.quyet.podomoro.busEvent.GetDataSuccessEvent;
import com.example.quyet.podomoro.databases.DBContext;
import com.example.quyet.podomoro.databases.TaskManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoadingFragment extends Fragment {


    private static final String TAG = "Loading fragment";
    private EventBus bus = EventBus.getDefault();

    public LoadingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        taskFragmentListener = (TaskFragmentListener) context;
    }

    TaskFragmentListener taskFragmentListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loading, container, false);
        loadData();
        bus.register(this);
        return view;
    }

    @Override
    public void onStop() {
        bus.unregister(this);
        super.onStop();
    }

    @Subscribe
    public void OnGetData(GetDataSuccessEvent gde) {
        boolean ok = gde.isDone();
        if (ok) {
            Log.d(TAG, "OnGetData: true");
            TaskFragment taskFragment = new TaskFragment();
            taskFragmentListener.onChangeFragment(taskFragment, false);
        }else{
            Log.d(TAG, "OnGetData: false");
            Toast.makeText(this.getContext(), "cannot parse", Toast.LENGTH_SHORT).show();
        }
    }
    @Subscribe
    public void OnFailureNetwork(FailureNetworkEvent fne){
        if(fne.isFailed()) {
            DBContext.instance.getTasks();
            TaskFragment taskFragment = new TaskFragment();
            taskFragmentListener.onChangeFragment(taskFragment, false);

        }
    }

    public void loadData() {
        TaskManager.instance.getTaskFromServer();


    }
}
