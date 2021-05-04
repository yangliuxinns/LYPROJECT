package org.turings.investigationapplicqation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 版本
 */
public class EditionActivity extends AppCompatActivity {

    private ImageView back;
    private TextView js;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edition);
        getViews();
    }

    //获取控件
    private void getViews() {
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        js = findViewById(R.id.js);
        js.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),FunctionIntroductionActivity.class);
                startActivity(intent);
            }
        });
    }
}
