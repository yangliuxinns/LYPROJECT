package org.turings.investigationapplicqation.DialogAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import org.turings.investigationapplicqation.EditQuestionnaire;
import org.turings.investigationapplicqation.EditTopicSettings;
import org.turings.investigationapplicqation.Entity.TopicBigType;
import org.turings.investigationapplicqation.R;
import org.turings.investigationapplicqation.Util.CustomGridView;

import java.util.List;

public class CustomAddQuestionAdapter extends BaseAdapter {

    private List<TopicBigType> list;//数据源
    private Context context;//上下文环境
    private int itemLayout;//布局文件
    public ViewHolder holder;

    public CustomAddQuestionAdapter(List<TopicBigType> list, Context context, int itemLayout) {
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
            holder.qType = view.findViewById(R.id.q_type);
            holder.grid_option = view.findViewById(R.id.grid_option);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.qType.setText(list.get(listPosition).getTextTopic());
        CustomGridAdapter customGridAdapter = new CustomGridAdapter(list.get(listPosition).getList(),context,R.layout.grid_item);
        holder.grid_option.setAdapter(customGridAdapter);
        holder.grid_option.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(list.get(listPosition).getList().get(i).getTextName().equals("题库")){

                }else {
                    Intent intent = new Intent(context, EditTopicSettings.class);
                    intent.putExtra("type",list.get(listPosition).getList().get(i).getTextName());
                    ((EditQuestionnaire)context).startActivityForResult(intent,1);
                }
            }
        });
        return view;
    }
    public final static class ViewHolder{
        public TextView qType;
        public CustomGridView grid_option;
    }
}
