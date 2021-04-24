package org.turings.investigationapplicqation.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.turings.investigationapplicqation.DialogAdapter.CustomDetailAdapter;
import org.turings.investigationapplicqation.DialogAdapter.CustomSummaryResultAdapter;
import org.turings.investigationapplicqation.Entity.Options;
import org.turings.investigationapplicqation.Entity.Question;
import org.turings.investigationapplicqation.Entity.Questionnaire;
import org.turings.investigationapplicqation.Entity.Result;
import org.turings.investigationapplicqation.Entity.ResultInfo;
import org.turings.investigationapplicqation.R;
import org.turings.investigationapplicqation.Util.ChartView;
import org.turings.investigationapplicqation.Util.OnItemClickListener;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//详细答案
public class DetailedDataFragment extends Fragment {

    private RecyclerView mRecycler;
    private List<Result> mList = new ArrayList<>();
    private CustomDetailAdapter mAdapter;
    private View view;
    private static Questionnaire questionnaire;
    private static int qId;
    private ListView customListView;
    private CustomSummaryResultAdapter customSummaryResultAdapter;
    private ChartView chartView;
    private OkHttpClient okHttpClient;
    private SmartRefreshLayout srl;//刷新
    private ImageView nullYlx;//如果没有题目显示
    private LinearLayout lyYlx;//空空如也外框
    private List<Result> lists;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    String ms = (String) msg.obj;
                    if (ms.equals("刷新")) {
                        mList.clear();
                        for (Result result : lists) {
                            mList.add(result);
                            mAdapter.notifyDataSetChanged();
                        }
                        if (mList.size() == 0 || mList == null) {
                            nullYlx.setVisibility(View.VISIBLE);
                            lyYlx.setVisibility(View.VISIBLE);
                        } else {
                            nullYlx.setVisibility(View.GONE);
                            lyYlx.setVisibility(View.GONE);
                        }
                    } else {
                        mList.clear();
                        if (mList.size() == 0 || mList == null) {
                            nullYlx.setVisibility(View.VISIBLE);
                            lyYlx.setVisibility(View.VISIBLE);
                        } else {
                            nullYlx.setVisibility(View.GONE);
                            lyYlx.setVisibility(View.GONE);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };
    // 可用于传值
    public static DetailedDataFragment newInstance(Questionnaire id) {
        Bundle bundle = new Bundle();
        questionnaire = id;
        DetailedDataFragment fragment = new DetailedDataFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.detail_fragment, container, false);
        qId = questionnaire.getId();
//        initData();
        initView();
        return view;
    }
    //初始化数据
    private void initData() {
        //初始化数据
        //准备假的数据
        Options options1 = new Options(1, "选项1", "",null);
        Options options2 = new Options(1, "选项2", "",null);
        Options options3 = new Options(1, "选项3", "",null);
        Options options4 = new Options(1, "选项4", "",null);
        final List<Options> lo = new ArrayList<>();
        lo.add(options1);
        lo.add(options2);
        lo.add(options3);
        lo.add(options4);
        Question question1 = new Question(1, 1, "我是题目1", "填空", null, true, 1);
        Question question2 = new Question(1, 2, "我是题目2", "单选题", lo, true, 1);
        Question question3 = new Question(1, 3, "我是题目3", "多选题", lo, true, 1);
        Question question4 = new Question(1, 4, "我是题目4", "单选题", lo, true, 1);
        Question question5 = new Question(1, 5, "我是题目5", "单选题", lo, true, 1);
        Question question6 = new Question(1, 6, "我是题目6", "单选题", lo, true, 1);
        List<Question> lq = new ArrayList<>();
        lq.add(question1);
        lq.add(question2);
        lq.add(question3);
        lq.add(question4);
        lq.add(question5);
        lq.add(question6);
        questionnaire = new Questionnaire(1, "问卷", getString(R.string.welcome), false,null,0,false,false,false,null,1,false);
        questionnaire.setList(lq);
        ResultInfo resultInfo1 = new ResultInfo(1,2,2,"选项2");
        ResultInfo resultInfo2 = new ResultInfo(2,1,2,"填空");
        ResultInfo resultInfo3 = new ResultInfo(3,3,1,"选项1");
        ResultInfo resultInfo4 = new ResultInfo(4,4,2,"选项2");
        ResultInfo resultInfo5 = new ResultInfo(5,5,2,"选项2");
        ResultInfo resultInfo6 = new ResultInfo(6,6,2,"选项2");
        List< ResultInfo > resultInfos = new ArrayList<>();
        resultInfos.add(resultInfo1);
        resultInfos.add(resultInfo2);
        resultInfos.add(resultInfo3);
        resultInfos.add(resultInfo4);
        resultInfos.add(resultInfo5);
        resultInfos.add(resultInfo6);
        Result result1 = new Result(1,1,new Date(), resultInfos);
        Result result3 = new Result(1,1,new Date(), resultInfos);
        ResultInfo resultInfo12 = new ResultInfo(1,2,3,"选项3");
        List< ResultInfo > resultInfos2 = new ArrayList<>();
        resultInfos2.add(resultInfo12);
        Result result2 = new Result(1,1,new Date(), resultInfos2);
        mList.add(result1);
        mList.add(result2);
        mList.add(result3);
    }



    //初始化View
    private void initView() {
        nullYlx = view.findViewById(R.id.nullYlx);
        srl = view.findViewById(R.id.srl);
        lyYlx = view.findViewById(R.id.lyylx);
        customListView = view.findViewById(R.id.list);
        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadToDataBase();
            }
        }).start();
        if (mList.size() == 0 || mList == null) {
            nullYlx.setVisibility(View.VISIBLE);
        } else {
            nullYlx.setVisibility(View.GONE);
        }
        mRecycler = view.findViewById(R.id.mRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(layoutManager);
        if (mAdapter == null) {
            mAdapter = new CustomDetailAdapter(getActivity(), mList,questionnaire);
            mRecycler.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mRecycler.addItemDecoration(new DividerItemDecoration(view.getContext(),DividerItemDecoration.VERTICAL));
        mAdapter.notifyDataSetChanged();
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
//    @Override
//    public void onStart() {
//        super.onStart();
//        initView();
//        initData();
//    }

    //访问服务器上传至数据库，搜索
    private void uploadToDataBase() {
        okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("qId", String.valueOf(qId))
                .build();
        String url = "http://" + getResources().getString(R.string.ipConfig) + ":8080/WorkProject/ylx/findResultByQuestionaireId";
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
                        Type type = new TypeToken<List<Result>>() {
                        }.getType();
                        //反序列化
                        lists = new ArrayList<>();
                        lists = gson.fromJson(response.body().string(), type);
                        Message msg = Message.obtain();
                        msg.what = 1;
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
}
