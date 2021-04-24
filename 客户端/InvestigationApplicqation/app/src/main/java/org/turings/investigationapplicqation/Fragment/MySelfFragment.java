package org.turings.investigationapplicqation.Fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;

import org.turings.investigationapplicqation.CircleImageView;
import org.turings.investigationapplicqation.Entity.User;
import org.turings.investigationapplicqation.FixInfoActivity;
import org.turings.investigationapplicqation.FixPAndMActivity;
import org.turings.investigationapplicqation.LoginAndRegisterActivity;
import org.turings.investigationapplicqation.MainActivity;
import org.turings.investigationapplicqation.R;
import org.turings.investigationapplicqation.RubbishActivity;

import java.io.IOException;
import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

//个人中心
public class MySelfFragment extends Fragment implements View.OnClickListener {

    private LinearLayout ly_info;
    private LinearLayout ly_project;
    private LinearLayout ly_rubbish;
    private LinearLayout ly_fixPsd;
    private LinearLayout ly_cancellation;
    private LinearLayout ly_edition;
    private LinearLayout ly_canel;
    private View view;
    private CircleImageView circleImageView;
    private ImagePicker imagePicker = new ImagePicker();
    private TextView name;
    private TextView phone;
    private LinearLayout info;
    private User user1;
    private OkHttpClient okHttpClient;

    private String uid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.myself_layout,container,false);
        init();
        getViews();
        getResiter();
        // 设置标题
        imagePicker.setTitle("设置头像");
        // 设置是否裁剪图片
        imagePicker.setCropImage(true);
        return view;
    }


    //初始化
    private void init() {
        //获得头像用户名手机号等信息
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("userInfo",MODE_PRIVATE);
        uid= sharedPreferences.getString("uId","");
        //搜索user
        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadToDataBase(uid);
            }
        }).start();
    }

    //注册
    private void getResiter() {
        ly_project.setOnClickListener(this);
        circleImageView.setOnClickListener(this);
        info.setOnClickListener(this);
        ly_rubbish.setOnClickListener(this);
        ly_canel.setOnClickListener(this);
        ly_fixPsd.setOnClickListener(this);
    }

    private void getViews() {
        ly_info = view.findViewById(R.id.ly_info);
        ly_project = view.findViewById(R.id.ly_project);
        ly_rubbish = view.findViewById(R.id.ly_rubbish);
        ly_fixPsd = view.findViewById(R.id.ly_fixPsd);
        ly_cancellation = view.findViewById(R.id.ly_cancellation);
        ly_edition = view.findViewById(R.id.ly_edition);
        ly_canel = view.findViewById(R.id.ly_canel);
        circleImageView = view.findViewById(R.id.imgView);
        name = view.findViewById(R.id.name);
        phone = view.findViewById(R.id.phone);
        info = view.findViewById(R.id.info);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    private void startChooser() {
        // 启动图片选择器
        imagePicker.startChooser(this, new ImagePicker.Callback() {
            // 选择图片回调
            @Override public void onPickImage(Uri imageUri) {
            }

            // 裁剪图片回调
            @Override public void onCropImage(Uri imageUri) {
                String uri = String.valueOf(imageUri);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadToDataBase(uri,uid);
                    }
                }).start();
            }

            // 自定义裁剪配置
            @Override public void cropConfig(CropImage.ActivityBuilder builder) {
                builder
                        // 是否启动多点触摸
                        .setMultiTouchEnabled(false)
                        // 设置网格显示模式
                        .setGuidelines(CropImageView.Guidelines.OFF)
                        // 圆形/矩形
                        .setCropShape(CropImageView.CropShape.OVAL)
                        // 调整裁剪后的图片最终大小
                        .setRequestedSize(600, 600)
                        // 宽高比
                        .setAspectRatio(1, 1);
            }

            // 用户拒绝授权回调
            @Override public void onPermissionDenied(int requestCode, String[] permissions,
                                                     int[] grantResults) {
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.info://修改个人信息
                Intent intent1 = new Intent(view.getContext(), FixPAndMActivity.class);
                intent1.putExtra("user", (Serializable) user1);
                startActivity(intent1);
                getActivity().finish();
                break;
            case R.id.ly_project://我的项目
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.setTab(0);
                break;
            case R.id.imgView:
                //调用裁剪
                startChooser();
                break;
            case R.id.ly_rubbish:
                Intent intent2 = new Intent(view.getContext(), RubbishActivity.class);
                startActivity(intent2);
                break;
            case R.id.ly_canel:
                SharedPreferences sharedPreferences= getActivity().getSharedPreferences("userInfo",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent3 = new Intent(view.getContext(), LoginAndRegisterActivity.class);
                startActivity(intent3);
                getActivity().finish();
                break;
            case R.id.ly_fixPsd:
                Intent intent4 = new Intent(getContext(), FixInfoActivity.class);
                intent4.putExtra("uId",uid);
                startActivity(intent4);
                getActivity().finish();
                break;
        }
    }
    //去修改
    private void uploadToDataBase(String userId) {
        okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .build();
        String url = "http://" + getResources().getString(R.string.ipConfig) + ":8080/WorkProject/ylx/findUser";
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

    //存储图片
    private void uploadToDataBase(String uri,String userId) {
        okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("uri", uri)
                .add("userId", userId)
                .build();
        String url = "http://" + getResources().getString(R.string.ipConfig) + ":8080/WorkProject/ylx/fixHead";
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
    private Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
                if (msg.what == 2) {
                    if (msg.obj.equals("不存在用户")) {
                        Toast.makeText(getActivity(), "不存在用户",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        //进入主activity
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
                        User user = gson.fromJson(msg.obj.toString(), new TypeToken<User>() {
                        }.getType());
                        user1 = user;
                        name.setText("用户名："+user1.getUser_name());
                        phone.setText("手机号："+user1.getPhone());
                        if(user1.getHead_protrait() == null){
                            //没有头像默认
                            int icon = getResources().getIdentifier("ptylx", "mipmap",getActivity().getPackageName());
                            // 设置图片
                            circleImageView.setImageResource(icon);
                        }else {
                            circleImageView.setImageURI(Uri.parse(user1.getHead_protrait()));
                        }
                    }
                }else if(msg.what == 1){
                    if(msg.obj.equals("修改失败，请重试")){
                        Toast.makeText(getActivity(),"请重试",Toast.LENGTH_SHORT).show();
                    }else {
                        circleImageView.setImageURI(Uri.parse((String) msg.obj));
                    }
                }
        }
    };
}
