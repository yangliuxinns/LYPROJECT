package org.turings.investigationapplicqation.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.turings.investigationapplicqation.CreateBlankQuestionnaire;
import org.turings.investigationapplicqation.R;
import org.turings.investigationapplicqation.SearchActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class WorkBenchFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ViewPager viewPager;
    private ArrayList<Fragment> mDatas;

    private TextView noticeTv, findTv;

    private ImageView cursor;
    private int bmpw = 0, mCurrentIndex = 0, mScreen1_5, fixLeftMargin;// bmpw游标宽度,mCurrentIndex表示当前所在页面
    private LinearLayout.LayoutParams params;
    //搜索
    private LinearLayout ll_search;
    //添加项目
    private ImageView ivAddProject;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.work_bench_layout,container,false);
        initView();
        initImg();
        getViews();
        getRegister();
        return view;
    }

    //注册
    private void getRegister() {
        ll_search.setOnClickListener(this);
        ivAddProject.setOnClickListener(this);
    }

    //获取控件
    private void getViews() {
        ll_search = view.findViewById(R.id.search_layout);
        ivAddProject = view.findViewById(R.id.iv_addProject);
    }

    // 初始化imageview
    private void initImg() {
        Display disPlay = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        disPlay.getMetrics(outMetrics);
        mScreen1_5 = outMetrics.widthPixels/2-120;
        bmpw = outMetrics.widthPixels/2-120;
        fixLeftMargin = 0;
        ViewGroup.LayoutParams layoutParams = cursor.getLayoutParams();
        layoutParams.width = mScreen1_5;
        cursor.setLayoutParams(layoutParams);
        /**
         * 设置左侧固定距离
         */
        params = (android.widget.LinearLayout.LayoutParams) cursor.getLayoutParams();
        params.leftMargin = fixLeftMargin;
        cursor.setLayoutParams(params);
    }

    // 改变游动条
    private void changeTextView(int position) {
        noticeTv.setTextColor(getResources().getColor(R.color.grey));
        findTv.setTextColor(getResources().getColor(R.color.grey));
        switch (position) {
            case 0:// 草稿箱
                noticeTv.setTextColor(getResources().getColor(R.color.colorMain));
                break;
            case 1:// 已发布
                findTv.setTextColor(getResources().getColor(R.color.colorMain));
                break;
        }
        mCurrentIndex = position;
    }

    // 初始化控件
    private void initView() {
        LinearLayout noticeLayout = view.findViewById(R.id.layout_notice);
        noticeLayout.setOnClickListener(this);
        LinearLayout findLayout = view.findViewById(R.id.layout_find);
        findLayout.setOnClickListener(this);

        noticeTv = view.findViewById(R.id.tv_notice);
        findTv = view.findViewById(R.id.tv_find);

        cursor = view.findViewById(R.id.cursor);
        viewPager = view.findViewById(R.id.viewpager);

        mDatas = new ArrayList<>();
        DraftsFragment tab01 = DraftsFragment.newInstance("标题A");
        PublishedFragment tab02 = PublishedFragment.newInstance("标题B");
        mDatas.add(tab01);
        mDatas.add(tab02);

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return mDatas.size();
            }

            @Override
            public Fragment getItem(int positon) {
                return mDatas.get(positon);
            }
        };

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int positon) {
                // 滑动结束
                changeTextView(positon);
            }

            @Override
            public void onPageScrolled(int positon, float positonOffset, int positonOffsetPx) {
                // 滑动过程
                if (mCurrentIndex == 0 && positon == 0) {// 0-->1
                    params.leftMargin = (int) (mCurrentIndex * bmpw + positonOffset
                            * bmpw)
                            + fixLeftMargin;
                } else if (mCurrentIndex == 1 && positon == 0) {// 1-->0
                    params.leftMargin = (int) (mCurrentIndex * bmpw + (positonOffset - 1)
                            * bmpw)
                            + fixLeftMargin;
                } else if (mCurrentIndex == 1 && positon == 1) {// 1-->1
                    params.leftMargin = (int) (mCurrentIndex * bmpw + positonOffset
                            * bmpw)
                            + fixLeftMargin;
                } else if (mCurrentIndex == 2 && positon == 1) {// 2-->1
                    params.leftMargin = (int) (mCurrentIndex * bmpw + (positonOffset - 1)
                            * bmpw)
                            + fixLeftMargin;
                } else if (mCurrentIndex == 2 && positon == 2) {// 2-->2
                    params.leftMargin = (int) (mCurrentIndex * bmpw + positonOffset
                            * bmpw)
                            + fixLeftMargin;
                }
                cursor.setLayoutParams(params);
            }

            @Override
            public void onPageScrollStateChanged(int positon) {
                // TODO Auto-generated method stub

            }
        });
        viewPager.setOffscreenPageLimit(mDatas.size());// 缓存
    }

    // 控件点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_notice:// 草稿箱
                viewPager.setCurrentItem(0);
                break;
            case R.id.layout_find:// 已发布
                viewPager.setCurrentItem(1);
                break;
            case R.id.search_layout://跳转搜索页面
                Intent intent1 = new Intent(getContext(), SearchActivity.class);
                startActivity(intent1);
                break;
            case R.id.iv_addProject://添加项目
                Intent intent = new Intent(getContext(),CreateBlankQuestionnaire.class);
                startActivity(intent);
                break;
        }
    }
}
