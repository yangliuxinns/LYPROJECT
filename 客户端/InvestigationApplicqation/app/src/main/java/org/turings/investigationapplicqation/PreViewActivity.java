package org.turings.investigationapplicqation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.turings.investigationapplicqation.DialogAdapter.CustomShowTopicAdapter;
import org.turings.investigationapplicqation.Entity.PeddleData;
import org.turings.investigationapplicqation.Entity.Question;
import org.turings.investigationapplicqation.Entity.QuestionResult;
import org.turings.investigationapplicqation.Entity.Questionnaire;
import org.turings.investigationapplicqation.Util.CustomListView;

import java.util.ArrayList;
import java.util.List;

public class PreViewActivity extends AppCompatActivity implements View.OnClickListener {

    private Questionnaire questionnaire;//要显示的问卷
    private ImageView back;//关闭预览
    private TextView tv_title;//问卷名
    private TextView tv_info;//问卷描述
    private CustomListView listView;//题目列表
    private Button buttonT;//上一题
    private Button buttonN;//下一题
    private Button buttonC;//提交
    private int currrentPage;//当前页数
    private List<PeddleData> peddleDatas;//分页数据
    private CustomShowTopicAdapter customShowTopicAdapter;
    private List<Question> questions= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_view);
        //接收数据
        questionnaire = (Questionnaire) getIntent().getSerializableExtra("q_data");
        getViews();
        getRegister();
        init();
    }

    //初始化处理数据
    private void init() {
        tv_title.setText(questionnaire.getTitle());
        tv_info.setText(questionnaire.getInstructions());
        //处理数据
        //不分页
        if(questionnaire.getTotalPage() == 0){
            questions.addAll(questionnaire.getList());
            customShowTopicAdapter = new CustomShowTopicAdapter(questions,getApplicationContext(),R.layout.topic_item);
            listView.setAdapter(customShowTopicAdapter);
            buttonT.setVisibility(View.GONE);
            buttonN.setVisibility(View.GONE);
            buttonC.setVisibility(View.VISIBLE);
        }else {//分页
            peddleDatas = new ArrayList<>();
            for(int i=1;i<=questionnaire.getTotalPage();i++){
                PeddleData peddleData = new PeddleData();
                List<Question> questions = new ArrayList<>();
                List<QuestionResult> result = new ArrayList<>();
                for(int k=0;k<questionnaire.getList().size();k++){
                    if(questionnaire.getList().get(k).getPageNumber() == i){
                        questions.add(questionnaire.getList().get(k));
                    }
                }
                peddleData.setQuestions(questions);
                peddleData.setCurrent(i);
                peddleData.setSize(questions.size());
                peddleData.setResult(result);
                peddleDatas.add(peddleData);
            }
            //初始化页码
            currrentPage = 1;
            //设置显示
            buttonT.setVisibility(View.GONE);
            buttonC.setVisibility(View.GONE);
            //展示题目
            questions.addAll(peddleDatas.get(0).getQuestions());
            customShowTopicAdapter = new CustomShowTopicAdapter(questions,getApplicationContext(),R.layout.topic_item);
            listView.setAdapter(customShowTopicAdapter);
        }
    }
    //注册
    private void getRegister() {
        back.setOnClickListener(this);
        buttonT.setOnClickListener(this);
        buttonN.setOnClickListener(this);
        buttonC.setOnClickListener(this);
    }
    //获得控件
    private void getViews() {
        back = findViewById(R.id.back);
        tv_title = findViewById(R.id.txt_title);
        listView = findViewById(R.id.lv_list);
        buttonT = findViewById(R.id.btn_login);
        buttonN = findViewById(R.id.btn_next);
        buttonC = findViewById(R.id.btn_commit);
        tv_info = findViewById(R.id.txt_info);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_login://上一页
                for(int i=0;i<peddleDatas.size();i++){
                    if(peddleDatas.get(i).getCurrent() == currrentPage-1){
                        questions.addAll(peddleDatas.get(i).getQuestions());
                        customShowTopicAdapter.notifyDataSetChanged();
                        listView.setAdapter(customShowTopicAdapter);
                    }
                }
                currrentPage--;
                if(currrentPage == 1){
                    tv_title.setVisibility(View.VISIBLE);
                    buttonT.setVisibility(View.GONE);
                    buttonC.setVisibility(View.GONE);
                }else if(currrentPage == peddleDatas.size()){
                    tv_title.setVisibility(View.GONE);
                    buttonT.setVisibility(View.VISIBLE);
                    buttonC.setVisibility(View.VISIBLE);
                }else {
                    tv_title.setVisibility(View.GONE);
                    buttonT.setVisibility(View.VISIBLE);
                    buttonN.setVisibility(View.VISIBLE);
                    buttonC.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_next://下一页
                List<QuestionResult> result = customShowTopicAdapter.getQuestionResult();
                List<String> list = new ArrayList<>();
                Boolean flag = true;
                //首先判断是否有未达题目
                for(int q=0;q<questions.size();q++){
                    if(questions.get(q).getRequired()){
                        for(int l = 0;l<result.size();l++){
                            if(result.get(l).getOrder() == questions.get(q).getOrder()){
                                flag = true;
                            }else {
                                flag = false;
                            }
                        }
                        if(!flag){
                            //此题未解答
                            list.add("第"+questions.get(q).getOrder()+"题");
                        }
                    }
                }
                if(list.size() != 0){
                    Toast.makeText(getApplicationContext(),"以下题目未作答:"+list.toString(),Toast.LENGTH_SHORT).show();
                }else {
                    for(int i=0;i<peddleDatas.size();i++){
                        if(peddleDatas.get(i).getCurrent() == currrentPage+1){
                            questions.addAll(peddleDatas.get(i).getQuestions());
                            customShowTopicAdapter.notifyDataSetChanged();
                            listView.setAdapter(customShowTopicAdapter);
                        }
                    }
                    currrentPage++;
                    if(currrentPage == 1){
                        tv_title.setVisibility(View.VISIBLE);
                        buttonT.setVisibility(View.GONE);
                        buttonC.setVisibility(View.GONE);
                    }else if(currrentPage == peddleDatas.size()){
                        tv_title.setVisibility(View.GONE);
                        buttonT.setVisibility(View.VISIBLE);
                        buttonC.setVisibility(View.VISIBLE);
                    }else {
                        tv_title.setVisibility(View.GONE);
                        buttonT.setVisibility(View.VISIBLE);
                        buttonN.setVisibility(View.VISIBLE);
                        buttonC.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.btn_commit://提交
                if(questionnaire.getTotalPage()==0){
                    //判断题目是否回答完了
                }else {

                    Toast.makeText(getApplicationContext(),"此为浏览界面，不可提交",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}
