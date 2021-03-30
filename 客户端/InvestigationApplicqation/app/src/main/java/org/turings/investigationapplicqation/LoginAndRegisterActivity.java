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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mob.MobSDK;
import com.mob.OperationCallback;

import org.json.JSONObject;
import org.turings.investigationapplicqation.Entity.Questionnaire;
import org.turings.investigationapplicqation.Entity.User;
import org.turings.investigationapplicqation.Util.MobUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LoginAndRegisterActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener{

    public static void actionStart(Context context){
        Intent intent=new Intent(context,LoginAndRegisterActivity.class);
        context.startActivity(intent);
    }
    private TextView tvLogin;//登录文字
    private ImageView ivLogin;//登录选择图标
    private TextView tvRegister;//注册文字
    private ImageView ivRegister;//注册选择图标
    private LinearLayout ly_login_item;
    private LinearLayout ly_register_item;
    private OkHttpClient okHttpClient;
    //登录模块
    private LinearLayout ly_login;
    private LinearLayout ly_phone_login;
    private LinearLayout ly_psd_login;
    private EditText edPhone;//手机号
    private EditText edPsd;//密码
    private LinearLayout forgetPsd;//忘记密码
    private Button btnLogin;//登录按钮
    private LinearLayout QQlogin;//qq登录
    private LinearLayout weixinLogin;//微信登录
    private LinearLayout codeLogin;//验证码登录
    private Button btnDuan;//短信登录
    //注册模块
    private LinearLayout ly_register;
    private EditText ePhone;//手机号
    private EditText yanzhengma;//验证码
    private Button getMNum;//获取验证密码
    private EditText ed_psd;//密码
    private Button btn_register;//立即注册
    private TextView serviceTv;//用户协议
    private TextView secretTv;//隐私条款
    boolean isgrant=false;
    boolean debug=false;
    private MobUtil mobUtil;
    int i = 30;
    Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                getMNum.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                getMNum.setText("获取验证码");
                getMNum.setClickable(true);
                getMNum.setBackground(getDrawable(R.drawable.shape_btn1));
                getMNum.setTextColor(getColor(R.color.colorMain));
                i = 30;
            } else if(msg.what == 1){
                //注册
                if(msg.obj.equals("注册成功")){
                    tvLogin.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                    tvLogin.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//字体加粗
                    ivLogin.setVisibility(View.VISIBLE);
                    tvRegister.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                    ivRegister.setVisibility(View.INVISIBLE);
                    tvRegister.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    ly_login.setVisibility(View.VISIBLE);
                    ly_register.setVisibility(View.GONE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ly_phone_login.setElevation(10.0f);
                        ly_psd_login.setElevation(0);
                    }
                    Toast.makeText(getApplicationContext(), "注册成功，现在登录吧",
                            Toast.LENGTH_SHORT).show();
                    mobUtil.unregister();

                }else if(msg.obj.equals("注册失败，请重试")){
                    Toast.makeText(getApplicationContext(), "失败请重试",
                            Toast.LENGTH_SHORT).show();
                    mobUtil.unregister();
                }else {
                    Toast.makeText(getApplicationContext(), "号码注册过",
                            Toast.LENGTH_SHORT).show();
                    mobUtil.unregister();
                }
            } else if(msg.what == 2){
                if(msg.obj.equals("用户名或密码不匹配")){
                    Toast.makeText(getApplicationContext(), "用户名密码不匹配",
                            Toast.LENGTH_SHORT).show();
                    mobUtil.unregister();
                }else {
                    //进入主activity
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
                    User user = gson.fromJson(msg.obj.toString(),new TypeToken<User>(){}.getType());
                    SharedPreferences sharedPreferences=getSharedPreferences("userInfo",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("phone",user.getPhone());
                    editor.putString("uId",Integer.toBinaryString(user.getId()));
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.setAction("work");
                    startActivity(intent);
                    mobUtil.unregister();
                }
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回MainActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                        Toast.makeText(getApplicationContext(), "提交验证码成功",
                                Toast.LENGTH_SHORT).show();
                        //进行注册
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "正在获取验证码",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "验证码错误",
                                Toast.LENGTH_SHORT).show();
                        ((Throwable) data).printStackTrace();
                    }
                }
            }
        }
    };
    private EventHandler eventHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_and_register_layout);
