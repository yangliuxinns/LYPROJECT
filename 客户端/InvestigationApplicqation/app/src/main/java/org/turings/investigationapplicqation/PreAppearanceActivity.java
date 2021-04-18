package org.turings.investigationapplicqation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//预览效果
public class PreAppearanceActivity extends AppCompatActivity {

    private ImageView img;
    private TextView title;
    private ImageView back;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_appearance);
        Intent intent = getIntent();
        String imgph = (String) intent.getSerializableExtra("img");
        String ti    = (String) intent.getSerializableExtra("title");
        String color = intent.getStringExtra("color");
        img = findViewById(R.id.img);
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        int icon = getResources().getIdentifier(imgph, "mipmap",getPackageName());
        img.setImageResource(icon);
        title.setText(ti);
        title.setTextColor(Color.parseColor(color));
        btn = findViewById(R.id.btn);
        btn.setBackgroundColor(Color.parseColor(color));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
