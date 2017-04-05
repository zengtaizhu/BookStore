package com.project.zeng.bookstore.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.entities.Cart;
import com.project.zeng.bookstore.entities.Product;
import com.project.zeng.bookstore.listeners.OnItemLongClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zeng on 2017/3/19.参考自 http://www.codeforge.cn/article/258051
 * CartFragment的购物车Adapter
 */

public class CartAdapter extends BaseExpandableListAdapter{

    private Context mContext;
    private List<Cart> mCarts;

    private CheckInterface mCheckInterface;//CheckBox的选中接口
    private ModifyCountInterface mModifyCountInterface;//数量修改的接口

    private OnItemLongClickListener<Product> mProLongClickListener;

    public CartAdapter(Context mContext, List<Cart> carts) {
        this.mContext = mContext;
        mCarts = carts;
    }

    public void setCheckInterface(CheckInterface checkInterface){
        mCheckInterface = checkInterface;
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.mModifyCountInterface = modifyCountInterface;
    }

    @Override
    public int getGroupCount() {
        return mCarts.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mCarts.get(groupPosition).getProducts().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mCarts.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
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
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ViewHolderGroup group;
        if(null == convertView){
            group = new ViewHolderGroup();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_cart, parent, false);
            group.mSelectAllCheckBox = (CheckBox)convertView.findViewById(R.id.cb_cart_select_one);
            group.mSellerTxtView = (TextView)convertView.findViewById(R.id.tv_cart_seller);
            convertView.setTag(group);
        }else{
            group = (ViewHolderGroup) convertView.getTag();
        }
        final Cart item = mCarts.get(groupPosition);
        if(item != null){
            group.mSellerTxtView.setText(item.getSeller());
            group.mSelectAllCheckBox.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.isSelectAll = ((CheckBox)v).isChecked();
                    mCheckInterface.checkGroup(groupPosition, ((CheckBox) v).isChecked());//暴露组选接口，方便修改
                }
            });
            group.mSelectAllCheckBox.setChecked(item.isSelectAll);
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolderChild child;
        if(null == convertView){
            child = new ViewHolderChild();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_cart_product, parent, false);
            child.mSelectCheckBox = (CheckBox) convertView.findViewById(R.id.cb_cart_pro_select);

            child.mProImgView = (ImageView)convertView.findViewById(R.id.iv_cart_pro_img);
            child.mProTitleView =  (TextView)convertView.findViewById(R.id.tv_cart_pro_title);
            child.mPriceTxtView = (TextView) convertView.findViewById(R.id.tv_cart_pro_price);
            child.mMinusImgView = (ImageView)convertView.findViewById(R.id.iv_cart_pro_minus);
            child.mCountEdView = (EditText) convertView.findViewById(R.id.et_cart_pro_count);
            child.mPlusImgView = (ImageView)convertView.findViewById(R.id.iv_cart_pro_plus);
            convertView.setTag(child);
        }else{
            child = (ViewHolderChild) convertView.getTag();
        }
        final Product item = (Product) getChild(groupPosition, childPosition);
        if(item != null){
            Picasso.with(mContext).load(item.getPictureUrl()).fit().into(child.mProImgView);
            child.mProTitleView.setText(item.getTitle());
            child.mPriceTxtView.setText(String.valueOf(item.getPrice()));
            child.mCountEdView.setText(String.valueOf(item.getCount()));
            child.mSelectCheckBox.setChecked(item.isSelected);
            child.mSelectCheckBox.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.isSelected = ((CheckBox)v).isChecked();
                    child.mSelectCheckBox.setChecked(((CheckBox)v).isChecked());
                    mCheckInterface.checkChild(groupPosition, childPosition, ((CheckBox)v).isChecked());//暴露子选接口
                }
            });
            child.mMinusImgView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mModifyCountInterface.doDecrease(groupPosition,
                            childPosition, child.mCountEdView, child.mSelectCheckBox.isChecked());//暴露增加接口
                }
            });
            child.mPlusImgView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mModifyCountInterface.doIncrease(groupPosition,
                            childPosition, child.mCountEdView, child.mSelectCheckBox.isChecked());//暴露删减接口
                }
            });
            convertView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mProLongClickListener.onLongClick(item);
                    v.setOnCreateContextMenuListener(mContextMenuListener);
                    return false;
                }
            });
        }
        return convertView;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<Product>listener) {
        this.mProLongClickListener = listener;
    }

    private OnCreateContextMenuListener mContextMenuListener;

    public void setOnCreateContextMenu(OnCreateContextMenuListener listener){
        mContextMenuListener = listener;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /**
     * 更新数据并刷新界面 -----------待删除
     * @param carts
     */
    public void updateData(List<Cart> carts){
        mCarts.clear();
        mCarts = carts;
        notifyDataSetChanged();
    }

    /**
     * 复选框的接口
     */
    public interface CheckInterface{
        /**
         * 组选框状态改变触发的事件
         * @param groupPosition 组元素位置
         * @param isChecked 组元素选中与否
         */
        void checkGroup(int groupPosition, boolean isChecked);

        /**
         * 子选框状态改变触发的事件
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param isChecked 子元素选中与否
         */
        void checkChild(int groupPosition, int childPosition, boolean isChecked);
    }

    public interface ModifyCountInterface{
        /**
         * 增加操作
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked 子元素选中与否
         */
        void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);
        /**
         * 删减操作
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked 子元素选中与否
         */
        void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);
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
