package org.turings.investigationapplicqation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loper7.date_time_picker.dialog.CardDatePickerDialog;

import org.turings.investigationapplicqation.Entity.Questionnaire;
import org.turings.investigationapplicqation.Util.CustomDatePicker;
import org.turings.investigationapplicqation.Util.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QuestionnaireSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private Switch aSwitch1;//是否每部手机只能回答一次
    private Switch aSwitch2;//是否只能微信回答
    private Switch aSwitch3;//是否记录微信性别城市昵称
    private LinearLayout ly_s_time;//开始时间
    private LinearLayout ly_e_time;//结束时间
    private TextView tv_s_time;//结束时间填写
    private TextView tv_e_time;//结束时间填写
    private ImageView start;//开始go
    private ImageView end;//结束go
    private TextView tv_s_time2;//结束时间填写
    private TextView tv_e_time2;//结束时间填写
    private ImageView start2;//开始go
    private ImageView end2;//结束go
    private LinearLayout ly_s_time2;//开始时间
    private LinearLayout ly_e_time2;//结束时间
    private Button btn;//登录
    private Questionnaire questionnaire;//问卷
    private Questionnaire questionnaire2 ;//备选
    private CustomDatePicker mTimerPicker,mTimerPicker2;
    private ImageView back;//返回
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire_settings);
        questionnaire = (Questionnaire) getIntent().getSerializableExtra("q_data");
        questionnaire2 = new Questionnaire(questionnaire.getId(),questionnaire.getTitle(),questionnaire.getInstructions(),questionnaire.isRelease(),questionnaire.getList(),questionnaire.getTotalPage(),questionnaire.getOnlyPhone(),questionnaire.getOnlyWeixin(),questionnaire.getRecordWeixinInfo(),questionnaire.getAppearance(),questionnaire.getStartTime(),questionnaire.getEndTime(),false);
        getViews();
        init();
        getRegister();
        aSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //控制开关字体颜色
                if (b) {
                    //选中
                    aSwitch1.setSwitchTextAppearance(QuestionnaireSettingsActivity.this,R.style.s_true);
                    questionnaire.setOnlyPhone(true);
                }else {
                    aSwitch1.setSwitchTextAppearance(QuestionnaireSettingsActivity.this,R.style.s_false);
                    questionnaire.setOnlyPhone(false);
                }
            }
        });
        aSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //控制开关字体颜色
                if (b) {
                    //选中
                    aSwitch2.setSwitchTextAppearance(QuestionnaireSettingsActivity.this,R.style.s_true);
                    questionnaire.setOnlyWeixin(true);
                }else {
                    aSwitch2.setSwitchTextAppearance(QuestionnaireSettingsActivity.this,R.style.s_false);
                    questionnaire.setOnlyWeixin(false);
                }
            }
        });
        aSwitch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //控制开关字体颜色
                if (b) {
                    //选中
                    aSwitch3.setSwitchTextAppearance(QuestionnaireSettingsActivity.this,R.style.s_true);
                    questionnaire.setRecordWeixinInfo(true);
                }else {
                    aSwitch3.setSwitchTextAppearance(QuestionnaireSettingsActivity.this,R.style.s_false);
                    questionnaire.setRecordWeixinInfo(true);
                }
            }
        });
    }

    //绑定事件
    private void getRegister() {
        ly_s_time.setOnClickListener(this);
        ly_e_time.setOnClickListener(this);
        tv_s_time.setOnClickListener(this);
        tv_e_time.setOnClickListener(this);
        tv_s_time2.setOnClickListener(this);
        tv_e_time2.setOnClickListener(this);
        start2.setOnClickListener(this);
        end2.setOnClickListener(this);
        btn.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    //初始化
    private void init() {
        if(questionnaire.getStartTime() == null){//未设定开始时间
            ly_s_time.setVisibility(View.VISIBLE);
            ly_s_time2.setVisibility(View.GONE);
            tv_s_time.setText("开始时间");
        }else {
            ly_s_time2.setVisibility(View.VISIBLE);
            ly_s_time.setVisibility(View.GONE);
            tv_s_time2.setText(dateToString(questionnaire.getStartTime(),"yyyy-MM-dd HH:mm"));
        }
        if(questionnaire.getEndTime() == null) {
            ly_e_time.setVisibility(View.VISIBLE);
            ly_e_time2.setVisibility(View.GONE);
            tv_e_time.setText("结束时间");
        }else {
            ly_e_time2.setVisibility(View.VISIBLE);
            ly_e_time.setVisibility(View.GONE);
            tv_e_time2.setText(dateToString(questionnaire.getEndTime(),"yyyy-MM-dd HH:mm"));
        }
        aSwitch1.setChecked(questionnaire.getOnlyPhone());
        aSwitch2.setChecked(questionnaire.getOnlyWeixin());
        aSwitch3.setChecked(questionnaire.getRecordWeixinInfo());
        initTimerPicker();
        initTimerPicker2();
    }

    private void getViews() {
        aSwitch1 = findViewById(R.id.swh_status1);
        aSwitch2 = findViewById(R.id.swh_status2);
        aSwitch3 = findViewById(R.id.swh_status3);
        ly_s_time = findViewById(R.id.ly_s_time);
        ly_e_time = findViewById(R.id.ly_e_time);
        tv_s_time = findViewById(R.id.s_time);
        tv_e_time = findViewById(R.id.e_time);
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        ly_s_time2 = findViewById(R.id.ly_s_time2);
        ly_e_time2 = findViewById(R.id.ly_e_time2);
        tv_s_time2 = findViewById(R.id.s_time2);
        tv_e_time2 = findViewById(R.id.e_time2);
        start2 = findViewById(R.id.start2);
        end2 = findViewById(R.id.end2);
        btn = findViewById(R.id.btn);
        back = findViewById(R.id.back);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.s_time://还没有开始时间
                //选择时间
                mTimerPicker.show(tv_s_time.getText().toString());
                break;
            case R.id.e_time://设置结束时间
                mTimerPicker2.show(tv_e_time.getText().toString());
                break;
            case R.id.end2:
                ly_e_time.setVisibility(View.VISIBLE);
                ly_e_time2.setVisibility(View.GONE);
                tv_e_time2.setText("");
                questionnaire.setEndTime(null);
                break;
            case R.id.start2:
                ly_s_time.setVisibility(View.VISIBLE);
                ly_s_time2.setVisibility(View.GONE);
                tv_s_time2.setText("");
                questionnaire.setStartTime(null);
                break;
            case R.id.s_time2:
                mTimerPicker.show(tv_s_time2.getText().toString());
                break;
            case R.id.e_time2:
                mTimerPicker2.show(tv_e_time2.getText().toString());
                break;
            case R.id.btn://确认设置
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("q_data",questionnaire);
                //设置返回数据
                setResult(5, intent);//RESULT_OK为自定义常量
                //关闭Activity
                finish();
                break;
            case R.id.back:
                Intent intent1 = new Intent();
                //把返回数据存入Intent
                intent1.putExtra("q_data",questionnaire2);
                //设置返回数据
                setResult(5, intent1);//RESULT_OK为自定义常量
                //关闭Activity
                finish();
                break;
        }
    }

    private void initTimerPicker() {
        String beginTime = "1998-10-17 18:00";
        String endTime = "2100-10-17 18:00";

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                if(questionnaire.getEndTime() == null){
                    ly_s_time2.setVisibility(View.VISIBLE);
                    ly_s_time.setVisibility(View.GONE);
                    tv_s_time2.setText(DateFormatUtils.long2Str(timestamp, true));
                    questionnaire.setStartTime(new Date(timestamp));
                }else {
                    if(questionnaire.getEndTime().before(new Date(timestamp))){
                        //不能设置
                        Toast.makeText(getApplicationContext(),"开始事件不能晚于结束时间",Toast.LENGTH_SHORT).show();
                    }else {
                        Log.i("www", "onTimeSelected: 走着了");
                        ly_s_time2.setVisibility(View.VISIBLE);
                        ly_s_time.setVisibility(View.GONE);
                        tv_s_time2.setText(DateFormatUtils.long2Str(timestamp, true));
                        questionnaire.setStartTime(new Date(timestamp));
                    }
                }
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示时和分
        mTimerPicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }
    private void initTimerPicker2() {
        String beginTime = "1998-10-17 18:00";
        String endTime = "2100-10-17 18:00";

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker2 = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                if(questionnaire.getStartTime() == null){
                    Log.i("www", "onTimeSelected: 走着了2"+questionnaire.getStartTime());
                    ly_e_time2.setVisibility(View.VISIBLE);
                    ly_e_time.setVisibility(View.GONE);
                    tv_e_time2.setText(DateFormatUtils.long2Str(timestamp, true));
                    questionnaire.setEndTime(new Date(timestamp));
                }else {
                    if(questionnaire.getStartTime().after(new Date(timestamp))){
                        //不能设置
                        Toast.makeText(getApplicationContext(),"开始事件不能晚于结束时间",Toast.LENGTH_SHORT).show();
                    }else {
                        Log.i("www", "onTimeSelected: 走着了2");
                        ly_e_time2.setVisibility(View.VISIBLE);
                        ly_e_time.setVisibility(View.GONE);
                        tv_e_time2.setText(DateFormatUtils.long2Str(timestamp, true));
                        questionnaire.setEndTime(new Date(timestamp));
                    }
                }
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker2.setCancelable(true);
        // 显示时和分
        mTimerPicker2.setCanShowPreciseTime(true);
        // 允许循环滚动
        mTimerPicker2.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker2.setCanShowAnim(true);
    }
    public Date stringToDate(String strTime, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }
}
