package org.turings.investigationapplicqation.DialogAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.turings.investigationapplicqation.Entity.Options;
import org.turings.investigationapplicqation.Entity.Questionnaire;
import org.turings.investigationapplicqation.Entity.ResultInfo;
import org.turings.investigationapplicqation.R;

import java.util.List;

//每个人回答
public class CustomAnswerAdapter extends BaseAdapter {

    private Questionnaire questionnaire;//问卷
    private List<ResultInfo> list;//数据源
    private Context context;//上下文环境
    private int itemLayout;//布局文件
    public ViewHolder holder;
    public CustomAnswerAdapter(Questionnaire questionnaire,List<ResultInfo> list, Context context, int itemLayout) {
        this.questionnaire = questionnaire;
        this.list = list;
        this.context = context;
        this.itemLayout = itemLayout;
    }

    @Override
    public int getCount() {
        if(questionnaire.getList() != null){
            return questionnaire.getList().size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if(questionnaire.getList() != null){
            return questionnaire.getList().get(i);
        }else {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        if(questionnaire.getList() != null){
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
            holder.title = view.findViewById(R.id.title);
            holder.answer= view.findViewById(R.id.answer);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        if(questionnaire.getList().get(listPosition).getType().equals("多选题")){
            holder.title.setText(questionnaire.getList().get(listPosition).getOrder()+"."+questionnaire.getList().get(listPosition).getTitle()+"  [多选题]");
        }else {
            holder.title.setText(questionnaire.getList().get(listPosition).getOrder()+"."+questionnaire.getList().get(listPosition).getTitle());
        }
        StringBuffer stringBuffer = new StringBuffer();
        for(ResultInfo resultInfo : list){
                if(resultInfo.getQuestion_number() == questionnaire.getList().get(listPosition).getOrder()){
                    if(stringBuffer.length()>0) {
                        stringBuffer.append("|"+resultInfo.getResult());
                    }else {
                        stringBuffer.append(resultInfo.getResult());
                    }
                }
        }
        holder.answer.setText(stringBuffer.toString());
        return view;
    }
    public final static class ViewHolder{
        public TextView title;
        public TextView answer;
    }
}
