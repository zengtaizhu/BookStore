package com.project.zeng.bookstore.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by zeng on 2017/3/19.
 */

public class MyExpandableListView extends ExpandableListView{

    public MyExpandableListView(Context context) {
        super(context);
    }

    public MyExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //隐藏滚动条
        int expandSpec =  MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
