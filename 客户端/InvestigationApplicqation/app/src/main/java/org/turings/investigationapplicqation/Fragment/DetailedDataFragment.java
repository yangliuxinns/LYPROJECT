package org.turings.investigationapplicqation.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import org.turings.investigationapplicqation.DialogAdapter.CustomDetailAdapter;
import org.turings.investigationapplicqation.Entity.Options;
import org.turings.investigationapplicqation.Entity.Question;
import org.turings.investigationapplicqation.Entity.Questionnaire;
import org.turings.investigationapplicqation.Entity.Result;
import org.turings.investigationapplicqation.Entity.ResultInfo;
import org.turings.investigationapplicqation.R;
import org.turings.investigationapplicqation.Util.OnItemClickListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//详细答案
public class DetailedDataFragment extends Fragment {

    private RecyclerView mRecycler;
    private List<Result> mList = new ArrayList<>();
    private CustomDetailAdapter mAdapter;
    private View view;
    private Questionnaire questionnaire;
    // 可用于传值
    public static DetailedDataFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        DetailedDataFragment fragment = new DetailedDataFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.detail_fragment, container, false);

        initData();
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
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        initView();
//        initData();
//    }
}
