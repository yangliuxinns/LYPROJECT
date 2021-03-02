package org.turings.investigationapplicqation.DialogAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.turings.investigationapplicqation.EditQuestionnaire;
import org.turings.investigationapplicqation.EditTopicSettings;
import org.turings.investigationapplicqation.Entity.TopicBigType;
import org.turings.investigationapplicqation.Entity.TopicType;
import org.turings.investigationapplicqation.R;

import java.util.List;

public class CustomGridAdapter extends BaseAdapter {

    private List<TopicType> list;//数据源
    private Context context;//上下文环境
    private int itemLayout;//布局文件
    public ViewHolder holder;

    public CustomGridAdapter(List<TopicType> list, Context context, int itemLayout) {
        this.list = list;
        this.context = context;
        this.itemLayout = itemLayout;
    }

    @Override
    public int getCount() {
        if(list != null){
            return list.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if(list != null){
            return list.get(i);
        }else {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        if(list != null){
            return i;
        }else {
            return 0;
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int listPosition = i;
        if(null == view){
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(itemLayout,null);
            holder.txt = view.findViewById(R.id.txt_icon);
            holder.img = view.findViewById(R.id.img_icon);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.txt.setText(list.get(listPosition).getTextName());
        int icon = context.getResources().getIdentifier(list.get(listPosition).getiName(), "mipmap", context.getPackageName());
        // 设置图片
        holder.img.setImageResource(icon);
//        holder.img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mOnItemClickListener.onNumClick(listPosition);
//            }
//        });
        return view;
    }
    public final static class ViewHolder{
        public TextView txt;
        public ImageView img;
    }

    /**
     * 点击题目按钮的监听接口
     */
    public interface onItemClickListener {
        void onNumClick(int position);
    }

    private onItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(onItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener  = mOnItemClickListener;
    }
}
