package org.turings.investigationapplicqation.DialogAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import org.turings.investigationapplicqation.Entity.Options;
import org.turings.investigationapplicqation.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SingleCheckAdapter extends BaseAdapter {
    private List<Options> list;
    private Context mContext;
    private ArrayList<String> resultList = new ArrayList<>();

    private HashMap<String,Boolean> rButtonStates = new HashMap<String,Boolean>();

    public SingleCheckAdapter(Context context, List<Options> list,HashMap<String, Boolean> states) {
        this.list = list;
        this.mContext = context;
        this.rButtonStates = states;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.single_choose_item, null);
            holder.textView = convertView.findViewById(R.id.tv_option);
            holder.radioButton = convertView.findViewById(R.id.radiobuttonlist_singleChoiceButton);
            holder.img = convertView.findViewById(R.id.img);
            holder.editText = convertView.findViewById(R.id.et_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(list.get(position).getContent());
        if(list.get(position).getImg().equals("sr")){
            if(getStates(position) == null || getStates(position) == false){
                holder.editText.setVisibility(View.GONE);
            }else {
                holder.editText.setVisibility(View.VISIBLE);
            }
        }else {
            if(list.get(position).getImg().isEmpty()){
                holder.img.setVisibility(View.GONE);
            }else{
                String dataFileStr = mContext.getFilesDir().getAbsolutePath()+"/"+list.get(position).getImg();
                Bitmap bitmap = BitmapFactory.decodeFile(dataFileStr);
                //添加图片
                holder.img.setImageBitmap(bitmap);
                holder.img.setVisibility(View.VISIBLE);
            }
        }

        boolean res = false;
        if(getStates(position) == null || getStates(position) == false)//判断当前位置的radiobutton点击状态
        {
            res = false;
            setStates(position, false);
        }else{
            res = true;
        }
        holder.radioButton.setChecked(res);
        return convertView;

    }

    //用于在activity中重置所有的radiobutton的状态
    public void clearStates(int position){
        // 重置，确保最多只有一项被选中
        for(String key:rButtonStates.keySet()){
            rButtonStates.put(key, false);
        }
        rButtonStates.put(String.valueOf(position), true);
    }
    //用于获取状态值
    public Boolean getStates(int position){
        return rButtonStates.get(String.valueOf(position));
    }
    //设置状态值
    public void setStates(int position, boolean isChecked){
        rButtonStates.put(String.valueOf(position), false);
    }

    public static class ViewHolder {
        private TextView textView;
        private RadioButton radioButton;
        private ImageView img;
        private EditText editText;
    }


}
