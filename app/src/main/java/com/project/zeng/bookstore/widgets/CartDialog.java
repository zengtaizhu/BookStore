package com.project.zeng.bookstore.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.entities.Product;
import com.squareup.picasso.Picasso;

/**
 * Created by zeng on 2017/3/30.
 * 添加商品到购物车的Dialog
 */

public class CartDialog extends Dialog implements OnClickListener{

    private Context mContext;
    //组件
    private ImageView proImgView;
    private TextView proPriceView;
    private TextView proCountView;
    private ImageView countMinusView;
    private ImageView countPlusView;
    private EditText countView;
    private TextView commitView;

    private OnDialogClickListener listener;

    private Product mProduct;//商品
    public int count = 1;//购买或添加到购物车的数量

    public CartDialog(Context context, Product product, OnDialogClickListener listener) {
        super(context);
        mContext = context;
        mProduct = product;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_cart);
        init();
    }

    /**
     * 初始化组件
     */
    private void init(){
        proImgView = (ImageView) findViewById(R.id.iv_add_cart_pro_img);
        proPriceView = (TextView) findViewById(R.id.tv_add_cart_pro_price);
        proCountView = (TextView) findViewById(R.id.tv_add_cart_pro_count);
        countMinusView = (ImageView) findViewById(R.id.iv_add_cart_pro_minus);
        countPlusView = (ImageView) findViewById(R.id.iv_add_cart_pro_plus);
        countView = (EditText) findViewById(R.id.et_add_cart_pro_count);
        commitView = (TextView) findViewById(R.id.tv_add_cart_commit);
        Picasso.with(mContext).load(mProduct.getPictureUrl()).fit().into(proImgView);
        proPriceView.setText("￥" + mProduct.getPrice());
        proCountView.setText("库存" + mProduct.getCount() + "件");
        countMinusView.setOnClickListener(this);
        countPlusView.setOnClickListener(this);
        commitView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_add_cart_pro_minus:
                count = Integer.valueOf(countView.getText().toString().trim());
                if(count > 1){//数量为1时，不能减少
                    count--;
                    countView.setText(count + "");
                }
                break;
            case R.id.iv_add_cart_pro_plus:
                count = Integer.valueOf(countView.getText().toString().trim());
                if(count < mProduct.getCount()){//数量为库存时，不能增加
                    count++;
                    countView.setText(count + "");
                }
                break;
            case R.id.tv_add_cart_commit:
                listener.add(count);
                this.dismiss();
                break;
        }
    }

    //回调接口，用于点击确认
    public interface OnDialogClickListener{
        void add(int count);
    }
}
