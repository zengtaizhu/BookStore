package com.project.zeng.bookstore.ui.frgm;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.MyApplication;
import com.project.zeng.bookstore.adapter.CartAdapter;
import com.project.zeng.bookstore.entities.Cart;
import com.project.zeng.bookstore.entities.Product;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.listeners.OnItemLongClickListener;
import com.project.zeng.bookstore.net.CartAPI;
import com.project.zeng.bookstore.net.impl.CartAPIImpl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.project.zeng.bookstore.adapter.CartAdapter.*;

/**
 * Created by zeng on 2017/2/28.
 * 购物车的Fragment
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class CartFragment extends Fragment implements OnClickListener,
        CheckInterface, ModifyCountInterface{

    //组件
    private ImageView mBackImgView;
    private ExpandableListView mExListView;
    private CheckBox mAllCheckBox;
    private TextView mTotalPriceTxtView;
    private TextView mSettleTxtView;

    private Context mContext;

    private List<Cart> mCarts;//购物车列表
    private Product mProduct;//长按点击的商品

    private double totalPrice = 0.0;//购买的商品总价
    private int totalCount = 0;//购买商品的数量

    //适配器
    private CartAdapter mCartAdapter;

    //全局配置
    private MyApplication app;

    //购物车的网络请求API
    CartAPI mCartAPI = new CartAPIImpl();

    private boolean isShowBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);//允许Fragment添加item到选项菜单
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        init(view);
        initListener();
        app = (MyApplication) getActivity().getApplication();
        return view;
    }

    /**
     * 初始化组件
     * @param view
     */
    private void init(View view){
        mBackImgView = (ImageView)view.findViewById(R.id.iv_cart_back);
        if(isShowBack){
            mBackImgView.setVisibility(View.VISIBLE);
        }
        mExListView = (ExpandableListView) view.findViewById(R.id.exListView_cart);
        mAllCheckBox = (CheckBox) view.findViewById(R.id.cb_cart_select_all);
        mTotalPriceTxtView = (TextView)view.findViewById(R.id.tv_cart_total_price);
        mSettleTxtView = (TextView)view.findViewById(R.id.tv_cart_settle);
        mContext = getActivity().getApplication();
        mCarts = new ArrayList<>();
    }

    /**
     * 初始化事件
     */
    private void initListener(){
        mBackImgView.setOnClickListener(this);
        mSettleTxtView.setOnClickListener(this);
        mAllCheckBox.setOnClickListener(this);
    }

    /**
     * 获取购物车数据
     */
    public void fetchData(){
        mCartAPI.fetchCarts("201330350312", new DataListener<List<Cart>>() {//------先用Id测试
            @Override
            public void onComplete(List<Cart> result) {
                if(result != null){
//                    Log.e("CartFragment", "从网络获取的数据cart数量为：" + result.size());
                    mCarts = result;
                    mCartAdapter = new CartAdapter(mContext, result);
                    mCartAdapter.setCheckInterface(CartFragment.this);//1.设置复选框接口
                    mCartAdapter.setModifyCountInterface(CartFragment.this);//2.设置数量改变接口
                    mCartAdapter.setOnItemLongClickListener(new OnItemLongClickListener<Product>() {
                        @Override
                        public void onLongClick(Product item) {
//                            Log.e("CartFragment", "点击了" + item.getTitle());
                            mProduct = item;
                        }
                    });
                    mCartAdapter.setOnCreateContextMenu(new OnCreateContextMenuListener() {
                        @Override
                        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                            menu.add(Menu.NONE, 0, 0, "删除");
                        }
                    });
                    mExListView.setAdapter(mCartAdapter);
                    for(int i = 0; i < mCartAdapter.getGroupCount(); i++){
                        mExListView.expandGroup(i);//3.初始化时，将ExpandableListView以展开的方式呈现
                    }
                }
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        Toast.makeText(mContext, "item的title=" + item.getTitle(), Toast.LENGTH_SHORT).show();
        deDelete();
        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回按钮
            case R.id.iv_cart_back:
                getActivity().finish();
                break;
            //结算按钮
            case R.id.tv_cart_settle:
                Toast.makeText(getActivity().getApplication(), "结算", Toast.LENGTH_SHORT).show();
                break;
            //全选按钮
            case R.id.cb_cart_select_all:
                doCheckAll();
                break;
        }
    }

    /**
     * 全选和反选
     */
    private void doCheckAll(){
        for(int i = 0; i < mCarts.size(); i++){
            mCarts.get(i).isSelectAll = mAllCheckBox.isChecked();//设置所有的Cart的选中与否
            List<Product> products = mCarts.get(i).getProducts();
            for(int j = 0; j < products.size(); j++){//设置所有的Product的选中与否
                products.get(j).isSelected = mAllCheckBox.isChecked();
            }
        }
        calculate();
        mCartAdapter.notifyDataSetChanged();
    }

    /**
     * 删除商品的操作
     */
    private void deDelete(){
        for(int i = 0; i < mCarts.size(); i++){
            List<Product> products = mCarts.get(i).getProducts();
            for(int j = 0; j < products.size(); j++){
                if(products.get(j).equals(mProduct)){
                    products.remove(mProduct);
                    mCartAdapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        Cart cart = mCarts.get(groupPosition);
        List<Product> products = cart.getProducts();
        for(int i = 0; i < products.size(); i++){
            products.get(i).isSelected = isChecked;
        }
        if(isAllCheck()){
            mAllCheckBox.setChecked(true);
        }else{
            mAllCheckBox.setChecked(false);
        }
        mCartAdapter.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
        boolean allChildSameState = true;//判断该组下的所有子元素是否是同一种状态
        Cart cart = mCarts.get(groupPosition);
        List<Product> products = cart.getProducts();
        for(int i = 0; i < products.size(); i++){
            if(products.get(i).isSelected != isChecked){
                allChildSameState = false;
                break;
            }
        }
        if(allChildSameState){
            cart.isSelectAll = isChecked;
        }else{
            cart.isSelectAll = false;
        }
        if(isAllCheck()){
            mAllCheckBox.setChecked(true);
        }else {
            mAllCheckBox.setChecked(false);
        }
        mCartAdapter.notifyDataSetChanged();
        calculate();
    }

    /**
     * 判断所有Cart是否全部被选中
     * @return
     */
    private boolean isAllCheck(){
        for(Cart cart : mCarts){
            if(!cart.isSelectAll){
                return false;
            }
        }
        return true;
    }

    /**
     * 计算总价
     * 1.先清空总价和总数量
     * 2.遍历所有已选的子元素，计算总价和总数量
     * 3.更新界面的总价和总数量
     */
    private void calculate(){
        totalCount = 0;
        totalPrice = 0;
        for(int i = 0; i < mCarts.size(); i++){
            Cart cart = mCarts.get(i);
            List<Product> products = cart.getProducts();
            for(int j = 0; j < products.size(); j++){
                Product product = products.get(j);
                if(product.isSelected){
                    totalCount++;
                    totalPrice += product.getPrice() * product.getCount();
                }
            }
        }
        mTotalPriceTxtView.setText(df.format(totalPrice) + "");
        mSettleTxtView.setText("结算(" + totalCount +")");
    }

    //计算后，总价只保留小数点后两位
    DecimalFormat df = new DecimalFormat(".##");

    @Override
    public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        Product product = mCarts.get(groupPosition).getProducts().get(childPosition);
        int currentCount = product.getCount();
        currentCount++;
        product.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        mCartAdapter.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        Product product = mCarts.get(groupPosition).getProducts().get(childPosition);
        int currentCount = product.getCount();
        if(currentCount == 1){
            return;
        }
        currentCount--;
        product.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        mCartAdapter.notifyDataSetChanged();
        calculate();
    }

    /**
     * 是否显示返回按钮
     * @param isShow
     */
    public void showBackImageView(boolean isShow){
        isShowBack = isShow;
    }
}
