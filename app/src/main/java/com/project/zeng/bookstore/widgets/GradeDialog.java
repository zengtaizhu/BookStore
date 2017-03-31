package com.project.zeng.bookstore.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zeng.bookstore.R;

import java.util.List;

/**
 * Created by zeng on 2017/3/31.
 * ProductsActivity的筛选Dialog
 */

public class GradeDialog extends Dialog{

    private Context mContext;
    //组件
    private MyListView mGradeListView;
    private List<String> grades;//年级

    private OnGradeClickListener mListener;

    public GradeDialog(Context context, List<String> grade, OnGradeClickListener listener) {
        super(context);
        mContext = context;
        grades = grade;
        mListener = listener;
    }

    public GradeDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_pro_filter);
        mGradeListView = (MyListView) findViewById(R.id.lv_pro_grade);
        mGradeListView.setAdapter(new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, grades));
        mGradeListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);//设置多选
        mGradeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.filterProducts(grades.get(position));
                dismiss();
            }
        });
    }

    /**
     * 回调接口，用于处理选择年级
     */
    public interface OnGradeClickListener{
        void filterProducts(String name);
    }
}
