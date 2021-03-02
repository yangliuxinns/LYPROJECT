package org.turings.investigationapplicqation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.turings.investigationapplicqation.DialogAdapter.CustomDialogYLX;
import org.turings.investigationapplicqation.DialogAdapter.CustomQuestionnaireAdapter;
import org.turings.investigationapplicqation.Entity.Options;
import org.turings.investigationapplicqation.Entity.Question;
import org.turings.investigationapplicqation.Entity.Questionnaire;
import org.turings.investigationapplicqation.Util.AddProjectPopupWindow;
import org.turings.investigationapplicqation.Util.CustomListView;
import org.turings.investigationapplicqation.Util.ListViewUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/*
编辑问卷
 */
public class EditQuestionnaire extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;//返回询问是否保存
    private LinearLayout menu;//存/发
    private LinearLayout title;//标题
    private TextView tvTitle;//标题
    private TextView tvContent;//简介
    private CustomListView list;
    private LinearLayout addpt;//添加题目
    private LinearLayout ae;//外观
    private LinearLayout fix;//设置
    private LinearLayout preview;//预览
    private Questionnaire questionnaire;//问卷
    private int order = 0;//记录题号也就是总题目数量
    private OkHttpClient okHttpClient;
    private Response response;//响应
    private ScrollView sc;
    private List<Question> lq = new ArrayList<>();
    private CustomQuestionnaireAdapter customQuestionnaireAdapter;
    private AddProjectPopupWindow addProjectPopupWindow;
    private int total =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_questionnaire);
        getViews();
        init();
        Options options1 = new Options(1, "选项1sijdaojasosdoooooooooooooooooooooooooooooooooooooasassddddddddd", "");
        Options options2 = new Options(1, "选项2", "");
        final List<Options> lo = new ArrayList<>();
        lo.add(options1);
        lo.add(options2);
        Question question1 = new Question(1, 1, "我是题目1", "填空", null, true, 1);
        Question question2 = new Question(1, 2, "我是题目2", "选择", lo, true, 1);
        Question question3 = new Question(1, 3, "我是题目1", "手机号", null, false, 1);
        Question question4 = new Question(1, 4, "我是题目1", "城市", null, true, 1);
        Question question5 = new Question(1, 5, "我是题目1", "位置", null, true, 1);
        Question question6 = new Question(1, 6, "我是题目1", "选时间", null, true, 1);
        Question question7 = new Question(1, 7, "我是题目1", "分页", null, true, 1);
