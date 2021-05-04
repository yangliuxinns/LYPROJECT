package org.turings.investigationapplicqation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.turings.investigationapplicqation.DialogAdapter.CustomDrafDialog;
import org.turings.investigationapplicqation.DialogAdapter.CustomPublishDialog;
import org.turings.investigationapplicqation.DialogAdapter.CustomSearchResultListViewAdapter;
import org.turings.investigationapplicqation.Entity.Question;
import org.turings.investigationapplicqation.Entity.Questionnaire;
import org.turings.investigationapplicqation.Util.CustomListView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.turings.investigationapplicqation.Util.RecyclerViewAnimation.runLayoutAnimation;

//搜索结果
public class SearchResultActivity extends AppCompatActivity {

    private ListView listView;
    private TextView num;
    private CustomSearchResultListViewAdapter customSearchResultListViewAdapter;
    private OkHttpClient okHttpClient;
    private String uId;
    private List<Questionnaire> lists = new ArrayList<>();
    private List<Questionnaire> list;
    private LinearLayout ly;
    private LinearLayout search;
    private ImageView back;
    private Dialog dialog;
    private View inflate;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1://问卷部署
                    lists.clear();
                    lists.addAll(list);
                    num.setText("符合搜索条件的项目 共"+lists.size()+"个");
                    if(lists.size()>0){
                        customSearchResultListViewAdapter.notifyDataSetChanged();
                        ly.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    }else {
                        ly.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    lists.remove(msg.obj);
                    num.setText("符合搜索条件的项目 共"+lists.size()+"个");
                    if(lists.size()>0){
                        customSearchResultListViewAdapter.notifyDataSetChanged();
                        ly.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    }else {
                        ly.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Intent intent = getIntent();
        String str = (String) intent.getSerializableExtra("search");
        SharedPreferences sharedPreferences= getSharedPreferences("userInfo",MODE_PRIVATE);
        uId=sharedPreferences.getString("uId","");
        num = findViewById(R.id.num);
//        Questionnaire qn1 = new Questionnaire(1,"调查问卷1","问卷说明1lalaalalalalalala",false,null,0,false,false,false,"");
//        Questionnaire qn2 = new Questionnaire(1,"调查问卷2","问卷说明1lalaalalalalalala",true,null,0,false,false,false,"");
//        Questionnaire qn3 = new Questionnaire(1,"调查问卷3","问卷说明1lalaalalalalalala",false,null,0,false,false,false,"");
//        Questionnaire qn4 = new Questionnaire(1,"调查问卷4","问卷说明1lalaalalalalalala",true,null,0,false,false,false,"");
//        Questionnaire qn5 = new Questionnaire(1,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,false,false,false,"");
//        Questionnaire qn6 = new Questionnaire(1,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,false,false,false,"");
//        Questionnaire qn7 = new Questionnaire(1,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,false,false,false,"");
//        Questionnaire qn8 = new Questionnaire(1,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,false,false,false,"");
//        Questionnaire qn9 = new Questionnaire(1,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,false,false,false,"");
//        Questionnaire qn10 = new Questionnaire(1,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,false,false,false,"");
//        List<Questionnaire> list = new ArrayList<>();
//        list.add(qn1);
//        list.add(qn2);
//        list.add(qn3);
//        list.add(qn4);
//        list.add(qn5);
//        list.add(qn6);
//        list.add(qn7);
//        list.add(qn8);
//        list.add(qn9);
//        list.add(qn10);

        listView = findViewById(R.id.list);
        search = findViewById(R.id.search_layout);
        ly = findViewById(R.id.log);
        //搜索数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadToDataBase(str);
            }
        }).start();
        customSearchResultListViewAdapter = new CustomSearchResultListViewAdapter(lists,getApplicationContext(),R.layout.search_result_questionare_item_layout);
        listView.setAdapter(customSearchResultListViewAdapter);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        back = findViewById(R.id.search_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                show(lists.get(position).isRelease(),position);
            }
        });
    }
    //搜索所有题目
    private void uploadToDataBase(String str) {
        okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("uId", uId)
                .add("str",str)
                .build();
        String url = "http://" + getResources().getString(R.string.ipConfig) + ":8080/WorkProject/ylx/findQsByTitle";
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
                        Type type = new TypeToken<List<Questionnaire>>() {
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

    //自定义图片选择dialog
    public void show(boolean release,int position){
        dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.custom_search_list_dialog, null);
        //初始化控件
        //编辑
        TextView edit = inflate.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否发布
                if(release){
                    //已经发布
                    showCustomDialog(lists.get(position));
                }else {
                    Intent intent = new Intent(getApplicationContext(), EditQuestionnaire.class);
                    List<Question> qs = new ArrayList<>();
                    for(Question qu:list.get(position).getList()){
                        if(qu.getOptions() != null) {
                            for (int i = 0; i < qu.getOptions().size(); i++) {
                                if (!qu.getOptions().get(i).getImg().equals("sr") || qu.getOptions().get(i).getImg().equals("") || qu.getOptions().get(i).getImg().isEmpty()) {
                                    qu.getOptions().get(i).setImgcontent(null);
                                }
                            }
                            qs.add(qu);
                        }
                    }
                    list.get(position).setList(qs);
                    //查看是否分页
                    Questionnaire questionnaire = list.get(position);
                    if(questionnaire.getTotalPage() != 0){
                        if(questionnaire.getTotalPage() == 2){
                            Question question1 = new Question(0,0,"","分页",null,false,1);
                            questionnaire.getList().add(0,question1);
                            //尾部
                            int n = 0;
                            for(int k=0;k<questionnaire.getList().size();k++){
                                if(questionnaire.getList().get(k).getPageNumber() == 2){
                                    n++;
                                    Question question2 = new Question(0,0,"","分页",null,false,2);
                                    questionnaire.getList().add(k,question2);
                                    break;
                                }
                            }
                            if(n==0){
                                Question question2 = new Question(0,0,"","分页",null,false,2);
                                questionnaire.getList().add(question2);
                            }
                        }else {
                            //不止一页
                            for(int i = 0;i<questionnaire.getTotalPage();i++){
                                if(i==0){
                                    Question question1 = new Question(0,0,"","分页",null,false,1);
                                    questionnaire.getList().add(0,question1);
                                }else {
                                    //尾部
                                    int n = 0;
                                    for(int k=0;k<questionnaire.getList().size();k++){
                                        if(questionnaire.getList().get(k).getPageNumber() == (i+1)){
                                            n++;
                                            Question question2 = new Question(0,0,"","分页",null,false,i+1);
                                            questionnaire.getList().add(k,question2);
                                            break;
                                        }
                                    }
                                    if(n==0){
                                        Question question2 = new Question(0,0,"","分页",null,false,i+1);
                                        questionnaire.getList().add(question2);
                                    }
                                }
                            }
                        }

                    }
                    intent.putExtra("questionnaire_data", questionnaire);
                    startActivity(intent);
                }
            }
        });
        //发布
        TextView publish = inflate.findViewById(R.id.publish);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否发布
                if(release){
                    //已经发布
                    Toast.makeText(getApplicationContext(),"该问卷已经发布",Toast.LENGTH_SHORT).show();
                }else {
                    list.get(position).setRelease(true);
                    lists.clear();
                    customSearchResultListViewAdapter.notifyDataSetChanged();
                    lists.addAll(list);
                    customSearchResultListViewAdapter.notifyDataSetChanged();
                    Intent inten = new Intent(getApplicationContext(), ReleaseActivity.class);
                    inten.putExtra("url","http://192.168.10.223:8080/WorkProject/ylx/preInvestigation/"+lists.get(position).getId());
                    inten.putExtra("uId",list.get(position).getId()+"");
                    startActivity(inten);
                }
            }
        });
        //删除
        TextView del = inflate.findViewById(R.id.delete);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
                list.get(position).setDel(true);
                lists.clear();
                customSearchResultListViewAdapter.notifyDataSetChanged();
                lists.addAll(list);
                customSearchResultListViewAdapter.notifyDataSetChanged();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        deleteToDataBase(lists.get(position),position);
                    }
                }).start();
            }
        });
        //预览
        TextView pre = inflate.findViewById(R.id.preview);
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getApplicationContext(), PreViewActivity.class);
                intent3.putExtra("q_data", lists.get(position));
                startActivity(intent3);
            }
        });
        //分享
        TextView share = inflate.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先发布再分享
                //判断是否发布
                if(release){
                    //已经发布
                    Intent inten = new Intent(getApplicationContext(), ReleaseActivity.class);
                    inten.putExtra("url","http://192.168.10.223:8080/WorkProject/ylx/preInvestigation/"+list.get(position).getId());
                    inten.putExtra("uId",list.get(position).getId()+"");
                    startActivity(inten);
                }else {
                    //请先发布才能分享
                    //问卷还没有发布是否发布
                    showPublishCustomDialog(lists.get(position));
                }
            }
        });
        //统计
        TextView count = inflate.findViewById(R.id.count);
        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(release){
                    Intent intent = new Intent(getApplicationContext(), StatisticalResultsActivity.class);
                    intent.putExtra("id", list.get(position));
                    startActivity(intent);
                }else {
                    //已经发布
                    Toast.makeText(getApplicationContext(),"问卷未发布，暂无数据",Toast.LENGTH_SHORT).show();
                }
            }
        });
