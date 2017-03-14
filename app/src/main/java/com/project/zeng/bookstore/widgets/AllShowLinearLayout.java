package com.project.zeng.bookstore.widgets;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by zeng on 2017/3/2.
 */

public class AllShowLinearLayout extends LinearLayout {

    //停留在顶部的View
    private View mTopView;
    private ViewStateChangeListener mViewStateListener;

    @Override
    public void computeScroll() {
        super.computeScroll();
    }

    public AllShowLinearLayout(Context context) {
        super(context);
    }

    /**
     * 回调接口，用于显示或隐藏View
     */
    public interface ViewStateChangeListener{
        public void onViewShow();
        public void onViewGone();
    }
}
