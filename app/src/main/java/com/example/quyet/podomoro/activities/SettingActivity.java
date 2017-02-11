package com.example.quyet.podomoro.activities;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.quyet.podomoro.R;
import com.example.quyet.podomoro.adapters.ColorTableAdapter;

import butterknife.BindView;

public class SettingActivity extends AppCompatActivity {
    @BindView(R.id.rv_color_Choose)
    RecyclerView rv_color_Choose;

    private ColorTableAdapter colorTableAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();

        setContentView(R.layout.activity_setting);
    }
    private void setupUI() {
        colorTableAdapter = new ColorTableAdapter();
        rv_color_Choose.setAdapter(colorTableAdapter);
        rv_color_Choose.setLayoutManager(new GridLayoutManager(this,5));

    }

}
