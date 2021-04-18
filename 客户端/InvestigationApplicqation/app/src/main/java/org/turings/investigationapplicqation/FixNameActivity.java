package org.turings.investigationapplicqation;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.turings.investigationapplicqation.Entity.User;

import java.io.IOException;

//修改名字
public class FixNameActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private TextView save;//保存
    private EditText fixName;//修改名字
    private User user;
    private OkHttpClient okHttpClient;
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
        save.setOnClickListener(this);
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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadToDataBase(fixName.getText().toString(),user.getId());
                    }
                }).start();

                break;
        }
    }
    //去修改
    private void uploadToDataBase(String name,int userId) {
        okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("name",name)
                .add("userId", String.valueOf(userId))
                .build();
        String url = "http://" + getResources().getString(R.string.ipConfig) + ":8080/WorkProject/ylx/fixName";
        final Request request = new Request.Builder().post(formBody).url(url).build();
        final Call call = okHttpClient.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //异步请求
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("lww", "请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String str = response.body().string();
                        if(str.equals("修改失败，请重试")){
                            Toast.makeText(getApplicationContext(),"修改失败，请重试",Toast.LENGTH_SHORT).show();
                        }else {
                            user.setUser_name(name);
                            Intent intent1 = new Intent();
                            //把返回数据存入Intent
                            intent1.putExtra("q_data",user);
                            //设置返回数据
                            setResult(1, intent1);//RESULT_OK为自定义常量
                            //关闭Activity
                            finish();
                        }
                    }
                });
            }
        }).start();
    }
}
