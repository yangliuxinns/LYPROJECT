package org.turings.investigationapplicqation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ClauseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clause);
        Intent intent = getIntent();
        String str = intent.getStringExtra("data");
        if(str.equals("隐私条款")){

        }else {

        }
    }
}
