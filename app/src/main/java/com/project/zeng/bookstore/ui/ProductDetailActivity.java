package com.project.zeng.bookstore.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.adapter.CommentAdapter;
import com.project.zeng.bookstore.entities.Comment;
import com.project.zeng.bookstore.entities.Product;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.CommentAPI;
import com.project.zeng.bookstore.net.impl.CommentAPIImpl;
import com.project.zeng.bookstore.ui.frgm.CartActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zeng on 2017/3/4.
 * 商品详细Activity
 */

public class ProductDetailActivity extends Activity implements OnClickListener{

    //组件
    private ImageView mBackImgView;

    private ImageView mProImgView;
    private TextView mProTitleTxtView;
    private TextView mProDescTxtView;
    private TextView mProPriceTxtView;
    private TextView mProAuthorTxtView;
    private TextView mProPressTxtView;
    private TextView mProSellerTxtView;
    private ListView mProCommentsView;

    private ImageView mCartImgView;
    private TextView mBuyView;
    private TextView mAddToCartView;

    private ImageView mGoTopImgView;

    private Product product;

    //商品评论列表的适配器
    private CommentAdapter mCommentAdapter;

    //商品评论的请求API
    private CommentAPI mCommentAPI = new CommentAPIImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_product_detail);
        //获取intent的数据
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        product = (Product) bundle.getSerializable("product");
//        Toast.makeText(this, "product的id=" + product.getId(), Toast.LENGTH_SHORT).show();
        init();
        initListener();
        //获取商品评论数据，更新界面
        fetchCommentData();
    }

    /**
     * 初始化组件
     */
    private void init(){
        mBackImgView = (ImageView)findViewById(R.id.iv_pro_detail_back);
        mProImgView = (ImageView)findViewById(R.id.iv_pro_detail_img);
        mProTitleTxtView = (TextView)findViewById(R.id.tv_pro_detail_title);
        mProDescTxtView = (TextView)findViewById(R.id.tv_pro_detail_describe);
        mProPriceTxtView = (TextView)findViewById(R.id.tv_pro_detail_price);
        mProAuthorTxtView = (TextView)findViewById(R.id.tv_pro_detail_author);
        mProPressTxtView = (TextView)findViewById(R.id.tv_pro_detail_press);
        mProSellerTxtView = (TextView)findViewById(R.id.tv_pro_detail_seller);
        mProCommentsView = (ListView)findViewById(R.id.lv_pro_comment);
        mCartImgView = (ImageView)findViewById(R.id.iv_pro_to_cart);
        mBuyView = (TextView)findViewById(R.id.tv_pro_buy);
        mAddToCartView = (TextView)findViewById(R.id.tv_pro_add_to_cart);
        mGoTopImgView = (ImageView)findViewById(R.id.iv_pro_detail_go_top);
        initView();
    }

    /**
     * 初始化界面
     */
    private void initView(){
        Picasso.with(getApplicationContext()).load(product.getPictureUrl()).fit().into(mProImgView);//加载图片
        mProTitleTxtView.setText(product.getTitle());
        mProDescTxtView.setText(product.getDescribe());
        mProPriceTxtView.setText(String.valueOf(product.getPrice()));
        mProAuthorTxtView.setText(product.getAuthor());
        mProPressTxtView.setText(product.getPress());
        mProSellerTxtView.setText("ID：" + product.getSellerId());
    }

    /**
     * 初始化组件事件和适配器
     */
    private void initListener(){
        mBackImgView.setOnClickListener(this);
        mProSellerTxtView.setOnClickListener(this);
        mCartImgView.setOnClickListener(this);
        mBuyView.setOnClickListener(this);
        mAddToCartView.setOnClickListener(this);
        mGoTopImgView.setOnClickListener(this);
        mCommentAdapter = new CommentAdapter(getApplicationContext());
        mProCommentsView.setAdapter(mCommentAdapter);
    }

    /**
     * 获取商品评价数据，渲染到界面
     */
    public void fetchCommentData(){
        mCommentAPI.fetchComment(product.getId(), new DataListener<List<Comment>>() {
            @Override
            public void onComplete(List<Comment> result) {
                if(null != result){
//                    Log.e("ProductDetailActivity", "从网络获取的comment的数量为：" + result.size());
                    mCommentAdapter.updateData(result);
                }else{
//                    Log.e("ProductDetailActivity", "从网络获取的comment的数量为0");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回按钮
            case R.id.iv_pro_detail_back:
                finish();
                break;
            //跳转到商品卖家
            case R.id.tv_pro_detail_seller:
                Toast.makeText(this, "跳转到商品卖家", Toast.LENGTH_SHORT).show();
                break;
            //跳转到购物车Fragment
            case R.id.iv_pro_to_cart:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                break;
            //购买商品
            case R.id.tv_pro_buy:
                Toast.makeText(this, "购买商品", Toast.LENGTH_SHORT).show();
                break;
            //添加到购物车
            case R.id.tv_pro_add_to_cart:
                Toast.makeText(this, "添加到购物车", Toast.LENGTH_SHORT).show();
                break;
            //跳转到顶部
            case R.id.iv_pro_detail_go_top:
                Toast.makeText(this, "跳转到顶部", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}