package org.turings.investigationapplicqation.DialogAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.turings.investigationapplicqation.Entity.Questionnaire;
import org.turings.investigationapplicqation.R;

import java.util.List;

public class CustomSearchResultListViewAdapter extends BaseAdapter {

    private List<Questionnaire> list;//数据源
    private Context context;//上下文环境
    private int itemLayout;//布局文件
    public ViewHolder holder;

    public CustomSearchResultListViewAdapter(List<Questionnaire> list, Context context, int itemLayout) {
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
            view = LayoutInflater.from(context).inflate(R.layout.search_result_questionare_item_layout,null);
            holder.title = view.findViewById(R.id.title);
            holder.instruction = view.findViewById(R.id.instruction);
            holder.img = view.findViewById(R.id.img);
            holder.states = view.findViewById(R.id.states);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.title.setText(list.get(listPosition).getTitle());
        holder.instruction.setText(list.get(listPosition).getInstructions());
        if(list.get(listPosition).isRelease()){
            //已发布
            holder.img.setImageResource(R.mipmap.statesok);
            holder.states.setText("已发布");
            holder.states.setTextColor(context.getResources().getColor(R.color.colorMain));
        }else {
            holder.img.setImageResource(R.mipmap.statesno);
            holder.states.setText("未发布");
            holder.states.setTextColor(context.getResources().getColor(R.color.colorTomato));
        }
        return view;
    }
    public final static class ViewHolder{
        public TextView title;
        public TextView instruction;
        private TextView states;
        private ImageView img;
    }
}
