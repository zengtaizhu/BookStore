package com.project.zeng.bookstore.ui.frgm;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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
import com.project.zeng.bookstore.entities.Result;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.listeners.OnItemLongClickListener;
import com.project.zeng.bookstore.net.CartAPI;
import com.project.zeng.bookstore.net.impl.CartAPIImpl;
import com.project.zeng.bookstore.ui.PayActivity;

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
    private MyApplication app;//全局配置

    private List<Cart> mCarts;//购物车列表
    private Product mProduct;//长按点击的商品

    private double totalPrice = 0.0;//购买的商品总价
    private int totalCount = 0;//购买商品的数量

    //适配器
    private CartAdapter mCartAdapter;

    //购物车的网络请求API
    CartAPI mCartAPI = new CartAPIImpl();

    private boolean isShowBack;//是否显示返回按钮

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);//允许Fragment添加item到选项菜单
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        init(view);
        initListener();
        mContext = getActivity().getApplication();
        app = (MyApplication) mContext;
//        Log.e("CartFragment", "token=" + app.getToken());
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
        mAllCheckBox.setChecked(false);
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
    public void fetchData(String token){
        if(mAllCheckBox != null){
            mAllCheckBox.setChecked(false);
        }
        if(token.equals("")){//未登录
//            Toast.makeText(mContext, "请先登录!", Toast.LENGTH_SHORT).show();
            return;
        }
        mCartAPI.fetchCarts(token, new DataListener<List<Cart>>() {
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
//                Toast.makeText(getActivity().getApplication(), "结算", Toast.LENGTH_SHORT).show();
                settle();
                break;
            //全选按钮
            case R.id.cb_cart_select_all:
                doCheckAll();
                break;
        }
    }

    private List<Product> products = new ArrayList<>();//购买的商品

    /**
     * 结算
     */
    private void settle(){
        if(mCarts.size() <= 0){
            Toast.makeText(mContext, "没有选择商品，请重新选择", Toast.LENGTH_SHORT).show();
            return;
        }
        int count = 0;//选择的购物车数量
        for(Cart cart : mCarts){
            for(Product p : cart.getProducts()){
                if(p.isSelected == true){
                    count++;
                    products.add(p);//添加选中的商品
                    break;
                }
            }
        }
        Log.e("CartFragment", "count=" + count);
        if(count > 1){
            Toast.makeText(mContext, "对不起，本版本只支持同时购买同一卖家的商品，请重选", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(mContext, PayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("sellerId", products.get(0).getSellerId());
        ArrayList list = new ArrayList();
        list.add(products);
        bundle.putParcelableArrayList("products", list);
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
    }

    /**
     * 全选和反选
     */
    private void doCheckAll(){
        if(null == mCartAdapter){
            return;
        }
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
            final List<Product> products = mCarts.get(i).getProducts();
            for(int j = 0; j < products.size(); j++){
                if(products.get(j).equals(mProduct)){
                    if(products.size() == 1){//若Cart只有该商品，则删除Cart
                        //删除服务器数据库数据
                        final int index = i;//当前的购物车
//                        Log.e("CartFragment", "id=" + mCarts.get(i).getId());
                        mCartAPI.deleteCart(app.getToken(), mCarts.get(i).getId(), new DataListener<Result>() {
                            @Override
                            public void onComplete(Result result) {
                                Toast.makeText(mContext, result.getMessage(), Toast.LENGTH_SHORT).show();
                                if(result.getResult().contains("error")){
                                    return;
                                }
                                mCarts.remove(index);
                                mCartAdapter.notifyDataSetChanged();
                                return;
                            }
                        });
                    }else {
                        //删除数据库数据
                        mCartAPI.deleteProFromCart(app.getToken(), mProduct.getId(),
                                mCarts.get(i).getId(), new DataListener<Result>() {
                                    @Override
                                    public void onComplete(Result result) {
                                        Toast.makeText(mContext, result.getMessage(), Toast.LENGTH_SHORT).show();
                                        if(result.getResult().contains("error")){
                                            return;
                                        }
                                        products.remove(mProduct);
                                        mCartAdapter.notifyDataSetChanged();
                                        return;
                                    }
                                });
                    }
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
    DecimalFormat df = new DecimalFormat("##.##");

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){//跳转到OrderActivity
            Log.e("CartFragment", "resultCode=" + resultCode);
            if(resultCode == 0){//支付成功，则更新购物车数据
                for(Product p : products){
                    mProduct = p;
                    deDelete();//删除购物车中购买了的商品
                }
                fetchData(app.getToken());
            }else{//resultCode=1，则代表支付失败
                products.clear();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
