package org.turings.investigationapplicqation;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

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
                show();
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
    public void show(){
        dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.custom_search_list_dialog, null);
        //初始化控件
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
//        canel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
    }
}
