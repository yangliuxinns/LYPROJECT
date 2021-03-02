package org.turings.investigationapplicqation.DialogAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import org.turings.investigationapplicqation.Entity.Question;
import org.turings.investigationapplicqation.Entity.QuestionResult;
import org.turings.investigationapplicqation.R;
import org.turings.investigationapplicqation.Util.CustomListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomShowTopicAdapter extends BaseAdapter {

    private List<Question> list;//数据源
    private Context context;//上下文环境
    private int itemLayout;//布局文件
    private List<QuestionResult> qs = new ArrayList<>();//结果
    public ViewHolder holder;

    public CustomShowTopicAdapter(List<Question> list, Context context, int itemLayout) {
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
            holder.list_choose = view.findViewById(R.id.list_choose);
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
            HashMap<String, Boolean> optionStates = new HashMap<String, Boolean>();
            final SingleCheckAdapter singleCheckAdapter1 = new SingleCheckAdapter(view.getContext(),list.get(listPosition).getOptions(),optionStates);
            holder.list_choose.setAdapter(singleCheckAdapter1);
            holder.list_choose.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            holder.list_choose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    EditText editText = view.findViewById(R.id.et_title);
                    Log.v("----hxm", "您选择的是:" + list.get(listPosition).getOptions().get(position).getContent());
                    RadioButton radioButton = (RadioButton) view.findViewById(R.id.radiobuttonlist_singleChoiceButton);
                    //每次选择一个item时都要清除所有的状态，防止出现多个被选中
                    singleCheckAdapter1.clearStates(position);
                    radioButton.setChecked(singleCheckAdapter1.getStates(position));
                    //刷新数据，调用getView刷新ListView
                    singleCheckAdapter1.notifyDataSetChanged();
                    boolean flag = false;
                    int post=0;
                    for(int i=0;i<qs.size();i++){
                        if(qs.get(i).getOrder() == list.get(listPosition).getOrder()){
                            flag = true;
                            post = i;
                        }else {
                            flag = false;
                        }
                    }
                    if(flag){
                        if(list.get(listPosition).getOptions().get(position).getImg().equals("sr")){
                            qs.get(post).getResult().put(position,editText.getText().toString().trim());
                        }else {
                            qs.get(post).getResult().put(position,list.get(listPosition).getOptions().get(position).getContent());
                        }
                    }else {
                        Map<Integer,String> result = new HashMap<>();
                        if(list.get(listPosition).getOptions().get(position).getImg().equals("sr")){
                            result.put(position,editText.getText().toString().trim());
                        }else {
                            result.put(position,list.get(listPosition).getOptions().get(position).getContent());
                        }
                        QuestionResult questionResult = new QuestionResult(list.get(listPosition).getOrder(),result);
                        qs.add(questionResult);
                    }
                }
            });
        }else if( list.get(listPosition).getType().equals("多选题")){
            JudgeTypeOfQuestion("多选题");
//            MoreCheckAdapter singleCheckAdapter = new MoreCheckAdapter(view.getContext(),list.get(listPosition).getOptions());
//            holder.list_choose.setAdapter(singleCheckAdapter);
//            holder.list_choose.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//            holder.list_choose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Log.v("----hxm", "您选择的是:" + list.get(listPosition).getOptions().get(position).getContent());
//                    boolean flag = false;
//                    int post=0;
//                    if(qs!=null){
//                        for(int i=0;i<qs.size();i++){
//                            if(qs.get(i).getOrder() == list.get(listPosition).getOrder()){
//                                flag = true;
//                                post = i;
//                            }
//                        }
//                        if(flag){
//                            //添加选项是否重复
//                            if(qs.get(post).getResult().containsKey(position)){
//                                //重复
//                                qs.get(post).getResult().remove(position);
//                            }else {
//                                //不重复
//                                qs.get(post).getResult().put(position,list.get(listPosition).getOptions().get(position).getContent());
//                            }
//                        }else {
//                            Map<Integer,String> result = new HashMap<>();
//                            result.put(position,list.get(listPosition).getOptions().get(position).getContent());
//                            QuestionResult questionResult = new QuestionResult(list.get(listPosition).getOrder(),result);
//                            qs.add(questionResult);
//                        }
//                    }else {
//                        Map<Integer,String> result = new HashMap<>();
//                        result.put(position,list.get(listPosition).getOptions().get(position).getContent());
//                        QuestionResult questionResult = new QuestionResult(list.get(listPosition).getOrder(),result);
//                        qs.add(questionResult);
//                    }
//
//                }
//            });
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
            HashMap<String, Boolean> optionStates = new HashMap<String, Boolean>();
            final SingleCheckAdapter singleCheckAdapter2 = new SingleCheckAdapter(view.getContext(),list.get(listPosition).getOptions(),optionStates);
            holder.list_choose.setAdapter(singleCheckAdapter2);
            holder.list_choose.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            holder.list_choose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.v("----hxm", "您选择的是:" + list.get(listPosition).getOptions().get(position).getContent());
                    RadioButton radioButton = (RadioButton) view.findViewById(R.id.radiobuttonlist_singleChoiceButton);
                    //每次选择一个item时都要清除所有的状态，防止出现多个被选中
                    singleCheckAdapter2.clearStates(position);
                    radioButton.setChecked(singleCheckAdapter2.getStates(position));
                    //刷新数据，调用getView刷新ListView
                    singleCheckAdapter2.notifyDataSetChanged();
                    boolean flag = false;
                    int post=0;
                    for(int i=0;i<qs.size();i++){
                        if(qs.get(i).getOrder() == list.get(listPosition).getOrder()){
                            flag = true;
                            post = i;
                        }else {
                            flag = false;
                        }
                    }
                    if(flag){
                        qs.get(post).getResult().put(position,list.get(listPosition).getOptions().get(position).getContent());
                    }else {
                        Map<Integer,String> result = new HashMap<>();
                        result.put(position,list.get(listPosition).getOptions().get(position).getContent());
                        QuestionResult questionResult = new QuestionResult(list.get(listPosition).getOrder(),result);
                        qs.add(questionResult);
                    }
                }
            });
        } else if(list.get(listPosition).getType().equals("姓名")){
            JudgeTypeOfQuestion("姓名");
        }
        //题干
        holder.text.setText(list.get(i).getOrder()+"."+list.get(listPosition).getTitle());
        Log.i("www", "getView: "+list.get(listPosition).getOrder()+"."+list.get(listPosition).getTitle()+list.get(listPosition).toString());
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
        public CustomListView list_choose;//选项

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
                holder.text.setVisibility(View.VISIBLE);
                break;
            case "多选题":
                holder.blanks.setVisibility(View.GONE);
                holder.phone.setVisibility(View.GONE);
                holder.list_choose.setVisibility(View.VISIBLE);
                holder.calendar.setVisibility(View.GONE);
                holder.position.setVisibility(View.GONE);
                holder.city.setVisibility(View.GONE);
                holder.text.setVisibility(View.VISIBLE);
                break;
            case "填空题":
                holder.blanks.setVisibility(View.VISIBLE);
                holder.phone.setVisibility(View.GONE);
                holder.list_choose.setVisibility(View.GONE);
                holder.calendar.setVisibility(View.GONE);
                holder.position.setVisibility(View.GONE);
                holder.city.setVisibility(View.GONE);
                holder.text.setVisibility(View.VISIBLE);
                break;
            case "手机":
                holder.blanks.setVisibility(View.GONE);
                holder.phone.setVisibility(View.VISIBLE);
                holder.list_choose.setVisibility(View.GONE);
                holder.calendar.setVisibility(View.GONE);
                holder.position.setVisibility(View.GONE);
                holder.city.setVisibility(View.GONE);
                holder.text.setVisibility(View.VISIBLE);
                break;
            case "日期":
                holder.blanks.setVisibility(View.GONE);
                holder.phone.setVisibility(View.GONE);
                holder.list_choose.setVisibility(View.GONE);
                holder.calendar.setVisibility(View.VISIBLE);
                holder.position.setVisibility(View.GONE);
                holder.city.setVisibility(View.GONE);
                holder.text.setVisibility(View.VISIBLE);
                break;
            case "地图":
                holder.blanks.setVisibility(View.GONE);
                holder.phone.setVisibility(View.GONE);
                holder.list_choose.setVisibility(View.GONE);
                holder.calendar.setVisibility(View.GONE);
                holder.position.setVisibility(View.VISIBLE);
                holder.city.setVisibility(View.GONE);
                holder.text.setVisibility(View.VISIBLE);
                break;
            case "地区":
                holder.blanks.setVisibility(View.GONE);
                holder.phone.setVisibility(View.GONE);
                holder.list_choose.setVisibility(View.GONE);
                holder.calendar.setVisibility(View.GONE);
                holder.position.setVisibility(View.GONE);
                holder.city.setVisibility(View.VISIBLE);
                holder.text.setVisibility(View.VISIBLE);
                break;
            case "性别":
                holder.blanks.setVisibility(View.GONE);
                holder.phone.setVisibility(View.GONE);
                holder.list_choose.setVisibility(View.VISIBLE);
                holder.calendar.setVisibility(View.GONE);
                holder.position.setVisibility(View.GONE);
                holder.city.setVisibility(View.GONE);
                holder.text.setVisibility(View.VISIBLE);
                break;
            case "姓名":
                holder.blanks.setVisibility(View.VISIBLE);
                holder.phone.setVisibility(View.GONE);
                holder.list_choose.setVisibility(View.GONE);
                holder.calendar.setVisibility(View.GONE);
                holder.position.setVisibility(View.GONE);
                holder.city.setVisibility(View.GONE);
                holder.text.setVisibility(View.VISIBLE);
                break;
        }

    }

    public List<QuestionResult> getQuestionResult(){
        return qs;
    }

}
