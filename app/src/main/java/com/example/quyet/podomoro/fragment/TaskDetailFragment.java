package com.example.quyet.podomoro.fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.quyet.podomoro.R;
import com.example.quyet.podomoro.activities.TaskActivity;
import com.example.quyet.podomoro.adapters.TaskColorAdapter;
import com.example.quyet.podomoro.busEvent.DataSetChangeEvent;
import com.example.quyet.podomoro.busEvent.FailureNetworkEvent;
import com.example.quyet.podomoro.databases.DBContext;
import com.example.quyet.podomoro.databases.TaskManager;
import com.example.quyet.podomoro.databases.models.Task;
import com.example.quyet.podomoro.decoration.TaskColorDecor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskDetailFragment extends Fragment {

    private static final String TAG = TaskDetailFragment.class.toString();
    @BindView(R.id.rv_colors)
    RecyclerView rv_colors;
    @BindView(R.id.sw_isDone)
    Switch sw_isDone;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_payment)
    EditText payment;
    @BindView(R.id.til_name)
    TextInputLayout tilName;
    @BindView(R.id.til_payment)
    TextInputLayout tilPayment;
    TaskColorAdapter colorAdapter;
    private String title;
    private Task task;
    private int mode = -1;
    private Task newTask;

    public TaskDetailFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    ProgressDialog myDialog;

    EventBus bus = EventBus.getDefault();

    public void setTask(Task task) {
        this.task = task;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_task_detail, container, false);
        setupUI(view);
        addListener();
        bus.register(this);
        return view;
    }

    @Override
    public void onStop() {
        bus.unregister(this);
        super.onStop();
    }

    private void setupUI(View view) {
        ButterKnife.bind(this, view);
        //set layout managet
        rv_colors.setLayoutManager(new GridLayoutManager(this.getContext(), 4));
        // setAdapter
        colorAdapter = new TaskColorAdapter();
        rv_colors.setAdapter(colorAdapter);
        // add decoration
        rv_colors.addItemDecoration(new TaskColorDecor());
        // set title
        if (getActivity() instanceof TaskActivity) {
            ((TaskActivity) getActivity()).getSupportActionBar().setTitle(title);
        }
        if (task != null) {
            et_name.setText(task.getName());
            payment.setText(String.format("%s", task.getPayment_per_hour()));
            colorAdapter.setSelectedColor(task.getColor());
            if (task.isDone()) {
                sw_isDone.setChecked(true);
            } else {
                sw_isDone.setChecked(false);
            }
        }
        myDialog = new ProgressDialog(this.getActivity());
        myDialog.setMessage("Syncing...");
        myDialog.setCancelable(false);
        myDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().onBackPressed();
            }
        });


    }

    private void addListener() {
        payment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (payment.getText() == null) {
                    payment.setText("0");
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit_task, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item) {
            View view = this.getActivity().getCurrentFocus();
            //
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            //1 : get data from UI
            String taskName = et_name.getText().toString();
            ////validate input
            try {
                validateTaskName(taskName);
            } catch (Exception e) {
                tilName.setError(e.getMessage());
                return false;
            }
            try {
                validatePayment(payment.getText().toString());
            } catch (Exception e) {
                tilPayment.setError(e.getMessage());
                return false;
            }
            double paymentPerHour = Float.parseFloat(payment.getText().toString());
            String color = colorAdapter.getSelectedColor();
            boolean isDone = sw_isDone.isChecked();
            Log.d(TAG, String.format("onOptionsItemSelected: %s", isDone));
            // 2 : Create new Task
            newTask = new Task(taskName, color, paymentPerHour, isDone, "", "");

            if (task == null) {
                mode = 0;
                TaskManager.instance.addNewTask(newTask);
                myDialog.show();
            } else {
                mode = 1;
                TaskManager.instance.editTask(task);
                myDialog.show();
            }
        }
        return false;
    }

    @Subscribe
    public void OnDataSetChanged(DataSetChangeEvent dsce) {
        if (dsce.isChanged()) {
            switch (mode) {
                case 0:
                    DBContext.instance.add(newTask);
                    Log.d(TAG, "OnDataSetChanged: 0");
                    Toast.makeText(this.getContext(), R.string.saved, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    newTask.setLocal_id(task.getLocal_id());
                    newTask.setId(task.getId());
                    DBContext.instance.addOrUpdate(newTask);
                    Log.d(TAG, "OnDataSetChanged: 1");
                    Toast.makeText(this.getContext(), R.string.saved, Toast.LENGTH_SHORT).show();
                    break;
            }
            myDialog.dismiss();
            getActivity().onBackPressed();
        }
        else{
            switch (mode) {
                case 0:
                    Log.d(TAG, "OnDataSetChanged: parse fail");
                    Toast.makeText(this.getContext(), "Fail to add", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Log.d(TAG, "OnDataSetChanged: parse fail");
                    Toast.makeText(this.getContext(), "Fail to edit", Toast.LENGTH_SHORT).show();
                    break;
            }
            myDialog.dismiss();
            getActivity().onBackPressed();

        }
    }

    @Subscribe
    public void OnFailureNetwork(FailureNetworkEvent fne) {
        if (fne.isFailed()) {
            Toast.makeText(getContext(), "Can't sync data", Toast.LENGTH_SHORT).show();
            myDialog.dismiss();
            getActivity().onBackPressed();
        }
    }

    public void validateTaskName(String taskName) throws Exception {
        if (taskName.isEmpty()) {
            throw new Exception("Enter task name");
        }
    }

    public void validatePayment(String payment) throws Exception {

        if (payment.isEmpty()) {
            throw new Exception("Enter payment per hour");
        }
        try {
            float paymentPerHour = Float.parseFloat(payment);

        } catch (Exception e) {
            throw new Exception("Wrong format");
        }
    }


}
