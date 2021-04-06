package org.turings.investigationapplicqation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.turings.investigationapplicqation.DialogAdapter.AddQuestionDialog;
import org.turings.investigationapplicqation.DialogAdapter.CustomDialogYLX;
import org.turings.investigationapplicqation.DialogAdapter.CustomShowQuestionDialog;
import org.turings.investigationapplicqation.DialogAdapter.QuestionBankAdapter;
import org.turings.investigationapplicqation.DialogAdapter.RubbishAdapter;
import org.turings.investigationapplicqation.Entity.Question;
import org.turings.investigationapplicqation.Entity.Questionnaire;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.turings.investigationapplicqation.Util.RecyclerViewAnimation.runLayoutAnimation;

//题库
public class QuestionBankActivity extends AppCompatActivity {

    @BindView(R.id.rv_normal_show)
    LinearLayout rvNormalShow;//没有数据时展示的布局
    @BindView(R.id.rv)
    RecyclerView rv;//列表
    @BindView(R.id.tv_check_all)
    TextView tvCheckAll;//全选
    @BindView(R.id.tv_delete)
    TextView tvDelete;//选中
    @BindView(R.id.lay_bottom)
    LinearLayout layBottom;//底部布局
    @BindView(R.id.search_back)
    ImageView back;
    @BindView(R.id.find)
    TextView find;//前往
    @BindView(R.id.content)
    EditText findContent;//前往

    private static final int STATE_DEFAULT = 0;//默认状态
    private static final int STATE_EDIT = 1;//编辑状态
    private int mEditMode = 1;
    private boolean editorStatus = false;//是否为编辑状态
    private int index = 0;//当前选中的item数

