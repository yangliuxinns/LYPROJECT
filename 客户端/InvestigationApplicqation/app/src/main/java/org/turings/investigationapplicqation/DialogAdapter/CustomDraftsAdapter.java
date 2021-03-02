package org.turings.investigationapplicqation.DialogAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.turings.investigationapplicqation.EditQuestionnaire;
import org.turings.investigationapplicqation.Entity.Questionnaire;
import org.turings.investigationapplicqation.R;

import java.util.List;

public class CustomDraftsAdapter extends BaseAdapter {
    private List<Questionnaire> list;//数据源
    private Context context;//上下文环境
    private int itemLayout;//布局文件
    public ViewHolder holder;

    public CustomDraftsAdapter(List<Questionnaire> list, Context context, int itemLayout) {
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
            view = LayoutInflater.from(context).inflate(R.layout.drafts_project_item_layout,null);
            holder.title = view.findViewById(R.id.title);
            holder.instruction = view.findViewById(R.id.instruction);
            holder.lyEd = view.findViewById(R.id.ly_ed1);
            holder.pub = view.findViewById(R.id.pub);
            holder.del = view.findViewById(R.id.del);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.title.setText(list.get(listPosition).getTitle());
        holder.instruction.setText(list.get(listPosition).getInstructions());
//        holder.lyEd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        holder.lyEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("www", "onClick:点击编辑事件 ");
                Intent intent = new Intent(context, EditQuestionnaire.class);
                intent.putExtra("questionnaire_data", list.get(listPosition));
                context.startActivity(intent);
            }
        });
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除,放入回收站
                mOnItemDeleteListener.onDeleteClick(listPosition);

            }
        });
        return view;
    }
    public final static class ViewHolder{
        public TextView title;
        public TextView instruction;
        private LinearLayout lyEd;//编辑
        private LinearLayout pub;//发布
        private LinearLayout del;//删除
    }

    /**
     * 删除按钮的监听接口
     */
    public interface onItemDeleteListener {
        void onDeleteClick(int position);
    }

    private CustomQuestionnaireAdapter.onItemDeleteListener mOnItemDeleteListener;

    public void setmOnItemDeleteClickListener(CustomQuestionnaireAdapter.onItemDeleteListener mOnItemDeleteListener) {
        this.mOnItemDeleteListener  = mOnItemDeleteListener;
    }
}
