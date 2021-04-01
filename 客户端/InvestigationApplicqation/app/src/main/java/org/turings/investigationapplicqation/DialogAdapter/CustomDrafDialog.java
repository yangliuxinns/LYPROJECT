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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.turings.investigationapplicqation.EditQuestionnaire;
import org.turings.investigationapplicqation.Entity.Questionnaire;
import org.turings.investigationapplicqation.MainActivity;
import org.turings.investigationapplicqation.R;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CustomDrafDialog extends DialogFragment {
    private OkHttpClient okHttpClient;
    private Response response;//响应
    private Questionnaire questionnaire;//问卷

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_dialog_draf_layout_ylx, container, false);//布局，父视图，是否立刻加载
        Button btnOk = view.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadToDataBase();
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

    //改变问卷状态
    private void uploadToDataBase() {
        okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("uId", String.valueOf(questionnaire.getId()))
                .build();
        String url = "http://" + getResources().getString(R.string.ipConfig) + ":8080/WorkProject/ylx/fixQuestionaresDraf";
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
                        if(str.equals("更改成功")){
                            //去往
                            questionnaire.setRelease(false);
                            getDialog().dismiss();//关闭当前的对话框
                            Intent intent = new Intent(getActivity(), EditQuestionnaire.class);
                            intent.putExtra("questionnaire_data", questionnaire);
                            startActivity(intent);
                        }else {
                            //失败提示
                            getDialog().dismiss();//关闭当前的对话框
                            Toast.makeText(getContext(),"操作失败，请重试",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }
}
