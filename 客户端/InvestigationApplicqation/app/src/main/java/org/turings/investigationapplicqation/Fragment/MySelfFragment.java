package org.turings.investigationapplicqation.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;

import org.turings.investigationapplicqation.CircleImageView;
import org.turings.investigationapplicqation.Entity.User;
import org.turings.investigationapplicqation.FixPAndMActivity;
import org.turings.investigationapplicqation.MainActivity;
import org.turings.investigationapplicqation.R;
import org.turings.investigationapplicqation.RubbishActivity;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
    private User user;
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
        user = new User(1,"haha","a","123","111");
    }

    //注册
    private void getResiter() {
        ly_project.setOnClickListener(this);
        circleImageView.setOnClickListener(this);
        info.setOnClickListener(this);
        ly_rubbish.setOnClickListener(this);
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
                circleImageView.setImageURI(imageUri);
            }

            // 自定义裁剪配置
            @Override public void cropConfig(CropImage.ActivityBuilder builder) {
                builder
                        // 是否启动多点触摸
                        .setMultiTouchEnabled(false)
                        // 设置网格显示模式
                        .setGuidelines(CropImageView.Guidelines.OFF)
                        // 圆形/矩形
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        // 调整裁剪后的图片最终大小
                        .setRequestedSize(960, 540)
                        // 宽高比
                        .setAspectRatio(16, 9);
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
                intent1.putExtra("user", (Serializable) user);
                startActivity(intent1);
                getActivity().finish();
                break;
            case R.id.ly_project://我的项目
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.setAction("work");
                startActivity(intent);
                break;
            case R.id.imgView:
                //调用裁剪
                startChooser();
                break;
            case R.id.ly_rubbish:
                Intent intent2 = new Intent(view.getContext(), RubbishActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
