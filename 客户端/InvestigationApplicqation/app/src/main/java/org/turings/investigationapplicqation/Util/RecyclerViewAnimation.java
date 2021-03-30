package org.turings.investigationapplicqation.Util;

import android.content.Context;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import org.turings.investigationapplicqation.R;

import androidx.recyclerview.widget.RecyclerView;


/**
 * RecyclerView动画
 */

public class RecyclerViewAnimation {

    //数据变化时显示动画
    public static void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
