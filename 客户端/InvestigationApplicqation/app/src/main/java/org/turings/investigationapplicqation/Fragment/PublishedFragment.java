package org.turings.investigationapplicqation.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.turings.investigationapplicqation.DialogAdapter.CustomDialogYLX;
import org.turings.investigationapplicqation.DialogAdapter.CustomDrafDialog;
import org.turings.investigationapplicqation.DialogAdapter.CustomDraftsAdapter;
import org.turings.investigationapplicqation.DialogAdapter.CustomPublishAdapter;
import org.turings.investigationapplicqation.DialogAdapter.CustomQuestionnaireAdapter;
import org.turings.investigationapplicqation.Entity.Questionnaire;
import org.turings.investigationapplicqation.R;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import static android.content.Context.MODE_PRIVATE;

//发布的问卷
public class PublishedFragment extends Fragment {
    private ListView listView;
    private CustomPublishAdapter customPublishAdapter;
    private List<Questionnaire> list;
    private List<Questionnaire> lists;
    private View view;
    private OkHttpClient okHttpClient;
    private SmartRefreshLayout srl;//刷新
    private ImageView nullYlx;//如果没有题目显示
    private LinearLayout lyYlx;//空空如也外框
    private String uId;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String ms = (String) msg.obj;
            if (ms.equals("刷新")) {
                list.clear();
                for (Questionnaire questionnaire : lists) {
                    list.add(questionnaire);
                    customPublishAdapter.notifyDataSetChanged();
                }
                if (list.size() == 0 || list == null) {
                    nullYlx.setVisibility(View.VISIBLE);
                    lyYlx.setVisibility(View.VISIBLE);
                } else {
                    nullYlx.setVisibility(View.GONE);
                    lyYlx.setVisibility(View.GONE);
                }
            } else {
                list.clear();
                if (list.size() == 0 || list == null) {
                    nullYlx.setVisibility(View.VISIBLE);
                    lyYlx.setVisibility(View.VISIBLE);
                } else {
                    nullYlx.setVisibility(View.GONE);
                    lyYlx.setVisibility(View.GONE);
                }
                customPublishAdapter.notifyDataSetChanged();
            }
        }
    };
    // 可用于传值
    public static PublishedFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        PublishedFragment fragment = new PublishedFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.published_layout, container, false);
        //获取用户的id
        SharedPreferences sp = getContext().getSharedPreferences("userInfo",MODE_PRIVATE);
        uId = sp.getString("uId",null);
        getViews();
        init();
        return view;
    }
    //初始化
    private void init() {
        //获得所有草稿箱的问卷
//        Questionnaire qn1 = new Questionnaire(1,"调查问卷1","问卷说明1lalaalalalalalala",false,null,0,false,false,null,"","");
//        Questionnaire qn2 = new Questionnaire(1,"调查问卷2","问卷说明1lalaalalalalalala",false,null,0);
//        Questionnaire qn3 = new Questionnaire(1,"调查问卷3","问卷说明1lalaalalalalalala",false,null,0);
//        Questionnaire qn4 = new Questionnaire(1,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0);
//        Questionnaire qn5 = new Questionnaire(1,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0);
//        Questionnaire qn6 = new Questionnaire(1,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0);
//        Questionnaire qn7 = new Questionnaire(1,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0);
//        Questionnaire qn8 = new Questionnaire(1,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0);
//        Questionnaire qn9 = new Questionnaire(1,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0);
//        Questionnaire qn10 = new Questionnaire(1,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0);
        list = new ArrayList<>();
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadToDataBase();
            }
        }).start();
        if (list.size() == 0 || list == null) {
            nullYlx.setVisibility(View.VISIBLE);
        } else {
            nullYlx.setVisibility(View.GONE);
        }
        customPublishAdapter = new CustomPublishAdapter(list,getContext(),R.layout.published_project_item_layout);
        listView.setAdapter(customPublishAdapter);
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshData();
                srl.finishRefresh();
                Toast.makeText(getActivity().getApplicationContext(),
                        "刷新完成",
                        Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击编辑
                //提示是否停止收集，进入编辑
                showCustomDialog(list.get(position));
            }
        });
        customPublishAdapter.setmOnItemDeleteClickListener(new CustomQuestionnaireAdapter.onItemDeleteListener() {
            @Override
            public void onDeleteClick(final int position) {
                //删除
                list.get(position).setDel(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        deleteToDataBase(list.get(position));
                    }
                }).start();
                //更新
            }
        });
    }

    //获得控件
    private void getViews() {
        listView = view.findViewById(R.id.lv_projects);
        nullYlx = view.findViewById(R.id.nullYlx);
        srl = view.findViewById(R.id.srl);
        lyYlx = view.findViewById(R.id.lyylx);
    }
    //刷新数据
    private void refreshData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadToDataBase();
            }
        }).start();
    }
    //访问服务器上传至数据库，搜索
    private void uploadToDataBase() {
        okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("uId", uId)
                .build();
        String url = "http://" + getResources().getString(R.string.ipConfig) + ":8080/WorkProject/ylx/findQuestionaresByUserIdPublish";
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
                        lists = new ArrayList<>();
                        lists = gson.fromJson(response.body().string(), type);
                        Message msg = Message.obtain();
                        if (lists.size() != 0) {
                            msg.obj = "刷新";
                        } else {
                            msg.obj = "无数据";
                        }
                        handler.sendMessage(msg);
                    }
                });
            }
        }).start();
    }
    private void showCustomDialog(Questionnaire questionnaire) {
        //管理多个Fragment
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
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
    //删除
    private void deleteToDataBase(Questionnaire questionnaire) {
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
                        refreshData();
                        Log.i("lww", response.body().string());
                    }
                });
            }
        }).start();
    }
}
