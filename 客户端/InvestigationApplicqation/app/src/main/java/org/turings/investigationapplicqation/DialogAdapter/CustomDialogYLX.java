package org.turings.investigationapplicqation.DialogAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.turings.investigationapplicqation.Entity.Questionnaire;
import org.turings.investigationapplicqation.Fragment.DraftsFragment;
import org.turings.investigationapplicqation.MainActivity;
import org.turings.investigationapplicqation.R;


import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
//上传题目
public class CustomDialogYLX extends DialogFragment {
    private OkHttpClient okHttpClient;
    private Response response;//响应
    private Questionnaire questionnaire;//问卷

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_dialog_commit_layout_ylx, container, false);//布局，父视图，是否立刻加载
        Button btnOk = view.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击确定按钮执行的操作
                //关闭activity对象
                //将信息存入数据库，并跳转到添加错题页
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadToDataBase(questionnaire);
                    }
                }).start();
            }
        });
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击取消按钮执行的操作
                getDialog().dismiss();//关闭当前的对话框
                Intent intent = new Intent();
                intent.setClass(getActivity(), MainActivity.class);
                intent.setAction("work");
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        // WindowManager 接口的嵌套类
        WindowManager.LayoutParams windowParams = window.getAttributes();
        //设置弹出框周围的透明度
        windowParams.dimAmount = 0.5f;
        //设置弹出框内容的透明度
        windowParams.alpha = 1f;
        //设置弹出框距离上面的距离
        windowParams.y = 100;
        window.setBackgroundDrawableResource(R.drawable.dialog_stroke_layout_ylx);
        windowParams.width = 900;// 调整该值可以设置对话框显示的宽度
        window.setAttributes(windowParams);
        //DisplayMetrics类 获取手机显示屏的基本信息 包括尺寸、密度、字体缩放等信息
        DisplayMetrics dm = new DisplayMetrics();
        //将屏幕尺寸赋给dm
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        //设置弹出框的高度和宽度
    }

    public void setMsgData(Questionnaire subjectMsg) {
        questionnaire = subjectMsg;
    }

    //访问服务器上传至数据库保存
    private void uploadToDataBase(Questionnaire subjectMsg) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
        String subject = gson.toJson(subjectMsg);
        okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), subject);
        String url = "http://" + getResources().getString(R.string.ipConfig) + ":8080/WorkProject/ylx/saveQuestionares";
//
        Request request = new Request.Builder().post(requestBody).url(url).build();
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
                        int id = Integer.parseInt(str);
                        if (0 != id) {//跳转
                            //点击取消按钮执行的操作
                            getDialog().dismiss();//关闭当前的对话框
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), MainActivity.class);
                            intent.setAction("work");
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
                });
            }
        }).start();
    }
}
