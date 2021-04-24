package org.turings.investigationapplicqation.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

import org.turings.investigationapplicqation.DialogAdapter.CustomPublishAdapter;
import org.turings.investigationapplicqation.DialogAdapter.CustomQuestionnaireAdapter;
import org.turings.investigationapplicqation.DialogAdapter.CustomSummaryResultAdapter;
import org.turings.investigationapplicqation.Entity.Options;
import org.turings.investigationapplicqation.Entity.Question;
import org.turings.investigationapplicqation.Entity.Questionnaire;
import org.turings.investigationapplicqation.Entity.Result;
import org.turings.investigationapplicqation.Entity.ResultInfo;
import org.turings.investigationapplicqation.R;
import org.turings.investigationapplicqation.Util.ChartView;
import org.turings.investigationapplicqation.Util.CustomListView;
import org.turings.investigationapplicqation.Util.EChartOptionUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//图表统计
public class SummaryFragment extends Fragment {
    private View view;
    private ListView customListView;
    private CustomSummaryResultAdapter customSummaryResultAdapter;
    private ChartView chartView;
    private static int qId;
    private OkHttpClient okHttpClient;
    private SmartRefreshLayout srl;//刷新
    private ImageView nullYlx;//如果没有题目显示
    private LinearLayout lyYlx;//空空如也外框
    private List<Result> lists;
    private List<Result> list;
    private static Questionnaire questionnaire;
    private Questionnaire questionnaire1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    String ms = (String) msg.obj;
                    if (ms.equals("刷新")) {
                        list.clear();
                        for (Result result : lists) {
                            list.add(result);
                            customSummaryResultAdapter.notifyDataSetChanged();
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
                        customSummaryResultAdapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    String ms1 = (String) msg.obj;
                    if (ms1.equals("刷新")) {
                        questionnaire1 = questionnaire;
                    }
                    break;
            }
        }
    };
    // 可用于传值
    public static SummaryFragment newInstance(Questionnaire id) {
        questionnaire = id;
        Bundle bundle = new Bundle();
        SummaryFragment fragment = new SummaryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.summary_fragment, container, false);
        qId = questionnaire.getId();
        init();
        //准备假的数据
//        Options options1 = new Options(1, "选项1", "",null);
//        Options options2 = new Options(1, "选项2", "",null);
//        Options options3 = new Options(1, "选项3", "",null);
//        Options options4 = new Options(1, "选项4", "",null);
//        final List<Options> lo = new ArrayList<>();
//        lo.add(options1);
//        lo.add(options2);
//        lo.add(options3);
//        lo.add(options4);
//        Question question1 = new Question(1, 1, "我是题目1", "填空", null, true, 1);
//        Question question2 = new Question(1, 2, "我是题目2", "单选题", lo, true, 1);
//        Question question3 = new Question(1, 3, "我是题目3", "单选题", lo, true, 1);
//        Question question4 = new Question(1, 4, "我是题目4", "单选题", lo, true, 1);
//        Question question5 = new Question(1, 5, "我是题目5", "单选题", lo, true, 1);
//        Question question6 = new Question(1, 6, "我是题目6", "单选题", lo, true, 1);
//        List<Question> lq = new ArrayList<>();
//        lq.add(question1);
//        lq.add(question2);
//        lq.add(question3);
//        lq.add(question4);
//        lq.add(question5);
//        lq.add(question6);
//        Questionnaire questionnaire = new Questionnaire(1, "问卷", getString(R.string.welcome), false,null,0,false,false,false,null,1,false);
//        questionnaire.setList(lq);
//        ResultInfo resultInfo1 = new ResultInfo(1,2,2,"选项2");
//        ResultInfo resultInfo2 = new ResultInfo(2,1,2,"填空");
//        List< ResultInfo > resultInfos = new ArrayList<>();
//        resultInfos.add(resultInfo1);
//        resultInfos.add(resultInfo2);
//        Result result1 = new Result(1,1,new Date(), resultInfos);
//        ResultInfo resultInfo12 = new ResultInfo(1,2,3,"选项3");
//        ResultInfo resultInfo13 = new ResultInfo(1,2,1,"选项1");
//        ResultInfo resultInfo14 = new ResultInfo(1,2,4,"选项4");
//        ResultInfo resultInfo15 = new ResultInfo(2,1,2,"填空2");
//        ResultInfo resultInfo16 = new ResultInfo(2,1,2,"填空3");
//        ResultInfo resultInfo17= new ResultInfo(2,1,2,"填空4");
//        List< ResultInfo > resultInfos2 = new ArrayList<>();
//        resultInfos2.add(resultInfo12);
//        resultInfos2.add(resultInfo15);
//        List< ResultInfo > resultInfos3 = new ArrayList<>();
//        resultInfos3.add(resultInfo13);
//        resultInfos3.add(resultInfo16);
//        List< ResultInfo > resultInfos4 = new ArrayList<>();
//        resultInfos4.add(resultInfo14);
//        resultInfos4.add(resultInfo17);
//        Result result2 = new Result(1,1,new Date(), resultInfos2);
//        Result result3 = new Result(1,1,new Date(), resultInfos3);
//        Result result4 = new Result(1,1,new Date(), resultInfos4);
//        List<Result> list = new ArrayList<>();
//        list.add(result1);
//        list.add(result2);
//        list.add(result3);
//        list.add(result4);
//        customListView = view.findViewById(R.id.list);
//        customSummaryResultAdapter = new CustomSummaryResultAdapter(questionnaire,list,getContext(),R.layout.summary_result_item);
//        customListView.setAdapter(customSummaryResultAdapter);
//        customSummaryResultAdapter.setmOnItemDownMoveClickListener(new CustomSummaryResultAdapter.onItemDownMoveListener() {
//            @Override
//            public void onDownMoveClick(int position,View view) {
//                chartView = view.findViewById(R.id.lineChart);
//                chartView.setWebViewClient(new WebViewClient(){
//                    @Override
//                    public void onPageFinished(WebView view, String url) {
//                        super.onPageFinished(view, url);
//                        //最好在h5页面加载完毕后再加载数据，防止html的标签还未加载完成，不能正常显示
//                        refreshLineChart();
//                    }
//                });
//            }
//        });
        return view;
    }

    //初始化获取数据
    private void init() {
        nullYlx = view.findViewById(R.id.nullYlx);
        srl = view.findViewById(R.id.srl);
        lyYlx = view.findViewById(R.id.lyylx);
        customListView = view.findViewById(R.id.list);
        list = new ArrayList<>();
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
        customSummaryResultAdapter = new CustomSummaryResultAdapter(questionnaire,list,getContext(),R.layout.summary_result_item);
        customListView.setAdapter(customSummaryResultAdapter);
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
    //访问服务器上传至数据库，搜索
    private void uploadToDataBaseQ() {
        okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("uId", String.valueOf(qId))
                .build();
        String url = "http://" + getResources().getString(R.string.ipConfig) + ":8080/WorkProject/ylx/findQuestionaresById";
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
                        Type type = new TypeToken<Questionnaire>() {
                        }.getType();
                        //反序列化
                        questionnaire = gson.fromJson(response.body().string(), type);
                        Message msg = Message.obtain();
                        msg.what = 2;
                        if (questionnaire != null) {
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
