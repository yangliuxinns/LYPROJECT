package org.turings.investigationapplicqation.Util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import org.turings.investigationapplicqation.DialogAdapter.CustomAddQuestionAdapter;
import org.turings.investigationapplicqation.Entity.TopicBigType;
import org.turings.investigationapplicqation.Entity.TopicType;
import org.turings.investigationapplicqation.R;

import java.util.ArrayList;
import java.util.List;
//选择题型
public class AddProjectPopupWindow extends PopupWindow {
    private Context context;
    private View view;
    private ListView listView;
    private List<TopicBigType> topicBigTypes = new ArrayList<>();
    private List<TopicType> topicTypes1 = new ArrayList<>();
    private List<TopicType> topicTypes2 = new ArrayList<>();
    private List<TopicType> topicTypes3 = new ArrayList<>();
    private ImageView back;
    public AddProjectPopupWindow(Context mContext, View.OnClickListener itemsOnClick) {

        this.view = LayoutInflater.from(mContext).inflate(R.layout.popup_add_question, null);

        //设置数据
        listView = view.findViewById(R.id.lv_all);
        back = view.findViewById(R.id.back);
        //数据源
        TopicType topicType1 = new TopicType("单选题","choosesolo");
        TopicType topicType2 = new TopicType("多选题","choosemore");
        TopicType topicType3 = new TopicType("填空题","writeblank");
        TopicType topicType4 = new TopicType("分页","peddleg");
        TopicType topicType5 = new TopicType("姓名","name");
        TopicType topicType6 = new TopicType("性别","mean");
        TopicType topicType7 = new TopicType("手机","phonenum");
        TopicType topicType8 = new TopicType("日期","calender");
        TopicType topicType9 = new TopicType("地区","citypostion");
        TopicType topicType10 = new TopicType("地图","position2");
        TopicType topicType11 = new TopicType("题库选题","questionlibrary");
        topicTypes1.add(topicType1);
        topicTypes1.add(topicType2);
        topicTypes1.add(topicType3);
        topicTypes1.add(topicType4);
        topicTypes2.add(topicType5);
        topicTypes2.add(topicType6);
        topicTypes2.add(topicType7);
        topicTypes2.add(topicType8);
        topicTypes2.add(topicType9);
        topicTypes2.add(topicType10);
        topicTypes3.add(topicType11);
        TopicBigType topicBigType1 = new TopicBigType("添加基础题型",topicTypes1);
        TopicBigType topicBigType2 = new TopicBigType("添加题目模板",topicTypes2);
        TopicBigType topicBigType3 = new TopicBigType("题库加题",topicTypes3);
        topicBigTypes.add(topicBigType1);
        topicBigTypes.add(topicBigType2);
        topicBigTypes.add(topicBigType3);
        CustomAddQuestionAdapter customAddQuestionAdapter = new CustomAddQuestionAdapter(topicBigTypes,mContext,R.layout.tg_popup_item);
        listView.setAdapter(customAddQuestionAdapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop_layout).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });


        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        WindowManager WM = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        WM.getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        this.setHeight(height);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xffffff);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.take_photo_anim);

    }
}
