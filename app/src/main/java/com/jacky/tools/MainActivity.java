package com.jacky.tools;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.SparseArray;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new SparseArray<>(4);
    }
}
