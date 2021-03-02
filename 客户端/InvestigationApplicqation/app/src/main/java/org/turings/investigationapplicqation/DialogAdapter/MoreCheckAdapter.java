package org.turings.investigationapplicqation.DialogAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import org.turings.investigationapplicqation.Entity.Options;
import org.turings.investigationapplicqation.R;

import java.util.ArrayList;
import java.util.List;

public class MoreCheckAdapter extends BaseAdapter {
    private List<Options> list;
    private Context mContext;
    private ArrayList<String> resultList = new ArrayList<>();

    public MoreCheckAdapter(Context context,List<Options> list) {
        this.list = list;
        this.mContext = context;
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
        final MoreCheckAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new MoreCheckAdapter.ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.more_choose_item,
                    null);
            holder.img = convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        } else {
            holder = (MoreCheckAdapter.ViewHolder) convertView.getTag();
        }

        if(list.get(position).getImg().equals("sr")){
            int icon = mContext.getResources().getIdentifier("sr", "mipmap",mContext.getPackageName());
            // 设置图片
            holder.img.setImageResource(icon);
            holder.img.setVisibility(View.VISIBLE);
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
        return convertView;

    }

    public static class ViewHolder {
        ImageView img;
    }


}
