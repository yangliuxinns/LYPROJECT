package org.turings.investigationapplicqation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.turings.investigationapplicqation.Entity.User;

//修改名字
public class FixNameActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private TextView save;//保存
    private EditText fixName;//修改名字
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_name);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        getViews();
        fixName.setText(user.getUser_name());
    }

    //获取控件
    private void getViews() {
        back = findViewById(R.id.back);
        save = findViewById(R.id.save);
        fixName = findViewById(R.id.fixname);
        back.setOnClickListener(this);
        fixName.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                Intent intent1 = new Intent();
                //把返回数据存入Intent
                intent1.putExtra("q_data",user);
                //设置返回数据
                setResult(1, intent1);//RESULT_OK为自定义常量
                //关闭Activity
                finish();
                break;
            case R.id.save:
                //去保存
                break;
        }
    }
}
