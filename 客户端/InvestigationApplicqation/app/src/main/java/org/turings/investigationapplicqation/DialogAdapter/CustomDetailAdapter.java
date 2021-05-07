package org.turings.investigationapplicqation.DialogAdapter;


import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.turings.investigationapplicqation.Entity.Questionnaire;
import org.turings.investigationapplicqation.Entity.Result;
import org.turings.investigationapplicqation.R;
import org.turings.investigationapplicqation.Util.CustomListView;
import org.turings.investigationapplicqation.Util.ExpandableViewHoldersUtil;
import org.turings.investigationapplicqation.Util.OnItemClickListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomDetailAdapter extends RecyclerView.Adapter<CustomDetailAdapter.WxArticleHolder> {

    private Context mContext;
    private List<Result> mList;
    private final Questionnaire questionnaire;
    public CustomDetailAdapter(Context context, List<Result> list,Questionnaire questionnaire) {
        this.mContext = context;
        this.mList = list;
        this.questionnaire = questionnaire;
    }

    ExpandableViewHoldersUtil.KeepOneH<WxArticleHolder> keepOne = new ExpandableViewHoldersUtil.KeepOneH<>();

    //点击事件的回调
    private OnItemClickListener<Result> onItemClickListener;

    //设置回调监听
    public void setOnItemClickListener(OnItemClickListener<Result> listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public WxArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WxArticleHolder((ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final WxArticleHolder holder, final int position) {
        Log.i("eee", "onBindViewHolder: 什么");
        final Result bean = mList.get(position);
        holder.bind(position, bean);
    }


    @Override
    public int getItemCount() {
        Log.i("www", "getItemCount: "+mList);
        return mList == null ? 0 : mList.size();
    }

    public class WxArticleHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ExpandableViewHoldersUtil.Expandable {

        private WxArticleHolder mHolder;
        public final TextView mTitle;
        public final TextView mSource;
        public final ImageView mRight;
        public final RelativeLayout mTopLayout; //折叠View
        public final LinearLayout mBottomLayout; //折叠View
        public final TextView order;//排序
        public final TextView time;//回答时间
        public final TextView ip;//来源
        public final CustomListView listView;//回答时间
        public WxArticleHolder(@NonNull View itemView) {
            super(itemView);
            mHolder = this;
            mTitle = itemView.findViewById(R.id.mTitle);
            mSource = itemView.findViewById(R.id.mSource);
            mRight = itemView.findViewById(R.id.mRight);
            mTopLayout = itemView.findViewById(R.id.mTopLayout);
            order = itemView.findViewById(R.id.order);
            time = itemView.findViewById(R.id.time);
            ip = itemView.findViewById(R.id.ip);
            listView = itemView.findViewById(R.id.listView);
            mBottomLayout = itemView.findViewById(R.id.mBottomLayout);
            mTopLayout.setOnClickListener(this);
        }

        //绑定数据
        public void bind(final int pos, final Result bean) {
            keepOne.bind(this,pos);
            String d1;
            DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            d1 = format2.format(bean.getTime()); // 转换成 yyyyMMddHHmmss
            mTitle.setText("序号："+(pos+1)); //标题
            mSource.setText("提交时间："+d1); //来源
            order.setText("序号："+(pos+1));
            time.setText("提交时间："+d1);
            ip.setText("ip:192.168.137.149");
            //listView
            CustomAnswerAdapter customAnswerAdapter = new CustomAnswerAdapter(questionnaire,bean.getResults(),mContext,R.layout.result_data_item);
            listView.setAdapter(customAnswerAdapter);
            mHolder.mBottomLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(bean, mHolder.mBottomLayout, pos);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mTopLayout:
                    keepOne.toggle(mHolder, mRight);
                    break;
            }
        }

        @Override
        public View getExpandView() {
            return mBottomLayout;
        }
    }
}
