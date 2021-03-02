package org.turings.investigationapplicqation.DialogAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.turings.investigationapplicqation.Entity.Question;
import org.turings.investigationapplicqation.R;
import org.turings.investigationapplicqation.Util.CustomListView;
import org.turings.investigationapplicqation.Util.ListViewUtil;

import java.util.List;

public class CustomQuestionnaireAdapter extends BaseAdapter {
    private List<Question> list;//数据源
    private Context context;//上下文环境
    private int itemLayout;//布局文件
    public  ViewHolder holder;

    public CustomQuestionnaireAdapter(List<Question> list, Context context, int itemLayout) {
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
            holder.text = view.findViewById(R.id.title2);
            holder.iv_must = view.findViewById(R.id.iv_must);
            holder.blanks = view.findViewById(R.id.blanks);
            holder.phone = view.findViewById(R.id.phone);
            holder.calendar = view.findViewById(R.id.calendar);
            holder.position = view.findViewById(R.id.position);
            holder.city = view.findViewById(R.id.city);
            holder.peddle = view.findViewById(R.id.peddle);
            holder.list_choose = view.findViewById(R.id.list_choose);
            holder.top_ylx = view.findViewById(R.id.top_ylx);
            holder.down_ylx = view.findViewById(R.id.down_ylx);
            holder.cart_ylx = view.findViewById(R.id.cart_ylx);
            holder.tv_peddle = view.findViewById(R.id.tv_peddle);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        //是否必填
        if(list.get(listPosition).getRequired()){
            holder.iv_must.setVisibility(View.VISIBLE);
        }else {
            holder.iv_must.setVisibility(View.INVISIBLE);
        }
        if(list.get(listPosition).getType().equals("单选题")){
            JudgeTypeOfQuestion("单选题");
            CustomOptionsAdapter customOptionsAdapter = new CustomOptionsAdapter(list.get(listPosition).getOptions(),view.getContext(),R.layout.options_item);
            holder.list_choose.setAdapter(customOptionsAdapter);
        }else if( list.get(listPosition).getType().equals("多选题")){
            JudgeTypeOfQuestion("多选题");
            CustomOptionsAdapter customOptionsAdapter = new CustomOptionsAdapter(list.get(listPosition).getOptions(),view.getContext(),R.layout.options_item);
            holder.list_choose.setAdapter(customOptionsAdapter);
        }else if(list.get(listPosition).getType().equals("填空题")){
            JudgeTypeOfQuestion("填空题");
        }else if(list.get(listPosition).getType().equals("手机")){
            JudgeTypeOfQuestion("手机");
        }else if(list.get(listPosition).getType().equals("日期")){
            JudgeTypeOfQuestion("日期");
        }else if(list.get(listPosition).getType().equals("地图")){
            JudgeTypeOfQuestion("地图");
        }else if(list.get(listPosition).getType().equals("地区")){
            JudgeTypeOfQuestion("地区");
        }else if(list.get(listPosition).getType().equals("性别")){
            JudgeTypeOfQuestion("性别");
            CustomOptionsAdapter customOptionsAdapter = new CustomOptionsAdapter(list.get(listPosition).getOptions(),view.getContext(),R.layout.options_item);
            holder.list_choose.setAdapter(customOptionsAdapter);
        }else if(list.get(listPosition).getType().equals("分页")){
            JudgeTypeOfQuestion("分页");
            holder.tv_peddle.setText("第"+list.get(listPosition).getPageNumber()+"页");
        }else if(list.get(listPosition).getType().equals("姓名")){
            JudgeTypeOfQuestion("姓名");
        }
        //题干
        holder.text.setText(list.get(i).getOrder()+"."+list.get(listPosition).getTitle());
        Log.i("www", "getView: "+list.get(listPosition).getOrder()+"."+list.get(listPosition).getTitle()+list.get(listPosition).toString());
        holder.cart_ylx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//删除
                mOnItemDeleteListener.onDeleteClick(listPosition);
            }
        });
        //上移
        holder.top_ylx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemTopMoveListener.onTopMoveClick(listPosition);
            }
        });
        //下移
        holder.down_ylx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemDownMoveListener.onDownMoveClick(listPosition);
            }
        });
        return view;
    }


    public final static class ViewHolder{
        public ImageView iv_must;//是否必填
        public TextView text;
        public LinearLayout blanks;//填空
        public LinearLayout phone;//手机号
        public LinearLayout calendar;//选时间
        public LinearLayout position;//位置
        public LinearLayout city;//选城市
        public LinearLayout peddle;//分页
        public CustomListView list_choose;//选项
        public LinearLayout top_ylx;//上移
        public LinearLayout down_ylx;//下移
        public LinearLayout cart_ylx;//删除
        public TextView tv_peddle;//分页

    }
    public void JudgeTypeOfQuestion(String type){
        switch (type){
            case "单选题":
                holder.blanks.setVisibility(View.GONE);
                holder.phone.setVisibility(View.GONE);
                holder.list_choose.setVisibility(View.VISIBLE);
                holder.calendar.setVisibility(View.GONE);
                holder.position.setVisibility(View.GONE);
                holder.city.setVisibility(View.GONE);
                holder.peddle.setVisibility(View.GONE);
                holder.text.setVisibility(View.VISIBLE);
                break;
            case "多选题":
                holder.blanks.setVisibility(View.GONE);
                holder.phone.setVisibility(View.GONE);
                holder.list_choose.setVisibility(View.VISIBLE);
                holder.calendar.setVisibility(View.GONE);
                holder.position.setVisibility(View.GONE);
                holder.city.setVisibility(View.GONE);
                holder.peddle.setVisibility(View.GONE);
                holder.text.setVisibility(View.VISIBLE);
                break;
            case "填空题":
                holder.blanks.setVisibility(View.VISIBLE);
                holder.phone.setVisibility(View.GONE);
                holder.list_choose.setVisibility(View.GONE);
                holder.calendar.setVisibility(View.GONE);
                holder.position.setVisibility(View.GONE);
                holder.city.setVisibility(View.GONE);
                holder.peddle.setVisibility(View.GONE);
                holder.text.setVisibility(View.VISIBLE);
                break;
            case "手机":
                holder.blanks.setVisibility(View.GONE);
                holder.phone.setVisibility(View.VISIBLE);
                holder.list_choose.setVisibility(View.GONE);
                holder.calendar.setVisibility(View.GONE);
                holder.position.setVisibility(View.GONE);
                holder.city.setVisibility(View.GONE);
                holder.peddle.setVisibility(View.GONE);
                holder.text.setVisibility(View.VISIBLE);
                break;
            case "日期":
                holder.blanks.setVisibility(View.GONE);
                holder.phone.setVisibility(View.GONE);
                holder.list_choose.setVisibility(View.GONE);
                holder.calendar.setVisibility(View.VISIBLE);
                holder.position.setVisibility(View.GONE);
                holder.city.setVisibility(View.GONE);
                holder.peddle.setVisibility(View.GONE);
                holder.text.setVisibility(View.VISIBLE);
                break;
            case "地图":
                holder.blanks.setVisibility(View.GONE);
                holder.phone.setVisibility(View.GONE);
                holder.list_choose.setVisibility(View.GONE);
                holder.calendar.setVisibility(View.GONE);
                holder.position.setVisibility(View.VISIBLE);
                holder.city.setVisibility(View.GONE);
                holder.peddle.setVisibility(View.GONE);
                holder.text.setVisibility(View.VISIBLE);
                break;
            case "地区":
                holder.blanks.setVisibility(View.GONE);
                holder.phone.setVisibility(View.GONE);
                holder.list_choose.setVisibility(View.GONE);
                holder.calendar.setVisibility(View.GONE);
                holder.position.setVisibility(View.GONE);
                holder.city.setVisibility(View.VISIBLE);
                holder.peddle.setVisibility(View.GONE);
                holder.text.setVisibility(View.VISIBLE);
                break;
            case "分页":
                holder.blanks.setVisibility(View.GONE);
                holder.phone.setVisibility(View.GONE);
                holder.list_choose.setVisibility(View.GONE);
                holder.calendar.setVisibility(View.GONE);
                holder.position.setVisibility(View.GONE);
                holder.city.setVisibility(View.GONE);
                holder.peddle.setVisibility(View.VISIBLE);
                holder.iv_must.setVisibility(View.GONE);
                holder.text.setVisibility(View.GONE);
                break;
            case "性别":
                holder.blanks.setVisibility(View.GONE);
                holder.phone.setVisibility(View.GONE);
                holder.list_choose.setVisibility(View.VISIBLE);
                holder.calendar.setVisibility(View.GONE);
                holder.position.setVisibility(View.GONE);
                holder.city.setVisibility(View.GONE);
                holder.peddle.setVisibility(View.GONE);
                holder.text.setVisibility(View.VISIBLE);
                break;
            case "姓名":
                holder.blanks.setVisibility(View.VISIBLE);
                holder.phone.setVisibility(View.GONE);
                holder.list_choose.setVisibility(View.GONE);
                holder.calendar.setVisibility(View.GONE);
                holder.position.setVisibility(View.GONE);
                holder.city.setVisibility(View.GONE);
                holder.peddle.setVisibility(View.GONE);
                holder.text.setVisibility(View.VISIBLE);
                break;
        }

    }
    /**
     * 删除按钮的监听接口
     */
    public interface onItemDeleteListener {
        void onDeleteClick(int position);
    }

    private onItemDeleteListener mOnItemDeleteListener;

    public void setmOnItemDeleteClickListener(onItemDeleteListener mOnItemDeleteListener) {
        this.mOnItemDeleteListener  = mOnItemDeleteListener;
    }
    /**
     * 上移按钮的监听接口
     */
    public interface onItemTopMoveListener {
        void onTopMoveClick(int position);
    }

    private onItemTopMoveListener mOnItemTopMoveListener;

    public void setmOnItemTopMoveClickListener(onItemTopMoveListener mOnItemTopMoveListener) {
        this.mOnItemTopMoveListener  = mOnItemTopMoveListener;
    }
    /**
     * 下移按钮的监听接口
     */
    public interface onItemDownMoveListener {
        void onDownMoveClick(int position);
    }

    private onItemDownMoveListener mOnItemDownMoveListener;

    public void setmOnItemDownMoveClickListener(onItemDownMoveListener mOnItemDownMoveListener) {
        this.mOnItemDownMoveListener  = mOnItemDownMoveListener;
    }

}
