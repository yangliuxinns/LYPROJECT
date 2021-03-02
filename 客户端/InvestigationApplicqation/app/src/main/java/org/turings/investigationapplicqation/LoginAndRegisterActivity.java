package org.turings.investigationapplicqation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoginAndRegisterActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener{

    private TextView tvLogin;//登录文字
    private ImageView ivLogin;//登录选择图标
    private TextView tvRegister;//注册文字
    private ImageView ivRegister;//注册选择图标
    private LinearLayout ly_login_item;
    private LinearLayout ly_register_item;
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
    //注册模块
    private LinearLayout ly_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_and_register_layout);
        getViews();
        init();

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
        QQlogin = findViewById(R.id.QQ_login);
        weixinLogin = findViewById(R.id.weixin_login);
        codeLogin = findViewById(R.id.ly_code_login);
        ly_register = findViewById(R.id.ly_register);
        ly_login_item.setOnClickListener(this);
        ly_register_item.setOnClickListener(this);
        edPhone.setOnTouchListener(this);
        edPsd.setOnTouchListener(this);
        btnLogin.setOnClickListener(this);
    }

    //点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ly_login_item:
                init();
                break;
            case R.id.ly_register_item:
                registerInit();
                break;
            case R.id.btn_login:
                validateLogin();
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
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
