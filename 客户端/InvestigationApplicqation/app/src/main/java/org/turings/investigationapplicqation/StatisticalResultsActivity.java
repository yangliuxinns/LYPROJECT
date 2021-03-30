package org.turings.investigationapplicqation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.turings.investigationapplicqation.Fragment.DetailedDataFragment;
import org.turings.investigationapplicqation.Fragment.DraftsFragment;
import org.turings.investigationapplicqation.Fragment.PublishedFragment;
import org.turings.investigationapplicqation.Fragment.SummaryFragment;
import org.turings.investigationapplicqation.Util.ChartView;
import org.turings.investigationapplicqation.Util.EChartOptionUtil;

import java.util.ArrayList;
//统计结果
public class StatisticalResultsActivity extends AppCompatActivity implements View.OnClickListener {

    private View view;
    private ViewPager viewPager;
    private ArrayList<Fragment> mDatas;

    private TextView noticeTv, findTv;

    private ImageView cursor;
    private int bmpw = 0, mCurrentIndex = 0, mScreen1_5, fixLeftMargin;// bmpw游标宽度,mCurrentIndex表示当前所在页面
    private LinearLayout.LayoutParams params;
    private ImageView back;//返回
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical_results);
        initView();
        initImg();
    }
    // 初始化imageview
    private void initImg() {
        Display disPlay = this.getWindowManager().getDefaultDisplay();
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
            case 0:// 统计结果
                noticeTv.setTextColor(getResources().getColor(R.color.colorMain));
                break;
            case 1:// 详细数据
                findTv.setTextColor(getResources().getColor(R.color.colorMain));
                break;
        }
        mCurrentIndex = position;
    }

    // 初始化控件
    private void initView() {
        LinearLayout noticeLayout = findViewById(R.id.layout_notice1);
        noticeLayout.setOnClickListener(this);
        LinearLayout findLayout = findViewById(R.id.layout_find1);
        findLayout.setOnClickListener(this);

        noticeTv = findViewById(R.id.tv_notice);
        findTv = findViewById(R.id.tv_find);

        cursor = findViewById(R.id.cursor);
        viewPager = findViewById(R.id.viewpager);

        mDatas = new ArrayList<>();
        SummaryFragment tab01 = SummaryFragment.newInstance("标题A");
        DetailedDataFragment tab02 = DetailedDataFragment.newInstance("标题B");
        mDatas.add(tab01);
        mDatas.add(tab02);

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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
            case R.id.layout_notice1:// 统计结果
                viewPager.setCurrentItem(0);
                break;
            case R.id.layout_find1:// 详细数据
                viewPager.setCurrentItem(1);
                break;
        }
    }
}
