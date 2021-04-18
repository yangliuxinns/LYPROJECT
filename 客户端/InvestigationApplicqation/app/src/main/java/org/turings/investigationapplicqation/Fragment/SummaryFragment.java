package org.turings.investigationapplicqation.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
//图表统计
public class SummaryFragment extends Fragment {
    private View view;
    private ListView customListView;
    private CustomSummaryResultAdapter customSummaryResultAdapter;
    private ChartView chartView;
    // 可用于传值
    public static SummaryFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        SummaryFragment fragment = new SummaryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.summary_fragment, container, false);
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
        Question question3 = new Question(1, 3, "我是题目3", "单选题", lo, true, 1);
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
        Questionnaire questionnaire = new Questionnaire(1, "问卷", getString(R.string.welcome), false,null,0,false,false,false,null,1,false);
        questionnaire.setList(lq);
        ResultInfo resultInfo1 = new ResultInfo(1,2,2,"选项2");
        ResultInfo resultInfo2 = new ResultInfo(2,1,2,"填空");
        List< ResultInfo > resultInfos = new ArrayList<>();
        resultInfos.add(resultInfo1);
        resultInfos.add(resultInfo2);
        Result result1 = new Result(1,1,new Date(), resultInfos);
        ResultInfo resultInfo12 = new ResultInfo(1,2,3,"选项3");
        ResultInfo resultInfo13 = new ResultInfo(1,2,1,"选项1");
        ResultInfo resultInfo14 = new ResultInfo(1,2,4,"选项4");
        ResultInfo resultInfo15 = new ResultInfo(2,1,2,"填空2");
        ResultInfo resultInfo16 = new ResultInfo(2,1,2,"填空3");
        ResultInfo resultInfo17= new ResultInfo(2,1,2,"填空4");
        List< ResultInfo > resultInfos2 = new ArrayList<>();
        resultInfos2.add(resultInfo12);
        resultInfos2.add(resultInfo15);
        List< ResultInfo > resultInfos3 = new ArrayList<>();
        resultInfos3.add(resultInfo13);
        resultInfos3.add(resultInfo16);
        List< ResultInfo > resultInfos4 = new ArrayList<>();
        resultInfos4.add(resultInfo14);
        resultInfos4.add(resultInfo17);
        Result result2 = new Result(1,1,new Date(), resultInfos2);
        Result result3 = new Result(1,1,new Date(), resultInfos3);
        Result result4 = new Result(1,1,new Date(), resultInfos4);
        List<Result> list = new ArrayList<>();
        list.add(result1);
        list.add(result2);
        list.add(result3);
        list.add(result4);
        customListView = view.findViewById(R.id.list);
        customSummaryResultAdapter = new CustomSummaryResultAdapter(questionnaire,list,getContext(),R.layout.summary_result_item);
        customListView.setAdapter(customSummaryResultAdapter);
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

    private void refreshLineChart(){
        Object[] x = new Object[]{
                "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
        };
        Object[] y = new Object[]{
                820, 932, 901, 934, 1290, 1330, 1320
        };
        chartView.refreshEchartsWithOption(EChartOptionUtil.getLineChartOptions(x, y));
    }
}
