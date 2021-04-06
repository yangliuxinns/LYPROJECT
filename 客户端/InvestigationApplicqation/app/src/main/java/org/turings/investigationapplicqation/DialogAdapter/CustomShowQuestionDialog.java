package org.turings.investigationapplicqation.DialogAdapter;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.turings.investigationapplicqation.Entity.Question;
import org.turings.investigationapplicqation.R;
import org.turings.investigationapplicqation.Util.CustomListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CustomShowQuestionDialog extends DialogFragment {
    private Question questionnaire;
    private TextView textView;
    private CustomListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_dialog_question_show, container, false);//布局，父视图，是否立刻加载
        textView = view.findViewById(R.id.title);
        listView = view.findViewById(R.id.list);
        if(questionnaire.getType().equals("单选题") || questionnaire.getType().equals("多选题") || questionnaire.getType().equals("性别")){
            //初始化listView
            CustomQuestionContentAdapter customQuestionContentAdapter = new CustomQuestionContentAdapter(questionnaire.getOptions(),getContext(),R.layout.item_question_option);
            listView.setAdapter(customQuestionContentAdapter);
        }
        textView.setText(questionnaire.getTitle());
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

    public void setMsgData(Question subjectMsg) {
        questionnaire = subjectMsg;
    }

}
