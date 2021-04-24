package org.turings.investigationapplicqation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.turings.investigationapplicqation.Entity.User;

//修改用户名和手机号
public class FixPAndMActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back2;//返回键
    private LinearLayout lyName2;//修改名字
    private LinearLayout lyPhone2;//修改手机号
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_pand_m);
        //得到数据
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        getViews();
        getRegister();
    }


    //绑定控件
    private void getViews() {
        back2=findViewById(R.id.back2);
        lyName2=findViewById(R.id.ly_name);
        lyPhone2=findViewById(R.id.ly_phone);

    }
    //绑定监听器
    private void getRegister() {
        back2.setOnClickListener(this);
        lyName2.setOnClickListener(this);
        lyPhone2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back2: //返回
                Intent intent = new Intent(this,MainActivity.class);
                intent.setAction("self");
                startActivity(intent);
                break;
            case R.id.ly_name://修改名字
                Intent intent1 = new Intent(this,FixNameActivity.class);
                intent1.putExtra("user",user);
                startActivityForResult(intent1,1);
                break;
            case R.id.ly_phone://修改手机号
                Intent intent2 = new Intent(this,FixPhoneActivity.class);
                intent2.putExtra("user",user);
                startActivityForResult(intent2,2);
                break;

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode){
            case 1://修改名字
                user = (User) data.getSerializableExtra("q_data");
                break;
            case 2://修改手机号
                user = (User) data.getSerializableExtra("q_data");
                SharedPreferences sharedPreferences= getSharedPreferences("userInfo",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("phone",user.getPhone());
                break;

        }
    }
}
