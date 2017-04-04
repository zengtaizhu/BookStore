package com.project.zeng.bookstore.ui.frgm;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.MyApplication;
import com.project.zeng.bookstore.adapter.OrderAdapter;
import com.project.zeng.bookstore.entities.Order;
import com.project.zeng.bookstore.entities.Result;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.OrderAPI;
import com.project.zeng.bookstore.net.impl.OrderAPIImpl;

import java.util.List;

/**
 * Created by zeng on 2017/3/21.
 * MeFragment的订单Fragment
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class OrderFragment extends Fragment{

    //全局变量
    private MyApplication app;
    private Context mContext;
    //组件
    private ListView mOrdersView;
    //适配器
    private OrderAdapter mOrderAdapter;

    //获得订单的网络请求API
    OrderAPI mOrderAPI = new OrderAPIImpl();
    private int mState;//当前的订单状态

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        mContext = getActivity().getApplication();
        app = (MyApplication) mContext;
//        Log.e("OrderFragment", "token=" + app.getToken());
        mOrdersView = (ListView) view.findViewById(R.id.lv_order);
        init();
        return view;
    }

    /**
     * 初始化事件及其适配器
     */
    private void init() {
        mOrderAdapter = new OrderAdapter(mContext, app.getToken());
        mOrderAdapter.setCommentListener(new OrderAdapter.OnCommentClick() {
            @Override
            public void comment(final Order item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("订单评价");
                View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_order_comment, null);
                final EditText mCommentView = (EditText) view.findViewById(R.id.et_order_comment_dialog);
                builder.setView(view);
                builder.setPositiveButton("提交评价", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String comment = mCommentView.getText().toString().trim();
//                        Log.e("OrderFragment", "comment=" + comment);
                        if(comment.equals("")){
                            comment = "好评";//默认：好评
                        }
                        item.setComment(comment);
                        mOrderAPI.commentOrder(app.getToken(), item.getId(), comment, new DataListener<Result>() {
                            @Override
                            public void onComplete(Result result) {
                                if(result.getResult().equals("success")){
                                    fetchData(mState);
                                }else{
                                    Toast.makeText(mContext, result.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                builder.show();
            }
        });
        mOrdersView.setAdapter(mOrderAdapter);
    }

    /**
     * 根据state的值，获取相应状态的订单
     */
    public void fetchData(int state) {
        mState = state;
        if(app == null || app.getToken().equals("")){
//            Toast.makeText(mContext, "请先登录!", Toast.LENGTH_SHORT).show();
            return;
        }
        mOrderAPI.fetchOrdersByState(app.getToken(), state, new DataListener<List<Order>>() {
            @Override
            public void onComplete(List<Order> result) {
                if(null != result){
//                            Log.e("OrderFragment", "从网络加载的Order的数量为：" + result.size());
                    mOrderAdapter.updateData(result);
                }
            }
        });
    }
}