//        lq.add(question1);
//        lq.add(question2);
//        lq.add(question3);
//        lq.add(question4);
//        lq.add(question5);
//        lq.add(question6);
//        lq.add(question7);
        customQuestionnaireAdapter = new CustomQuestionnaireAdapter(lq, getApplicationContext(), R.layout.questions_item);
        list.setAdapter(customQuestionnaireAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!lq.get(i).getType().equals("分页")){

                    Intent intent = new Intent(getApplicationContext(), EditQuestionSettingItemActivity.class);
                    intent.putExtra("que",lq.get(i));
                    intent.putExtra("postion",i);
                    startActivityForResult(intent,1);
                }
            }
        });
        //删除已选题目
        customQuestionnaireAdapter.setmOnItemDeleteClickListener(new CustomQuestionnaireAdapter.onItemDeleteListener() {
            @Override
            public void onDeleteClick(int position) {
                if(lq.get(position).getType().equals("分页")){
                    if(position == 0){
                        Toast.makeText(getApplicationContext(),"第一个分页不能删除",Toast.LENGTH_SHORT).show();
                    }else if(total == 2 && lq.get(position).getPageNumber()==2){
                        lq.remove(position);
                        lq.remove(0);
                        total = 0;
                        questionnaire.setTotalPage(0);
                        questionnaire.setList(lq);
                        customQuestionnaireAdapter.notifyDataSetChanged();
                    }else {
                        total--;
                        questionnaire.setTotalPage(total);
                        for(int i=0;i<lq.size();i++){
                            if(i>position && lq.get(i).getType().equals("分页")){
                                lq.get(i).setPageNumber(lq.get(i).getPageNumber()-1);
                                lq.get(i).setOrder(lq.get(i).getOrder()-1);
                                lq.set(i,lq.get(i));
                            }
                        }
                        lq.remove(position);
                        questionnaire.setList(lq);
                        customQuestionnaireAdapter.notifyDataSetChanged();
                    }
                }else {//删除题目，后面题号改变
                    order--;
                    for(int i=0;i<lq.size();i++){
                        if(i>position){
                           lq.get(i).setOrder(lq.get(i).getOrder()-1);
                            lq.set(i,lq.get(i));
                        }
                    }
                    lq.remove(position);
                    questionnaire.setList(lq);
                    customQuestionnaireAdapter.notifyDataSetChanged();
                }
            }
        });
        //上移题目
        customQuestionnaireAdapter.setmOnItemTopMoveClickListener(new CustomQuestionnaireAdapter.onItemTopMoveListener() {
            @Override
            public void onTopMoveClick(int position) {
                if(position==0){
                    if(lq.get(position).getType().equals("分页")){
                        Toast.makeText(getApplicationContext(),"第一个分页不能移动",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"已经在第一位不能在向上移动了",Toast.LENGTH_SHORT).show();
                    }
                }else if(position == 1){
                        Toast.makeText(getApplicationContext(),"上边是第一个分页不能移动",Toast.LENGTH_SHORT).show();
                } else {
                    if(lq.get(position).getType().equals("分页")){//判断要上移动的是不是分页
                        if(!lq.get(position-1).getType().equals("分页")) {//上边是一道题目
                            Question question = lq.get(position-1);
                            lq.get(position-1).setPageNumber(lq.get(position).getPageNumber());
                            lq.set(position-1,lq.get(position));
                            lq.set(position,question);
                            customQuestionnaireAdapter.notifyDataSetChanged();
                        }else {
                            Question question = lq.get(position-1);
                            int temp = lq.get(position-1).getPageNumber();
                            lq.get(position-1).setPageNumber(lq.get(position).getPageNumber());
                            lq.get(position).setPageNumber(temp);
                            lq.set(position-1,lq.get(position));
                            lq.set(position,question);
                            customQuestionnaireAdapter.notifyDataSetChanged();
                        }
                    }else {//要上移动的是题目
                        if(!lq.get(position-1).getType().equals("分页")){//上边是一道题目
                            Question question = lq.get(position-1);
                            lq.get(position-1).setOrder(lq.get(position-1).getOrder()+1);
                            lq.get(position).setOrder(lq.get(position).getOrder()-1);
                            lq.set(position-1,lq.get(position));
                            lq.set(position,question);
                            customQuestionnaireAdapter.notifyDataSetChanged();
                        }else {
                            Question question = lq.get(position-1);
                            lq.get(position).setPageNumber(lq.get(position-1).getPageNumber()-1);
                            lq.set(position-1,lq.get(position));
                            lq.set(position,question);
                            customQuestionnaireAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
        customQuestionnaireAdapter.setmOnItemDownMoveClickListener(new CustomQuestionnaireAdapter.onItemDownMoveListener() {
            @Override
            public void onDownMoveClick(int position) {
                if(position==(lq.size()-1)){
                    Toast.makeText(getApplicationContext(),"已经在最后不能在向下移动了",Toast.LENGTH_SHORT).show();
                }else if(position== 0){
                    if(lq.get(position).getType().equals("分页")){
                        Toast.makeText(getApplicationContext(),"第一个分页不能移动",Toast.LENGTH_SHORT).show();
                    }else {
                        if(!lq.get(position+1).getType().equals("分页")){//下边是一道题目
                            Question question = lq.get(position+1);
                            lq.get(position+1).setOrder(lq.get(position+1).getOrder()-1);
                            lq.get(position).setOrder(lq.get(position).getOrder()+1);
                            lq.set(position+1,lq.get(position));
                            lq.set(position,question);
                            customQuestionnaireAdapter.notifyDataSetChanged();
                        }else {//下边分页
                            Question question = lq.get(position+1);
                            lq.get(position).setPageNumber(lq.get(position+1).getPageNumber());
                            lq.set(position+1,lq.get(position));
                            lq.set(position,question);
                            customQuestionnaireAdapter.notifyDataSetChanged();
                        }
                    }
                }else {
                    if(lq.get(position).getType().equals("分页")){//判断要下移动的是不是分页
                        Question question = lq.get(position+1);
                        int temp = lq.get(position+1).getPageNumber();
                        lq.get(position+1).setPageNumber(lq.get(position).getPageNumber());
                        lq.get(position).setPageNumber(temp);
                        lq.set(position+1,lq.get(position));
                        lq.set(position,question);
                        customQuestionnaireAdapter.notifyDataSetChanged();
                    }else {//要下移动的是题目
                        if(!lq.get(position+1).getType().equals("分页")){//下边是一道题目
                            Question question = lq.get(position+1);
                            lq.get(position+1).setOrder(lq.get(position+1).getOrder()-1);
                            lq.get(position).setOrder(lq.get(position).getOrder()+1);
                            lq.set(position+1,lq.get(position));
                            lq.set(position,question);
                            customQuestionnaireAdapter.notifyDataSetChanged();
                        }else {//下边分页
                            Question question = lq.get(position+1);
                            lq.get(position).setPageNumber(lq.get(position+1).getPageNumber());
                            lq.set(position+1,lq.get(position));
                            lq.set(position,question);
                            customQuestionnaireAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    //初始化
    private void init() {
        questionnaire = (Questionnaire) getIntent().getSerializableExtra("questionnaire_data");
        tvTitle.setText(questionnaire.getTitle());
        tvContent.setText(questionnaire.getInstructions());
        if(questionnaire.getId() != 0){
            Log.i("www", "init: 获得id"+questionnaire.getId());
            lq.addAll(questionnaire.getList());
        }else {
            questionnaire.setList(lq);
        }
        title.setOnClickListener(this);
        menu.setOnClickListener(this);
        addpt.setOnClickListener(this);
        back.setOnClickListener(this);
        fix.setOnClickListener(this);
        preview.setOnClickListener(this);
        ae.setOnClickListener(this);
    }

    //货的控件
    private void getViews() {
        back = findViewById(R.id.back);
        menu = findViewById(R.id.menu);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        list = findViewById(R.id.lv_list);
        addpt = findViewById(R.id.addpt);
        ae = findViewById(R.id.ae);
        fix = findViewById(R.id.fix);
        preview = findViewById(R.id.preview);
        title = findViewById(R.id.title);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title:
                Intent intent = new Intent(this, EditTitleAndDescription.class);
                intent.putExtra("qn", questionnaire);
                startActivityForResult(intent,3);
                break;
            case R.id.menu:

                break;
            case R.id.addpt:
                showPopFormBottom(view);
                break;
            case R.id.back:
                //弹出是否保存弹窗
                showCustomDialog(questionnaire);
                break;
            case R.id.fix://点击设置
                Intent intent2 = new Intent(this, QuestionnaireSettingsActivity.class);
                intent2.putExtra("q_data", questionnaire);
                startActivityForResult(intent2,5);
                break;
            case R.id.ae://点击保存
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadToDataBase(questionnaire);
                    }
                }).start();
                Intent intent1 = new Intent(this,MainActivity.class);
                intent1.setAction("work");
                startActivity(intent1);
                finish();
                break;
            case R.id.preview://点击预览
                Intent intent3 = new Intent(this, PreViewActivity.class);
                intent3.putExtra("q_data", questionnaire);
                startActivity(intent3);
                break;
        }
    }

    //访问服务器上传至数据库
    private void uploadToDataBase(Questionnaire subjectMsg) {
        Log.i("www", "uploadToDataBase: id是多少"+subjectMsg.getId());
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
        String subject = gson.toJson(subjectMsg);
        okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"),subject);
        String url = "http://"+getResources().getString(R.string.ipConfig)+":8080/WorkProject/ylx/saveQuestionares";
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
                        Log.i("lww", response.body().string());
                    }
                });
            }
        }).start();
    }
    private void showCustomDialog(Questionnaire questionnaire) {
        //管理多个Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        //事务（一系列原子性操作）
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        CustomDialogYLX customDialog = new CustomDialogYLX();
        //是否添加过
        if(!customDialog.isAdded()){
            //没添加过添加
            transaction.add(customDialog,"dialog");
        }
        //传入要上传的数据
//        customDialog.subjectMsgData(subjectMsg);
        //显示Fragment
        transaction.show(customDialog);
        //提交，只有提交了上面的操作才会生效
        transaction.commit();
    }
    //弹出添加题目
    public void showPopFormBottom(View view) {
        addProjectPopupWindow = new AddProjectPopupWindow(this, this);
        //showAtLocation(View parent, int gravity, int x, int y)
        addProjectPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode){
            case 1://添加题目
                //EditTopicSettings
                Question question = (Question) data.getSerializableExtra("result_question");
                if(question.getType().equals("分页")){
                    if(questionnaire.getTotalPage() <2){
                        //头尾加分页
                        //加首部
                        total=total+1;
                        Question question1 = new Question(question.getId(), question.getOrder(), question.getTitle(), question.getType(), question.getOptions(), question.getRequired(), total);
                        lq.add(0,question1);
                        //尾部
                        total=total+1;
                        Question question2 = new Question(question.getId(), question.getOrder(), question.getTitle(), question.getType(), question.getOptions(), question.getRequired(), total);
                        lq.add(question2);
                        //更新题目的分页
                        for(int i = 0;i<lq.size();i++){
                            if(i != 0 && i != lq.size()-1){
                                Question q = lq.get(i);
                                q.setPageNumber(1);
                                lq.set(i,q);
                            }
                        }
                    }else if(questionnaire.getTotalPage() >= 2){
                        //尾部加一个分页
                        total++;
                        question.setPageNumber(total);
                        question.setOrder(order);
                        lq.add(question);
                    }
                    questionnaire.setTotalPage(total);
                }else {
                    order++;
                    question.setPageNumber(total);
                    question.setOrder(order);
                    lq.add(question);
                }
                questionnaire.setList(lq);
                customQuestionnaireAdapter.notifyDataSetChanged();
                list.setAdapter(customQuestionnaireAdapter);
                addProjectPopupWindow.dismiss();
                break;
            case 2:
                //来自按钮2的请求，作相应业务处理
                break;
            case 3://改动试卷标题和说明
                questionnaire = (Questionnaire) data.getSerializableExtra("q_data");
                tvTitle.setText(questionnaire.getTitle());
                tvContent.setText(questionnaire.getInstructions());
                break;
            case 4:
                Question qu = (Question) data.getSerializableExtra("result_question");
                int position = data.getIntExtra("postion",0);
                lq.set(position,qu);
                customQuestionnaireAdapter.notifyDataSetChanged();
                list.setAdapter(customQuestionnaireAdapter);
                break;
            case 5:
                questionnaire = (Questionnaire) data.getSerializableExtra("q_data");
                break;
        }
    }
}
