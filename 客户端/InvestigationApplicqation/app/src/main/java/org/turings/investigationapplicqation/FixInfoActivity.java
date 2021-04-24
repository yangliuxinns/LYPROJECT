package org.turings.investigationapplicqation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class FixInfoActivity extends AppCompatActivity {

    private EditText edy;//输入原密码
    private EditText edn;//输入新密码
    private EditText edq;//输入确认密码
    private Button btn;//确认修改
    private String uId;
    private ImageView back;//返回
    private OkHttpClient okHttpClient;

    Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            if(msg.what == 1){
               if(msg.obj.equals("原密码错误，不能修改")){
                   Toast.makeText(getApplicationContext(),"原密码错误，修改失败",Toast.LENGTH_SHORT).show();
               }else if(msg.obj.equals("修改失败")){
                   Toast.makeText(getApplicationContext(),"修改失败,请重试",Toast.LENGTH_SHORT).show();
               }else {
                   Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                   intent.setAction("self");
                   startActivity(intent);
                   finish();
               }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_info);
        Intent intent = getIntent();
        uId = intent.getStringExtra("uId");
        getViews();
    }

    //获取控件
    private void getViews() {
        edy = findViewById(R.id.yMsd);
        edn = findViewById(R.id.nMsd);
        edq = findViewById(R.id.qMsd);
        btn = findViewById(R.id.btn);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setAction("self");
                startActivity(intent);
                finish();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edy.getText().toString().isEmpty() || edy.getText().toString().equals("")){
                    //有空的
                    Toast.makeText(getApplicationContext(),"原密码不能为空",Toast.LENGTH_SHORT).show();
                }else if(edn.getText().toString().isEmpty() || edn.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"新密码不能为空",Toast.LENGTH_SHORT).show();
                }else if(edq.getText().toString().isEmpty() || edq.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"确认密码不能为空",Toast.LENGTH_SHORT).show();
                }else if(!edn.getText().toString().equals(edq.getText().toString())){
                    Toast.makeText(getApplicationContext(),"修改的密码不一致，请检查",Toast.LENGTH_SHORT).show();
                }else {
                    //后台修改
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            uploadToDataBase(edq.getText().toString().trim(),edy.getText().toString().trim(),uId);
                        }
                    }).start();
                }
            }
        });
    }

    //去修改
    private void uploadToDataBase(String psd,String nPsd,String userId) {
        Log.i("rrr", "uploadToDataBase: 原密码"+nPsd);
        okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("psd",psd.trim())
                .add("nPsd",nPsd.trim())
                .add("userId",userId)
                .build();
        String url = "http://" + getResources().getString(R.string.ipConfig) + ":8080/WorkProject/ylx/fixMsd";
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
                        Message msg = Message.obtain();
                        msg.obj = str;
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                });
            }
        }).start();
    }
}
