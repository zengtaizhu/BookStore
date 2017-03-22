package com.project.zeng.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.entities.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zeng on 2017/3/21.
 * MeFragment的订单的商品的Adapter
 */

public class OrderProductAdapter extends BaseAdapter{

    private Context mContext;
    private List<Product> mProducts;

    public OrderProductAdapter(Context context, List<Product> products) {
        this.mContext = context;
        this.mProducts = products;
    }

    @Override
    public int getCount() {
        return mProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return mProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(mProducts.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(null == convertView){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order_product, parent, false);
            viewHolder.mProImgView = (ImageView) convertView.findViewById(R.id.iv_order_pro_img);
            viewHolder.mProTitleView = (TextView) convertView.findViewById(R.id.tv_order_pro_title);
            viewHolder.mProDescView = (TextView) convertView.findViewById(R.id.tv_order_pro_describe);
            viewHolder.mProPriceView = (TextView) convertView.findViewById(R.id.tv_order_pro_price);
            viewHolder.mProCountView = (TextView) convertView.findViewById(R.id.tv_order_pro_count);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Product item = mProducts.get(position);
        Picasso.with(mContext).load(item.getPictureUrl()).fit().into(viewHolder.mProImgView);
        viewHolder.mProTitleView.setText(item.getTitle());
        viewHolder.mProDescView.setText(item.getDescribe());
        viewHolder.mProPriceView.setText("￥" + item.getPrice());
        viewHolder.mProCountView.setText("×" + item.getCount());
        return convertView;
    }

    /**
     * 订单商品的ViewHolder
     */
    private static class ViewHolder{
        private ImageView mProImgView;
        private TextView mProTitleView;
        private TextView mProDescView;
        private TextView mProPriceView;
        private TextView mProCountView;
    }
}