//        choosePhoto = (TextView) inflate.findViewById(R.id.choosePhoto);
//        takePhoto = (TextView) inflate.findViewById(R.id.takePhoto);
//        canel = inflate.findViewById(R.id.canel);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.y = 0;//设置Dialog距离底部的距离
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
//        choosePhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //从相册选择
//                choosePhoto();
//
//            }
//        });
//        takePhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //点击拍照
//                takePhoto();
//            }
//        });
        TextView cancel = inflate.findViewById(R.id.canel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void showCustomDialog(Questionnaire questionnaire) {
        //管理多个Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        //事务（一系列原子性操作）
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        CustomDrafDialog customDialog = new CustomDrafDialog();
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

    //发布问卷
    private void showPublishCustomDialog(Questionnaire questionnaire) {
        //管理多个Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        //事务（一系列原子性操作）
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        CustomPublishDialog customDialog = new CustomPublishDialog();
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
    private void deleteToDataBase(Questionnaire questionnaire, int position) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
        String subject = gson.toJson(questionnaire);
        okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"),subject);
        String url = "http://" + getResources().getString(R.string.ipConfig) + ":8080/WorkProject/ylx/updateQuestionaresDelete";
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
                        Message msg = Message.obtain();
                        msg.obj = position;
                        msg.what=2;
                        handler.sendMessage(msg);
                    }
                });
            }
        }).start();
    }
}
