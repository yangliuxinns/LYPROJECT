package org.turings.investigationapplicqation.DialogAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.turings.investigationapplicqation.Entity.Photo;
import org.turings.investigationapplicqation.Entity.TopicType;
import org.turings.investigationapplicqation.R;

import java.util.List;

public class PicGridAdapter extends BaseAdapter {

    private List<Photo> list;//数据源
    private Context context;//上下文环境
    private int itemLayout;//布局文件
    public ViewHolder holder;

    public PicGridAdapter(List<Photo> list, Context context, int itemLayout) {
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
            holder.txt = view.findViewById(R.id.txt);
            holder.img = view.findViewById(R.id.img);
            holder.pre = view.findViewById(R.id.pre);
            holder.checkit = view.findViewById(R.id.checkit);
            holder.rl = view.findViewById(R.id.rl);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.txt.setText(list.get(listPosition).getName());
        int icon = context.getResources().getIdentifier(list.get(listPosition).getImg(), "mipmap", context.getPackageName());
        // 设置图片
        holder.img.setImageResource(icon);
        if(1 == list.get(listPosition).getPos()){
            //选中
            holder.checkit.setVisibility(View.VISIBLE);
            holder.img.setBackground(context.getDrawable(R.drawable.select_bg_img));
            holder.txt.setVisibility(View.GONE);
            holder.pre.setVisibility(View.VISIBLE);
        }else {
            //选中
            holder.checkit.setVisibility(View.GONE);
            holder.img.setBackground(context.getDrawable(R.drawable.select_bg1_img));
            holder.txt.setVisibility(View.VISIBLE);
            holder.pre.setVisibility(View.GONE);
        }
        holder.pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onNumClick(listPosition);

            }
        });
        return view;
    }
    public final static class ViewHolder{
        public TextView txt;
        public ImageView img;
        public ImageView checkit;
        public RelativeLayout rl;
        public Button pre;
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
