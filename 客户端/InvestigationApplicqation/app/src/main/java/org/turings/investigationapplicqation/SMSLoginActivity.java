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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.turings.investigationapplicqation.Util.MobUtil;

import java.io.IOException;

public class SMSLoginActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView back;//返回
    private EditText edPhone;//手机号
    private EditText yanzhengma;//验证码
    private Button btn;//获取验证码
    private Button btnOk;//确认
    boolean isgrant=false;
    boolean debug=false;
    private MobUtil mobUtil1;
    int i = 30;

    private OkHttpClient okHttpClient;
    Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
//                getMNum.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
//                getMNum.setText("获取验证码");
//                getMNum.setClickable(true);
//                getMNum.setBackground(getDrawable(R.drawable.shape_btn1));
//                getMNum.setTextColor(getColor(R.color.colorMain));
                i = 30;
            } else if(msg.what == 1){
                //注册
                if(msg.obj.equals("注册成功")){
//                    tvLogin.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
//                    tvLogin.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//字体加粗
//                    ivLogin.setVisibility(View.VISIBLE);
//                    tvRegister.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
//                    ivRegister.setVisibility(View.INVISIBLE);
//                    tvRegister.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
//                    ly_login.setVisibility(View.VISIBLE);
//                    ly_register.setVisibility(View.GONE);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        ly_phone_login.setElevation(10.0f);
//                        ly_psd_login.setElevation(0);
//                    }
                    Toast.makeText(getApplicationContext(), "注册成功，现在登录吧",
                            Toast.LENGTH_SHORT).show();

                }else if(msg.obj.equals("注册失败，请重试")){
                    Toast.makeText(getApplicationContext(), "失败请重试",
                            Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "号码注册过",
                            Toast.LENGTH_SHORT).show();
                }
            } else if(msg.what == 2){
                if(msg.obj.equals("用户名或密码不匹配")){
                    Toast.makeText(getApplicationContext(), "用户名密码不匹配",
                            Toast.LENGTH_SHORT).show();
                }else {
                    //进入主activity
//                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
//                    User user = gson.fromJson(msg.obj.toString(),new TypeToken<User>(){}.getType());
//                    SharedPreferences sharedPreferences=getSharedPreferences("userInfo",MODE_PRIVATE);
//                    SharedPreferences.Editor editor=sharedPreferences.edit();
//                    editor.putString("phone",user.getPhone());
//                    editor.putString("uId",Integer.toBinaryString(user.getId()));
//                    editor.commit();
//                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                    intent.setAction("work");
//                    startActivity(intent);
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
        setContentView(R.layout.activity_smslogin);
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
                    Log.d("MobActivity", "onClick: 发送验证码");

//                mobUtil1.getVerrificationCode(MobUtil.CN, phoneNums, new MobUtil.MobGetcodeListener() {
//                    @Override
//                    public void onSuccess() {
//                        Log.i("www", "onSuccess: 验证码");
//                        Toast.makeText(getApplicationContext(),"成功请求验证码",Toast.LENGTH_SHORT).show();
//                        //写自己逻辑
//                    }
//
//                    @Override
//                    public void onfail() {
//                        Toast.makeText(getApplicationContext(),"失败请求验证码",Toast.LENGTH_SHORT).show();
//                        //写自己逻辑
//                    }
//                });
                break;
            case R.id.btn_register://数据库修改密码
                mobUtil1.submitVerrificationCode(MobUtil.CN, phoneNums, yanzhengma.getText().toString().trim(), new MobUtil.MobSendListener() {
                    @Override
                    public void onSuccess() {
                        //去数据库比对号码，是否存在账号
                        Toast.makeText(getApplicationContext(),"去登录",Toast.LENGTH_SHORT).show();
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        uploadToDataBase();
//                                    }
//                                }).start();

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
    //访问服务器上传至数据库，搜索
    private void uploadToDataBase() {
        okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("phone", edPhone.getText().toString())
                .build();
        Log.i("www", "uploadToDataBase: "+ edPhone.getText().toString());
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
