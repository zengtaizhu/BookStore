package com.project.zeng.bookstore.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.entities.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeng on 2017/3/3.（注：参考自http://blog.csdn.net/u013718120/article/details/51965678）
 * 商品列表的Adapter
 */

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.BaseViewHolder>{

    private int mType = 0;//布局类型，0：LinearLayout，1：GridView
    private List<Product> mProducts;//商品数据
    private Context mContext;
    private LayoutInflater mInflater;
    public MyItemClickListener mListener;

    public ProductRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        mProducts = new ArrayList<>();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView;
        if(0 == mType){
            rootView = mInflater.inflate(R.layout.item_pro_linearlayout, null, false);
            LinearViewHolder linearViewHolder = new LinearViewHolder(rootView, mListener);
            return linearViewHolder;
        }else{
            rootView = mInflater.inflate(R.layout.item_pro_gridlayout, null, false);
            GridViewHolder gridViewHolder = new GridViewHolder(rootView, mListener);
            return gridViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if(0 == mType){
            LinearViewHolder linearViewHolder = (LinearViewHolder)holder;
            Picasso.with(mContext).load(mProducts.get(position).getPictureUrl()).fit()
                    .into(linearViewHolder.mProImageView);
            linearViewHolder.mProImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            linearViewHolder.mProTitleTv.setText(mProducts.get(position).getTitle());
            linearViewHolder.mProAuthorTv.setText(mProducts.get(position).getAuthor());
            linearViewHolder.mProPriceTv.setText(String.valueOf(mProducts.get(position).getPrice()));
        }else{
            GridViewHolder gridViewHolder = (GridViewHolder)holder;
            Picasso.with(mContext).load(mProducts.get(position).getPictureUrl()).fit()
                    .into(gridViewHolder.mProImageView);
            gridViewHolder.mProImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            gridViewHolder.mProTitleTv.setText(mProducts.get(position).getTitle());
            gridViewHolder.mProAuthorTv.setText(mProducts.get(position).getAuthor());
            gridViewHolder.mProPriceTv.setText(String.valueOf(mProducts.get(position).getPrice()));
        }
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mType;
    }

    public void setType(int type){
        this.mType = type;
    }

    public void setItemClickListener(MyItemClickListener listener) {
        this.mListener = listener;
    }

    /**
     * 更新数据
     * @param products
     */
    public void updateData(List<Product> products){
        mProducts = products;
//        mProducts.addAll(products);//增加数据量
        notifyDataSetChanged();
    }

    public interface MyItemClickListener{
        void onItemClick(View view, int position);
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder{

        public MyItemClickListener mListener;
        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 商品线性布局的ViewHolder
     */
    public static class LinearViewHolder extends BaseViewHolder implements OnClickListener{

        private ImageView mProImageView;
        private TextView mProTitleTv;
        private TextView mProAuthorTv;
        private TextView mProPriceTv;

        public LinearViewHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            mProImageView = (ImageView) itemView.findViewById(R.id.iv_pro_img);
            mProTitleTv = (TextView) itemView.findViewById(R.id.tv_pro_title);
            mProAuthorTv = (TextView) itemView.findViewById(R.id.tv_pro_author);
            mProPriceTv = (TextView) itemView.findViewById(R.id.tv_pro_price);
            mListener = listener;
            itemView.setOnClickListener(this);
        }

        /**
         * 点击监听
         * @param v
         */
        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    /**
     * 商品GridView布局的ViewHolder
     */
    public static class GridViewHolder extends BaseViewHolder implements OnClickListener{

        private ImageView mProImageView;
        private TextView mProTitleTv;
        private TextView mProAuthorTv;
        private TextView mProPriceTv;

        public GridViewHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            mProImageView = (ImageView) itemView.findViewById(R.id.iv_pro_img);
            mProTitleTv = (TextView) itemView.findViewById(R.id.tv_pro_title);
            mProAuthorTv = (TextView) itemView.findViewById(R.id.tv_pro_author);
            mProPriceTv = (TextView) itemView.findViewById(R.id.tv_pro_price);
            mListener = listener;
            itemView.setOnClickListener(this);
        }

        /**
         * 点击监听
         * @param v
         */
        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
