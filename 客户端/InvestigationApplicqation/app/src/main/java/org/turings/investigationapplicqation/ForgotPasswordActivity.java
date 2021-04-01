package org.turings.investigationapplicqation;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.turings.investigationapplicqation.Entity.User;
import org.turings.investigationapplicqation.Util.MobUtil;

import java.io.IOException;

//忘记密码
public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    public static void actionStart(Context context){
        Intent intent=new Intent(context,ForgotPasswordActivity.class);
        context.startActivity(intent);
    }
    private ImageView back;//返回
    private EditText edPhone;//手机号
    private EditText yanzhengma;//验证码
    private Button btn;//获取验证码
    private EditText edPsd;//密码
    private EditText edPsds;//重复输入
    private Button btnOk;//确认
    boolean isgrant=false;
    boolean debug=false;
    private MobUtil mobUtil1;
    int i = 30;

    private OkHttpClient okHttpClient;
    Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                //注册
                if(msg.obj.equals("修改成功")){
                    mobUtil1.unregister();
                    Intent intent = new Intent(getApplicationContext(),FixYesActivity.class);
                    startActivity(intent);
                    finish();

                }else if(msg.obj.equals("修改失败，请重试")){
                    Toast.makeText(getApplicationContext(), "失败请重试",
                            Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "不存在该账号",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private EventHandler eventHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getViews();
        getRegister();
        initdata();
    }

    //注册监听器
    private void getRegister() {
        btnOk.setOnClickListener(this);
        btn.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    //获取控件
    private void getViews() {
        back = findViewById(R.id.back);
        edPhone = findViewById(R.id.ed_phone);
        yanzhengma = findViewById(R.id.yanzhengma);
        btn = findViewById(R.id.get_m);
        edPsd = findViewById(R.id.ed_psd);
        edPsds = findViewById(R.id.ed_psd2);
        btnOk =findViewById(R.id.btn_register);
    }
    public void initdata(){
        mobUtil1=new MobUtil();
        //为按钮添加倒计时 可选
        mobUtil1.setCountDown(btn,30);
    };

    private void applyPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /**
             * API23以上版本需要发起写文件权限请求
             */
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,},1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            //上面请求时候的请求码
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {
                    //授权
                    isgrant=true;
                }
                else{
                    Toast.makeText(this,"未同意权限可能导致功能不可使用",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        if(!isgrant){
            applyPermission();
        }
        String phoneNums = edPhone.getText().toString().trim();
        switch (view.getId()){
            case R.id.back:
                //返回
                mobUtil1.unregister();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setAction("work");
                startActivity(intent);
                finish();
                break;
            case R.id.get_m:
                //获取验证码
                // 1. 通过规则判断手机号
                if (!judgePhoneNums(phoneNums)) {
                    return;
                } // 2. 通过sdk发送短信验证
                if (debug)
                    Log.i("www", "onClick:hhh ");
                mobUtil1.getVerrificationCode(MobUtil.CN, phoneNums, new MobUtil.MobGetcodeListener() {
                    @Override
                    public void onSuccess() {
                        Log.i("www", "onSuccess: 验证码");
                        Toast.makeText(getApplicationContext(),"成功请求验证码",Toast.LENGTH_SHORT).show();
                        //写自己逻辑
                    }

                    @Override
                    public void onfail() {
                        Toast.makeText(getApplicationContext(),"失败请求验证码",Toast.LENGTH_SHORT).show();
                        //写自己逻辑
                    }
                });
                break;
            case R.id.btn_register://数据库修改密码
                mobUtil1.submitVerrificationCode(MobUtil.CN, phoneNums, yanzhengma.getText().toString().trim(), new MobUtil.MobSendListener() {
                    @Override
                    public void onSuccess() {
                        if(edPsd.getText().toString().equals("") || edPsd.getText().toString().isEmpty() || edPsds.getText().toString().equals("") || edPsds.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                        }else {
                            //判断密码是不是一样的
                            if(edPsd.getText().toString().trim().equals(edPsds.getText().toString().trim())){
                                //去查询修改，返回结果
                                //去注册
                                Toast.makeText(getApplicationContext(),"去修改",Toast.LENGTH_SHORT).show();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        uploadToDataBase();
                                    }
                                }).start();
                            }else {
                                Toast.makeText(getApplicationContext(),"密码不一致",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onfail() {
                        Toast.makeText(getApplicationContext(), "验证码错误",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11)
                && isMobileNO(phoneNums)) {
            return true;
        }
        Toast.makeText(this, "手机号码输入有误！",Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 判断一个字符串的位数
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
    //去修改
    private void uploadToDataBase() {
        okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("phone", edPhone.getText().toString())
                .add("psd", edPsd.getText().toString())
                .build();
        Log.i("www", "uploadToDataBase: "+ edPhone.getText().toString()+edPsd.getText().toString());
        String url = "http://" + getResources().getString(R.string.ipConfig) + ":8080/WorkProject/ylx/fixPsd";
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