    private List<Question> resultsBeans = new ArrayList<>();
    private List<Question> list;
    private Boolean flag = false;
    List<Question> mList = new ArrayList<>();//列表
    QuestionBankAdapter mAdapter;//适配器
    private OkHttpClient okHttpClient;
    private String uId;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1://布置所有题目
                    resultsBeans.clear();
                    for (Question question : list) {
                        question.setRequired(false);
                        resultsBeans.add(question);
                    }
                    if (resultsBeans.size() > 0) {
                        mList.clear();
                        mList.addAll(resultsBeans);
                        mAdapter.notifyDataSetChanged();//刷新数据
                        runLayoutAnimation(rv);//动画显示
                        rv.setVisibility(View.VISIBLE);
                        rvNormalShow.setVisibility(View.GONE);
                    } else {
                        rv.setVisibility(View.GONE);
                        rvNormalShow.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_bank);
        ButterKnife.bind(this);
        rv.setVisibility(View.VISIBLE);
        rvNormalShow.setVisibility(View.GONE);
        initList();
    }
    //初始化列表数据
    private void initList() {
        //数据是后台读出来的
        mAdapter = new QuestionBankAdapter(R.layout.question_bank_item, mList);//绑定视图和数据
        rv.setLayoutManager(new LinearLayoutManager(this));//设置线性布局管理器
        rv.setAdapter(mAdapter);//设置适配器

        //假数据
//        Questionnaire qn1 = new Questionnaire(1,"调查问卷1","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
//        Questionnaire qn2 = new Questionnaire(2,"调查问卷2","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
//        Questionnaire qn3 = new Questionnaire(3,"调查问卷3","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
//        Questionnaire qn4 = new Questionnaire(4,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
//        Questionnaire qn5 = new Questionnaire(5,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
//        Questionnaire qn6 = new Questionnaire(6,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
//        Questionnaire qn7 = new Questionnaire(7,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
//        Questionnaire qn8 = new Questionnaire(8,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
//        Questionnaire qn9 = new Questionnaire(9,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
//        Questionnaire qn10 = new Questionnaire(10,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
//
//
//        List<Questionnaire> resultsBeans = new ArrayList<>();
//        resultsBeans.add(qn1);
//        resultsBeans.add(qn2);
//        resultsBeans.add(qn3);
//        resultsBeans.add(qn4);
//        resultsBeans.add(qn5);
//        resultsBeans.add(qn6);
//        resultsBeans.add(qn7);
//        resultsBeans.add(qn8);
//        resultsBeans.add(qn9);
//        resultsBeans.add(qn10);
        //获取用户的id
        SharedPreferences sp = getSharedPreferences("userInfo",MODE_PRIVATE);
        uId = String.valueOf(1);
        mAdapter.setEditMode(mEditMode);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Question dataBean = mList.get(position);
                boolean isSelect = dataBean.isSelect();
                if (!isSelect) {
                    index++;
                    dataBean.setSelect(true);

                } else {
                    dataBean.setSelect(false);
                    index--;
                }
                if (index == 0) {
                    tvDelete.setText("选中");
                } else {
                    tvDelete.setText("选中(" + index + ")");
                }

                mAdapter.notifyDataSetChanged();
            }
        });
        //长按
        mAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                showCustomDialog(mList.get(position));
                return false;
            }
        });
        //搜索数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadToDataBase();
            }
        }).start();
    }
    //页面的点击事件
    @OnClick({R.id.tv_check_all, R.id.tv_delete,R.id.search_back,R.id.find})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_check_all://全选
                //判断是不是第一次选
                if(flag){
                    setAllItemUnchecked();
                    flag = false;
                }else {
                    setAllItemChecked();
                    flag = true;
                }
                break;
            case R.id.tv_delete://删除
                deleteCheckItem();
                break;
            case R.id.search_back:
                finish();
                break;
            case R.id.find://前往查找
                String str = findContent.getText().toString().trim();
                if(str.equals("") || str == null){//空查找所有题
                    //搜索数据
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            uploadToDataBase();
                        }
                    }).start();
                }else {//条件查找题目
                    //搜索数据
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            findQuestion(str);
                        }
                    }).start();
                }
                break;
        }
    }

    private void showCustomDialog(Question questionnaire) {
        //管理多个Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        //事务（一系列原子性操作）
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        CustomShowQuestionDialog customDialog = new CustomShowQuestionDialog();
        //是否添加过
        if(!customDialog.isAdded()){
            //没添加过添加
            transaction.add(customDialog,"dialog");
        }
        //传入要上传的数据
        customDialog.setMsgData(questionnaire);
        //显示Fragment
        transaction.show(customDialog);
        //提交，只有提交了上面的操作才会生效
        transaction.commit();
    }
    private void showCustomTishiDialog(List<Question> n) {
        //管理多个Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        //事务（一系列原子性操作）
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        AddQuestionDialog customDialog = new AddQuestionDialog();
        //是否添加过
        if(!customDialog.isAdded()){
            //没添加过添加
            transaction.add(customDialog,"dialog");
        }
        //传入要上传的数据
        customDialog.setMsgData(n);
        //显示Fragment
        transaction.show(customDialog);
        //提交，只有提交了上面的操作才会生效
        transaction.commit();
    }
    //全部选中
    private void setAllItemChecked() {
        if (mAdapter == null) return;
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setSelect(true);
        }
        mAdapter.notifyDataSetChanged();
        index = mList.size();
        tvDelete.setText("选中(" + index + ")");
        tvCheckAll.setText("反选");
    }

    //取消全部选中
    private void setAllItemUnchecked() {
        if (mAdapter == null) return;
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setSelect(false);
        }
        mAdapter.notifyDataSetChanged();
        tvDelete.setText("选中");
        tvCheckAll.setText("全选");
        index = 0;
    }



    //搜索选中的item
    private void deleteCheckItem() {
        //提示是否加入题目
        List<Question> listId = new ArrayList<>();
        if (mAdapter == null) return;

        for (int i = mList.size() - 1; i >= 0; i--) {

            if (mList.get(i).isSelect() == true) {
                //去搜索
                listId.add(mList.get(i));
            }
        }
        //询问是否添加
        showCustomTishiDialog(listId);
    }
    //搜索所有题目
    private void uploadToDataBase() {
        okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("uId", uId)
                .build();
        String url = "http://" + getResources().getString(R.string.ipConfig) + ":8080/WorkProject/ylx/findQuestionBank";
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
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
                        Type type = new TypeToken<List<Question>>() {
                        }.getType();
                        //反序列化
                        list = new ArrayList<>();
                        list = gson.fromJson(response.body().string(), type);
                        Message msg = Message.obtain();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                });
            }
        }).start();
    }
    //条件搜索题目
    private void findQuestion(String str) {
        okHttpClient = new OkHttpClient();
        Log.i("vvv", "findQuestion: "+str);
        FormBody formBody = new FormBody.Builder()
                .add("str", str)
                .build();
        String url = "http://" + getResources().getString(R.string.ipConfig) + ":8080/WorkProject/ylx/findQuestionBankByTitle";
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
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
                        Type type = new TypeToken<List<Question>>() {
                        }.getType();
                        //反序列化
                        list = new ArrayList<>();
                        list = gson.fromJson(response.body().string(), type);
                        Message msg = Message.obtain();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                });
            }
        }).start();
    }
}