//        MobSDK.submitPolicyGrantResult(true, null);
//        submitPrivacyGrantResult(true);
//        MobSDK.init(LoginAndRegisterActivity.this, "327a6c80f76ae","833db4d1aa8f4aa50cc5edf763078db3");
        getViews();
        init();
//        eventHandler = new EventHandler(){
//            @Override
//            public void afterEvent(int event, int result, Object data) {
//                Message msg = new Message();
//                msg.arg1 = event;
//                msg.arg2 = result;
//                msg.obj = data;
//                handler.sendMessage(msg);
//            }
//        };
        //注册回调监听接口
//        SMSSDK.registerEventHandler(eventHandler);
        initdata();
    }
    //初始化
    private void init() {
        tvLogin.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        tvLogin.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//字体加粗
        ivLogin.setVisibility(View.VISIBLE);
        tvRegister.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        ivRegister.setVisibility(View.INVISIBLE);
        tvRegister.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        ly_login.setVisibility(View.VISIBLE);
        ly_register.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ly_phone_login.setElevation(10.0f);
            ly_psd_login.setElevation(0);
        }
    }

    //获取控件
    private void getViews() {
        tvLogin = findViewById(R.id.tv_login);
        ivLogin = findViewById(R.id.iv_login);
        tvRegister = findViewById(R.id.tv_register);
        ivRegister = findViewById(R.id.iv_register);
        ly_login_item=findViewById(R.id.ly_login_item);
        ly_register_item = findViewById(R.id.ly_register_item);
        ly_login = findViewById(R.id.ly_login);
        ly_phone_login = findViewById(R.id.ly_phone_login);
        ly_psd_login = findViewById(R.id.ly_psd_login);
        edPhone = findViewById(R.id.et_phone_login);
        edPsd = findViewById(R.id.et_psd_login);
        forgetPsd = findViewById(R.id.ly_forget_psd);
        btnLogin = findViewById(R.id.btn_login);
//        QQlogin = findViewById(R.id.QQ_login);
//        weixinLogin = findViewById(R.id.weixin_login);
//        codeLogin = findViewById(R.id.ly_code_login);
        ly_register = findViewById(R.id.ly_register);
        ePhone = findViewById(R.id.ed_phone);//手机号
        yanzhengma = findViewById(R.id.yanzhengma);//验证码
        getMNum = findViewById(R.id.get_m);//获取验证密码
        ed_psd = findViewById(R.id.ed_psd);//密码
        btn_register = findViewById(R.id.btn_register);//立即注册
        serviceTv = findViewById(R.id.service_tv);
        secretTv = findViewById(R.id.secret_tv);
        btnDuan = findViewById(R.id.btn_duan);
        ly_login_item.setOnClickListener(this);
        ly_register_item.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        edPhone.setOnTouchListener(this);
        edPsd.setOnTouchListener(this);
        btnLogin.setOnClickListener(this);
        getMNum.setOnClickListener(this);
        secretTv.setOnClickListener(this);
        serviceTv.setOnClickListener(this);
        forgetPsd.setOnClickListener(this);
        btnDuan.setOnClickListener(this);
    }
    public void initdata(){
        mobUtil=new MobUtil();
        //为按钮添加倒计时 可选
        mobUtil.setCountDown(getMNum,30);
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

    //点击事件
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        if(!isgrant){
            applyPermission();
        }
        String phoneNums = ePhone.getText().toString().trim();
        switch (view.getId()){
            case R.id.ly_login_item:
                init();
                break;
            case R.id.ly_register_item:
                registerInit();
                break;
            case R.id.get_m://点击获取验证码
                // 1. 通过规则判断手机号
                if (!judgePhoneNums(phoneNums)) {
                    return;
                } // 2. 通过sdk发送短信验证
                if (debug)
                    Log.d("MobActivity", "onClick: 发送验证码");

                mobUtil.getVerrificationCode(MobUtil.CN, phoneNums, new MobUtil.MobGetcodeListener() {
                    @Override
                    public void onSuccess() {
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
            case R.id.btn_register://注册
                mobUtil.submitVerrificationCode(MobUtil.CN, phoneNums, yanzhengma.getText().toString().trim(), new MobUtil.MobSendListener() {
                    @Override
                    public void onSuccess() {
                        if(ed_psd.getText().toString().equals("") || ed_psd.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                        }else {
                            //去注册
                            Toast.makeText(getApplicationContext(),"注册",Toast.LENGTH_SHORT).show();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    uploadToDataBase();
                                }
                            }).start();
                        }
                    }

                    @Override
                    public void onfail() {
                        Toast.makeText(getApplicationContext(), "验证码错误",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btn_login:
                validateLogin();
                break;
            case R.id.secret_tv://隐私条款

                break;
            case R.id.service_tv://用户服务协议
                break;
            case R.id.ly_forget_psd://忘记密码
                mobUtil.unregister();
                Intent intent = new Intent(getApplicationContext(),ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_duan:
                mobUtil.unregister();
                Intent intent1 = new Intent(getApplicationContext(),SMSLoginActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
    }
    //触摸事件
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.et_psd_login:
                Log.v("www", "onClick:hhhhhhhhhhhhhhhhhhhhh ");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ly_phone_login.setElevation(0);
                    ly_psd_login.setElevation(10.0f);
                }
                break;
            case R.id.et_phone_login:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ly_phone_login.setElevation(10.0f);
                    ly_psd_login.setElevation(0);
                }
                break;
        }
        return false;
    }
    //注册事件初始化
    private void registerInit(){
        tvLogin.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        tvLogin.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//字体加粗
        ivLogin.setVisibility(View.INVISIBLE);
        tvRegister.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        ivRegister.setVisibility(View.VISIBLE);
        tvRegister.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        ly_login.setVisibility(View.GONE);
        ly_register.setVisibility(View.VISIBLE);
    }

    //登录验证
    private void validateLogin(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                loginToDataBase();
            }
        }).start();
//        Intent intent = new Intent(this,MainActivity.class);
//        startActivity(intent);
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

    /**
     * progressbar
     */
    private void createProgressBar() {
        FrameLayout layout = (FrameLayout) findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        ProgressBar mProBar = new ProgressBar(this);
        mProBar.setLayoutParams(layoutParams);
        mProBar.setVisibility(View.VISIBLE);
        layout.addView(mProBar);
    }

    private void submitPrivacyGrantResult(boolean granted) {
        MobSDK.submitPolicyGrantResult(granted, new OperationCallback<Void>() {
            @Override
            public void onComplete(Void data) {
                Log.d("www", "隐私协议授权结果提交：成功");
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("www", "隐私协议授权结果提交：失败");
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    //访问服务器上传至数据库，搜索
    private void uploadToDataBase() {
        okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("phone", ePhone.getText().toString())
                .add("psd", ed_psd.getText().toString())
                .build();
        Log.i("www", "uploadToDataBase: "+ edPhone.getText().toString()+edPsd.getText().toString());
        String url = "http://" + getResources().getString(R.string.ipConfig) + ":8080/WorkProject/ylx/register";
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
    //访问服务器上传至数据库，搜索
    private void loginToDataBase() {
        okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("phone", edPhone.getText().toString())
                .add("psd", edPsd.getText().toString())
                .build();
        String url = "http://" + getResources().getString(R.string.ipConfig) + ":8080/WorkProject/ylx/login";
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
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }
                });
            }
        }).start();
    }
}
