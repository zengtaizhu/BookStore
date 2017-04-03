package com.project.zeng.bookstore.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.MyApplication;
import com.project.zeng.bookstore.adapter.ProductRecyclerAdapter;
import com.project.zeng.bookstore.entities.Product;
import com.project.zeng.bookstore.entities.Result;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.ProductAPI;
import com.project.zeng.bookstore.net.impl.ProductAPIImpl;
import com.project.zeng.bookstore.widgets.MyRecyclerView;

import java.util.List;

import static com.project.zeng.bookstore.adapter.ProductRecyclerAdapter.*;

/**
 * Created by zeng on 2017/4/2.
 * 我的商店的Activity
 */

public class StoreActivity extends Activity implements MyItemLongClickListener, OnClickListener{

    private MyApplication app;
    //组件
    private ImageView mBackView;
    private ImageView mAddProView;
    private MyRecyclerView mProsView;
    //适配器
    private ProductRecyclerAdapter mProductAdapter;

    //商品网络请求API
    ProductAPI mProductAPI = new ProductAPIImpl();

    private List<Product> mProduct;//商品列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_store);
        app = (MyApplication) getApplication();
        init();
        initListener();
        initView();
        fetchData();
    }

    /**
     * 初始化组件
     */
    private void init() {
        mBackView = (ImageView) findViewById(R.id.iv_store_back);
        mAddProView = (ImageView) findViewById(R.id.iv_store_add_pro);
        mProsView = (MyRecyclerView) findViewById(R.id.rv_store_pros);
    }

    /**
     * 初始化事件
     */
    private void initListener() {
        mBackView.setOnClickListener(this);
        mAddProView.setOnClickListener(this);
        mProductAdapter = new ProductRecyclerAdapter(this);
        mProductAdapter.setItemLongListener(this);
        mProsView.setAdapter(mProductAdapter);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mProsView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * 获得数据，更新界面
     */
    private void fetchData(){
        mProductAPI.fetchProductBySeller(app.getUser().getId(), new DataListener<List<Product>>() {
            @Override
            public void onComplete(List<Product> result) {
                if(null != result){
                    Log.e("StoreActivity", "从网络获取的product数量为：" + result.size());
                    mProductAdapter.updateData(result);
                    mProduct = result;
                }
            }
        });
    }

    /**
     * 商品的长按事件
     * @param view
     * @param position
     */
    @Override
    public void onItemLongClick(View view, final int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        String[] choice = new String[]{"修改商品信息", "删除商品"};
        dialog.setItems(choice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){//
//                    Toast.makeText(StoreActivity.this, "修改商品", Toast.LENGTH_SHORT).show();
                    Intent editIntent = new Intent(StoreActivity.this, ProductEditActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("product", mProduct.get(position));
                    editIntent.putExtras(bundle);
                    startActivityForResult(editIntent, 0);
                }else{
//                    Toast.makeText(StoreActivity.this, "删除商品", Toast.LENGTH_SHORT).show();
                    mProductAPI.deleteProduct(app.getToken(), mProduct.get(position).getId(), new DataListener<Result>() {
                        @Override
                        public void onComplete(Result result) {
                            if(result.getResult().equals("success")){
                                mProduct.remove(position);//删除商品
                                mProductAdapter.updateData(mProduct);//更新界面
                            }
                            Toast.makeText(StoreActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回按钮
            case R.id.iv_store_back:
                finish();
                break;
            //添加商品
            case R.id.iv_store_add_pro:
//                Toast.makeText(StoreActivity.this, "添加商品", Toast.LENGTH_SHORT).show();
                Intent addIntent = new Intent(StoreActivity.this, ProductAddActivity.class);
                startActivityForResult(addIntent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){//跳转到商品编辑ProductEditActivity
            if(resultCode == 1){//修改商品信息成功
                fetchData();//加载新数据，更新界面
            }
        }else{//跳转到添加商品ProductAddActivity
            if(resultCode == 1){//添加商品成功
                fetchData();//加载新数据，更新界面
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
