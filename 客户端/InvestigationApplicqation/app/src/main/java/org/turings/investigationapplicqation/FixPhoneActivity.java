package org.turings.investigationapplicqation;

import androidx.annotation.NonNull;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.turings.investigationapplicqation.Entity.User;
import org.turings.investigationapplicqation.Util.MobUtil;

import java.io.IOException;

//修改手机号
public class FixPhoneActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn;
    private ImageView back;
    private EditText fixphone;
    private EditText yanzhengma;
    private Button get_m;
    boolean isgrant=false;
    boolean debug=false;
    private MobUtil mobUtil2;
    private User user;
    private OkHttpClient okHttpClient;
    int i = 30;
    private EventHandler eventHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_phone);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        getViews();
        fixphone.setText(user.getPhone());
    }

    //获取控件
    private void getViews() {
        btn = findViewById(R.id.btn);
        back = findViewById(R.id.back);
        fixphone = findViewById(R.id.fixphone);
        yanzhengma = findViewById(R.id.yanzhengma);
        get_m = findViewById(R.id.get_m);
        back.setOnClickListener(this);
        get_m.setOnClickListener(this);
        btn.setOnClickListener(this);
    }
    public void initdata(){
        mobUtil2=MobUtil.getInstance();
        //为按钮添加倒计时 可选
        mobUtil2.setCountDown(get_m,30);
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

    @Override
    public void onClick(View v) {
        if(!isgrant){
            applyPermission();
        }
        String phoneNums = fixphone.getText().toString().trim();
        switch (v.getId()){
            case R.id.back://取消修改
                Intent intent1 = new Intent();
                //把返回数据存入Intent
                intent1.putExtra("q_data",user);
                //设置返回数据
                setResult(2, intent1);//RESULT_OK为自定义常量
                //关闭Activity
                finish();
                break;
            case R.id.get_m://获取验证码
                // 1. 通过规则判断手机号
                if (!judgePhoneNums(phoneNums)) {
                    return;
                } // 2. 通过sdk发送短信验证
                if (debug)
                    Log.d("MobActivity", "onClick: 发送验证码");

                mobUtil2.getVerrificationCode(MobUtil.CN, phoneNums, new MobUtil.MobGetcodeListener() {
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
            case R.id.btn://修改设置
                mobUtil2.submitVerrificationCode(MobUtil.CN, phoneNums, yanzhengma.getText().toString().trim(), new MobUtil.MobSendListener() {
                    @Override
                    public void onSuccess() {
                        if(fixphone.getText().toString().equals("") || fixphone.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(),"手机号不能为空",Toast.LENGTH_SHORT).show();
                        }else {
                            //去注册
                            Toast.makeText(getApplicationContext(),"修改",Toast.LENGTH_SHORT).show();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    uploadToDataBase(phoneNums,user.getId());
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
    private void uploadToDataBase(String phone,int userId) {
        okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("name",phone)
                .add("userId", String.valueOf(userId))
                .build();
        String url = "http://" + getResources().getString(R.string.ipConfig) + ":8080/WorkProject/ylx/fixPhone";
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
                            mobUtil2.unregister();
                            user.setPhone(phone);
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
