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
import org.turings.investigationapplicqation.R;

import java.util.List;

public class CustomOptionsAdapter extends BaseAdapter {
    private List<Options> list;//数据源
    private Context context;//上下文环境
    private int itemLayout;//布局文件
    public ViewHolder holder;
    public CustomOptionsAdapter(List<Options> list, Context context, int itemLayout) {
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
            holder.content = view.findViewById(R.id.content);
            holder.img = view.findViewById(R.id.img);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.content.setText(list.get(listPosition).getContent());
        if(list.get(listPosition).getImg().equals("sr")){
            int icon = context.getResources().getIdentifier("sr", "mipmap",context.getPackageName());
            // 设置图片
            holder.img.setImageResource(icon);
            holder.img.setVisibility(View.VISIBLE);
        }else {
            if(list.get(listPosition).getImg().isEmpty()){
                holder.img.setVisibility(View.GONE);
            }else{
                String dataFileStr = context.getFilesDir().getAbsolutePath()+"/"+list.get(listPosition).getImg();
                Bitmap bitmap = BitmapFactory.decodeFile(dataFileStr);
                //添加图片
                holder.img.setImageBitmap(bitmap);
                holder.img.setVisibility(View.VISIBLE);
            }
        }
        return view;
    }
    public final static class ViewHolder{
        public TextView content;
        public ImageView img;
    }
}
