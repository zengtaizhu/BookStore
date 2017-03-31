package com.project.zeng.bookstore.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnScrollChangeListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.MyApplication;
import com.project.zeng.bookstore.adapter.CommentAdapter;
import com.project.zeng.bookstore.entities.Comment;
import com.project.zeng.bookstore.entities.Product;
import com.project.zeng.bookstore.entities.Result;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.CartAPI;
import com.project.zeng.bookstore.net.CommentAPI;
import com.project.zeng.bookstore.net.impl.CartAPIImpl;
import com.project.zeng.bookstore.net.impl.CommentAPIImpl;
import com.project.zeng.bookstore.widgets.CartDialog;
import com.project.zeng.bookstore.widgets.CartDialog.OnDialogClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zeng on 2017/3/4.
 * 商品详细Activity
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class ProductDetailActivity extends Activity implements OnClickListener, OnScrollChangeListener{

    private MyApplication app;//全局变量
    //组件
    private ImageView mBackImgView;

    private ScrollView mScrollView;
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

    private ImageView mGoTopImgView;//置顶按钮

    private Product mProduct;//浏览的商品

    //商品评论列表的适配器
    private CommentAdapter mCommentAdapter;

    //商品评论的网络请求API
    private CommentAPI mCommentAPI = new CommentAPIImpl();
    //购物车的网络请求API
    CartAPI mCartAPI = new CartAPIImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_product_detail);
        app = (MyApplication) getApplication();
        //获取intent的数据
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mProduct = (Product) bundle.getSerializable("product");
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
        mScrollView = (ScrollView)findViewById(R.id.sv_pro_detail);
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
        mScrollView.setOnScrollChangeListener(this);
        Picasso.with(getApplicationContext()).load(mProduct.getPictureUrl()).fit().into(mProImgView);//加载图片
        mProTitleTxtView.setText(mProduct.getTitle());
        mProDescTxtView.setText(mProduct.getDescribe());
        mProPriceTxtView.setText(String.valueOf(mProduct.getPrice()));
        mProAuthorTxtView.setText(mProduct.getAuthor());
        mProPressTxtView.setText(mProduct.getPress());
        mProSellerTxtView.setText("ID：" + mProduct.getSellerId());
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
        mCommentAPI.fetchComment(mProduct.getId(), new DataListener<List<Comment>>() {
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
            //跳转到顶部
            case R.id.iv_pro_detail_go_top:
                mScrollView.scrollTo(0, 0);
                break;
            //跳转到商品卖家
            case R.id.tv_pro_detail_seller:
//                Toast.makeText(this, "跳转到商品卖家", Toast.LENGTH_SHORT).show();
                Intent sellerIntent = new Intent(this, SellerActivity.class);
                sellerIntent.putExtra("sellerId", mProduct.getSellerId());
                startActivity(sellerIntent);
                break;
            //跳转到购物车Activity
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
//                Toast.makeText(this, "添加到购物车", Toast.LENGTH_SHORT).show();
                showAddToCartDialog();
                break;
        }
    }

    /**
     * 显示添加到购物车的Dialog
     */
    private void showAddToCartDialog(){
        CartDialog dialog = new CartDialog(this, mProduct, new OnDialogClickListener() {
            @Override
            public void add(int count) {
                addToCart(count);
            }
        });
        dialog.show();
    }

    /**
     * 添加商品到购物车
     */
    private void addToCart(int count){
//        Log.e("ProductActivity", "count=" + count);
        if(app.getToken().equals("")){
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }
        mCartAPI.addProToCart(app.getToken(), mProduct.getId(), count, new DataListener<Result>() {
            @Override
            public void onComplete(Result result) {
                Toast.makeText(ProductDetailActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //设置置顶按钮的显示与否
//        Log.e("ProductDetailActivity", "scrollY=" + scrollY + ",oldScrollY" + oldScrollY);
        if(scrollY >= 1000){
            mGoTopImgView.setVisibility(View.VISIBLE);
        }else{
            mGoTopImgView.setVisibility(View.GONE);
        }
    }
}