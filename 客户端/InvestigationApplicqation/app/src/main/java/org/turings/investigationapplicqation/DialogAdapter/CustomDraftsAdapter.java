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
import org.turings.investigationapplicqation.Entity.Question;
import org.turings.investigationapplicqation.Entity.Questionnaire;
import org.turings.investigationapplicqation.R;
import org.turings.investigationapplicqation.ReleaseActivity;

import java.util.ArrayList;
import java.util.List;
//草稿箱子问卷Adapter
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
        holder.pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发布问卷
                Intent inten = new Intent(context, ReleaseActivity.class);
                inten.putExtra("url","http://192.168.137.1:8080/WorkProject/ylx/preInvestigation/"+list.get(listPosition).getId());
                inten.putExtra("uId",list.get(listPosition).getId()+"");
                context.startActivity(inten);
            }
        });
        holder.lyEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditQuestionnaire.class);
                List<Question> qs = new ArrayList<>();
                for(Question qu:list.get(listPosition).getList()){
                    if(qu.getOptions() != null) {
                        for (int i = 0; i < qu.getOptions().size(); i++) {
                            if (!qu.getOptions().get(i).getImg().equals("sr") || qu.getOptions().get(i).getImg().equals("") || qu.getOptions().get(i).getImg().isEmpty()) {
                                qu.getOptions().get(i).setImgcontent(null);
                            }
                        }
                        qs.add(qu);
                    }
                }
                list.get(listPosition).setList(qs);
                //查看是否分页
                Questionnaire questionnaire = list.get(listPosition);
                if(questionnaire.getTotalPage() != 0){
                    if(questionnaire.getTotalPage() == 2){
                        Question question1 = new Question(0,0,"","分页",null,false,1);
                        questionnaire.getList().add(0,question1);
                        //尾部
                        int n = 0;
                        for(int k=0;k<questionnaire.getList().size();k++){
                            if(questionnaire.getList().get(k).getPageNumber() == 2){
                                n++;
                                Question question2 = new Question(0,0,"","分页",null,false,2);
                                questionnaire.getList().add(k,question2);
                                break;
                            }
                        }
                        if(n==0){
                            Question question2 = new Question(0,0,"","分页",null,false,2);
                            questionnaire.getList().add(question2);
                        }
                    }else {
                        //不止一页
                        for(int i = 0;i<questionnaire.getTotalPage();i++){
                            if(i==0){
                                Question question1 = new Question(0,0,"","分页",null,false,1);
                                questionnaire.getList().add(0,question1);
                            }else {
                                //尾部
                                int n = 0;
                                for(int k=0;k<questionnaire.getList().size();k++){
                                    if(questionnaire.getList().get(k).getPageNumber() == (i+1)){
                                        n++;
                                        Question question2 = new Question(0,0,"","分页",null,false,i+1);
                                        questionnaire.getList().add(k,question2);
                                        break;
                                    }
                                }
                                if(n==0){
                                    Question question2 = new Question(0,0,"","分页",null,false,i+1);
                                    questionnaire.getList().add(question2);
                                }
                            }
                        }
                    }

                }
                intent.putExtra("questionnaire_data",questionnaire);
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
