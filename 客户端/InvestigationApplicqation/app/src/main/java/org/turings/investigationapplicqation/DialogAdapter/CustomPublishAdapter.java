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
import org.turings.investigationapplicqation.PreViewActivity;
import org.turings.investigationapplicqation.R;
import org.turings.investigationapplicqation.ReleaseActivity;

import java.util.List;
//发布的问卷的Adapter
public class CustomPublishAdapter extends BaseAdapter {
    private List<Questionnaire> list;//数据源
    private Context context;//上下文环境
    private int itemLayout;//布局文件
    public ViewHolder holder;

    public CustomPublishAdapter(List<Questionnaire> list, Context context, int itemLayout) {
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
            view = LayoutInflater.from(context).inflate(R.layout.published_project_item_layout,null);
            holder.title = view.findViewById(R.id.title);
            holder.instruction = view.findViewById(R.id.instruction);
            holder.check = view.findViewById(R.id.check);
            holder.share = view.findViewById(R.id.share);
            holder.sum = view.findViewById(R.id.sum);
            holder.del = view.findViewById(R.id.del);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.title.setText(list.get(listPosition).getTitle());
        holder.instruction.setText(list.get(listPosition).getInstructions());
        //查看
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(context, PreViewActivity.class);
                intent3.putExtra("q_data", list.get(listPosition));
                context.startActivity(intent3);
            }
        });
        //分享
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten = new Intent(context, ReleaseActivity.class);
                inten.putExtra("url","http://192.168.10.223:8080/WorkProject/ylx/preview/"+list.get(listPosition).getId());
                inten.putExtra("uId",list.get(listPosition).getId()+"");
                context.startActivity(inten);
            }
        });
        //统计
        holder.sum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.i("www", "onClick:点击编辑事件 ");
//                Intent intent = new Intent(context, EditQuestionnaire.class);
//                intent.putExtra("questionnaire_data", list.get(listPosition));
//                context.startActivity(intent);
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
        private LinearLayout check;//查看
        private LinearLayout share;//分享
        private LinearLayout sum;//统计
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
