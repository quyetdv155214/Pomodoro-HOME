package com.example.quyet.podomoro.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quyet.podomoro.R;
import com.example.quyet.podomoro.activities.TaskActivity;
import com.example.quyet.podomoro.adapters.TaskColorAdapter;
import com.example.quyet.podomoro.decoration.TaskColorDecor;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskDetailFragment extends Fragment implements FragmentListener{

    @BindView(R.id.rv_colors)
    RecyclerView rv_colors;


    @BindView(R.id.et_payment)
    EditText payment;
    public TaskDetailFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_task_detail, container, false);
        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        ButterKnife.bind(this,view);

        //set layout managet
        rv_colors.setLayoutManager(new GridLayoutManager(this.getContext(),4));
        // setAdapter
        TaskColorAdapter colorAdapter = new TaskColorAdapter();
        rv_colors.setAdapter(colorAdapter);
        // add decoration
        rv_colors.addItemDecoration(new TaskColorDecor());
        //

        // set title
        if(getActivity() instanceof  TaskActivity){
           ((TaskActivity) getActivity()).getSupportActionBar().setTitle(R.string.add_new_task);
        }

    }
    private  void addListener(){
        payment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (payment.getText() == null){
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
        Toast.makeText(this.getContext(), "Save", Toast.LENGTH_SHORT).show();
        TaskFragment taskFragment = new TaskFragment();
        // // TODO: 2/14/2017 pop back stack  
//        replaceFragment(taskFragment, false);

        return true;
    }

    @Override
    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
            new ManagerFragment(this.getActivity().getSupportFragmentManager(),R.id.fl_main)
                    .replaceFragment(fragment,addToBackStack);

    }
}
