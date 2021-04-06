package org.turings.investigationapplicqation.DialogAdapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.turings.investigationapplicqation.Entity.Question;
import org.turings.investigationapplicqation.R;

import java.util.List;

import androidx.annotation.Nullable;

//题库Adapter
public class QuestionBankAdapter extends BaseQuickAdapter<Question, BaseViewHolder> {

    private static final int STATE_DEFAULT = 0;//默认状态
    int mEditMode = STATE_DEFAULT;

    public QuestionBankAdapter(int layoutResId, @Nullable List<Question> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Question item) {
        helper.setText(R.id.tv_type, "["+item.getType()+"]");
        helper.setText(R.id.tv_video_info, item.getTitle());
        helper.addOnClickListener(R.id.iv_check);//添加item点击事件
        helper.addOnLongClickListener(R.id.item_data);//长按事件
        if (mEditMode == STATE_DEFAULT) {
            //默认不显示
            helper.getView(R.id.iv_check).setVisibility(View.GONE);
        } else {
            //显示   显示之后再做点击之后的判断
            helper.getView(R.id.iv_check).setVisibility(View.VISIBLE);

            if (item.isSelect()) {//点击时，true 选中
                helper.getView(R.id.iv_check).setBackgroundResource(R.mipmap.icon_choose_selected);
            } else {//false 取消选中
                helper.getView(R.id.iv_check).setBackgroundResource(R.mipmap.icon_choose_default);
            }
        }
    }

    /**
     * 设置编辑状态   接收Activity中传递的值，并改变Adapter的状态
     */
    public void setEditMode(int editMode) {
        mEditMode = editMode;
        notifyDataSetChanged();//刷新
    }


}
