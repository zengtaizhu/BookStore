package com.project.zeng.bookstore.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.entities.Cart;
import com.project.zeng.bookstore.entities.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by zeng on 2017/3/9.
 * 购物车的Adapter
 */

public class CartAdapter extends BaseExpandableListAdapter{

    private Context mContext;
    List<Cart> mCarts;

    public CartAdapter(Context mContext) {
        this.mContext = mContext;
        mCarts = new ArrayList<>();
    }

    @Override
    public int getGroupCount() {
        return mCarts.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(groupPosition < 0 || groupPosition >= mCarts.size()){
            return 0;
        }
        return mCarts.get(groupPosition).getProducts().size();
    }

    @Override
    public Cart getGroup(int groupPosition) {
        return mCarts.get(groupPosition);
    }

    @Override
    public Product getChild(int groupPosition, int childPosition) {
        return mCarts.get(groupPosition).getProducts().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return Integer.valueOf(mCarts.get(groupPosition).getId());
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return Integer.valueOf(mCarts.get(groupPosition).getProducts().get(childPosition).getId());
    }

    @Override
    public boolean hasStableIds() {
        //分组和子选项是否持有稳定的ID, 即底层数据的改变会不会影响到它们
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderGroup group;
        if(null == convertView){
            group = new ViewHolderGroup();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_cart, parent, false);
            group.mSelectAllCheckBox = (CheckBox)convertView.findViewById(R.id.cb_cart_select_cart);
            group.mSellerTxtView = (TextView)convertView.findViewById(R.id.tv_cart_seller);
            convertView.setTag(group);
        }else{
            group = (ViewHolderGroup) convertView.getTag();
        }
        if(mCarts.get(groupPosition).getProducts().size() > 0){
            group.mSellerTxtView.setText(mCarts.get(groupPosition).getProducts().get(0).getSellerId());
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderChild child;
        if(null == convertView){
            child = new ViewHolderChild();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_cart_product, parent, false);
            child.mProImgView = (ImageView)convertView.findViewById(R.id.iv_cart_pro_img);
            child.mProTitleView =  (TextView)convertView.findViewById(R.id.tv_cart_pro_title);
            child.mPriceTxtView = (TextView) convertView.findViewById(R.id.tv_cart_pro_price);
            child.mMinusImgView = (ImageView)convertView.findViewById(R.id.iv_cart_pro_minus);
            child.mCountEdView = (EditText) convertView.findViewById(R.id.et_cart_pro_count);
            child.mPlusImgView = (ImageView)convertView.findViewById(R.id.iv_cart_pro_plus);
            child.mSelectCheckBox = (CheckBox) convertView.findViewById(R.id.cb_cart_pro_select);
            convertView.setTag(child);
        }else{
            child = (ViewHolderChild) convertView.getTag();
        }
        Product product = mCarts.get(groupPosition).getProducts().get(childPosition);
        Picasso.with(mContext).load(product.getPictureUrl()).fit().into(child.mProImgView);
        child.mProTitleView.setText(product.getTitle());
        child.mPriceTxtView.setText(String.valueOf(product.getPrice()));
        child.mCountEdView.setText(String.valueOf(product.getCount()));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        //指定位置的Product是否可以选择
        Toast.makeText(mContext, "groupPosition=" + groupPosition + ",childPosition=" + childPosition, Toast.LENGTH_SHORT).show();
        return true;
    }

    /**
     * 购物车的ViewHolder
     */
    private static class ViewHolderGroup{
        private CheckBox mSelectAllCheckBox;
        private TextView mSellerTxtView;
    }

    /**
     * 购物车商品的ViewHolder
     */
    private static class ViewHolderChild{
        private CheckBox mSelectCheckBox;
        private ImageView mProImgView;
        private TextView mProTitleView;
        private TextView mPriceTxtView;
        private ImageView mMinusImgView;
        private EditText mCountEdView;
        private ImageView mPlusImgView;
    }
}
