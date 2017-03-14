package com.project.zeng.bookstore.ui.frgm;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zeng.bookstore.R;

/**
 * Created by zeng on 2017/2/28.
 */

public class CartFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, OnClickListener{

    //组件
    private ListView mCartListView;
    private CheckBox mCheckBtn;
    private TextView mTotalPriceTxtView;
    private TextView mSettleTxtView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        init(view);
        initListener();
        return view;
    }

    /**
     * 初始化组件
     * @param view
     */
    private void init(View view){
        mCartListView = (ListView)view.findViewById(R.id.lv_cart);
        mCheckBtn = (CheckBox) view.findViewById(R.id.cb_cart_select_all);
        mTotalPriceTxtView = (TextView)view.findViewById(R.id.tv_cart_total_price);
        mSettleTxtView = (TextView)view.findViewById(R.id.tv_cart_settle);
    }

    /**
     * 初始化事件
     */
    private void initListener(){
        mCheckBtn.setOnCheckedChangeListener(this);
        mSettleTxtView.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cart_settle:
                Toast.makeText(getActivity().getApplication(), "结算", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
