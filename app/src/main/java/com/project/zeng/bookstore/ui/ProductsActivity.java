package com.project.zeng.bookstore.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnScrollChangeListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.adapter.ProductRecyclerAdapter;
import com.project.zeng.bookstore.db.AbsDBAPI;
import com.project.zeng.bookstore.db.models.DbFactory;
import com.project.zeng.bookstore.entities.Product;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.ProductAPI;
import com.project.zeng.bookstore.net.impl.ProductAPIImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.project.zeng.bookstore.adapter.ProductRecyclerAdapter.*;

/**
 * Created by zeng on 2017/3/2.
 * 搜索后的商品列表（注：每次都从网络加载数据，不保存商品信息到数据库）
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class ProductsActivity extends Activity implements OnClickListener,
        OnScrollChangeListener, MyItemClickListener{

    //组件
    private ScrollView mScrollView;

    private EditText mSearchEditText;
    private ImageView mBackView;
    private TextView mFilterView;

    private TextView mDefaultOrderView;
    private TextView mPriceOrderView;
    private ImageView mPriceOrderImgView;
    private TextView mAuthorOrderView;
    private ImageView mIndicatorView;

    private RecyclerView mRecyProView;

    private ImageView mGoTopView;

    //适配器
    private ProductRecyclerAdapter mProductRecyAdapter;

    //商品网络请求API
    ProductAPI mProductAPI = new ProductAPIImpl();
    //操作商品数据的数据库对象
    AbsDBAPI<Product> mProDbAPI = DbFactory.createProductModel();

    //切换标记,默认显示线性布局
    private boolean isLinearLayout = true;

    private List<Product> mProducts;//商品列表
    private String SEARCH_ALL = "0";//查找全部
    private String SEARCH_BY_KEY = "1";//通过关键字查找
    private String SEARCH_BY_PRO = "2";//通过商品ID查找
    private String SEARCH_BY_GRADE = "3";//通过适合年级查找

    private int DEFAULT = 0;//默认排列
    private int PRICE_ASC = 1;//价格升序排列
    private int PRICE_DESC = 2;//价格降序排列
    private boolean isPriceAsc = true;//判断是否是降序，默认升序
    private int AUTHOR = 3;//作者名排序

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_product);
        Intent intent = getIntent();
        String keyWord = intent.getStringExtra("key");//搜索关键字
        //搜索类型：1--关键字，2--商品类型ID，3--适合年级
        String type = intent.getStringExtra("type");
//        Toast.makeText(this, "keyWord=" + keyWord + ", type=" + type, Toast.LENGTH_SHORT).show();
        //若通过关键字搜索，则显示关键字
        if(type.equals(SEARCH_BY_KEY)){
            init(keyWord);
        }else if(type.equals(SEARCH_BY_PRO)){
            //若通过商品类型ID搜索，则显示商品类型名称
            String name = intent.getStringExtra("name");
            init(name);
        }
        initListener();
        //获取数据并更新界面
        fetchData(keyWord, type);
    }

    /**
     * 初始化组件
     * @param keyWord
     */
    private void init(String keyWord){
        mProducts = new ArrayList<>();//初始化Product列表
        mScrollView = (ScrollView)findViewById(R.id.sv_product);
        mSearchEditText = (EditText)findViewById(R.id.et_search_category);
        mSearchEditText.setText(keyWord);//设置搜索框内容
        mBackView = (ImageView)findViewById(R.id.iv_search_back);
        mFilterView = (TextView) findViewById(R.id.tv_pro_filter);
        mDefaultOrderView = (TextView)findViewById(R.id.tv_pro_order_default);
        mPriceOrderView = (TextView)findViewById(R.id.tv_pro_order_price);
        mPriceOrderImgView = (ImageView)findViewById(R.id.iv_pro_order_price);
        mAuthorOrderView = (TextView)findViewById(R.id.tv_pro_order_press);
        mIndicatorView = (ImageView) findViewById(R.id.iv_search_indicator);
        mRecyProView = (RecyclerView)findViewById(R.id.recyclerView_product);
        mGoTopView = (ImageView)findViewById(R.id.iv_pro_go_top);
    }

    /**
     * 初始化组件的事件及适配器
     */
    private void initListener(){
        mSearchEditText.setOnClickListener(this);
        mScrollView.setOnScrollChangeListener(this);
        mBackView.setOnClickListener(this);
        mIndicatorView.setOnClickListener(this);
        mProductRecyAdapter = new ProductRecyclerAdapter(this);
        mProductRecyAdapter.setItemClickListener(this);
        mRecyProView.setAdapter(mProductRecyAdapter);
        mRecyProView.setLayoutManager(new LinearLayoutManager(this));
        mGoTopView.setOnClickListener(this);
        mDefaultOrderView.setOnClickListener(this);
        mPriceOrderView.setOnClickListener(this);
        mAuthorOrderView.setOnClickListener(this);
    }

    /**
     * 获取Product数据后渲染到界面
     */
    public void fetchData(final String keyWord, String type){
        switch (type){
            //通过关键字搜索
            case "1":
//                HashMap<String, Object> hashMap = new HashMap<>();
//                hashMap.put("type", SEARCH_BY_KEY);
//                hashMap.put("key", new String[]{"%" + keyWord + "%"});
                //从网络加载数据
                mProductAPI.fetchProductsByWord(keyWord, new DataListener<List<Product>>() {
                    @Override
                    public void onComplete(List<Product> result) {
                        if(null != result){
//                            Log.e("ProductsActivity", "从网络获取的product的数量为：" + result.size());
                            mProductRecyAdapter.updateData(result);
                            mProducts = result;
                            mProDbAPI.deleteAll();//清空原先的缓存
                            mProDbAPI.saveItems(result);//保存数据到数据库
                        }else{
//                            Log.e("ProductsActivity", "从网络获取的product的数量为:0");
                        }
                    }
                });
                break;
            //通过商品类型ID
            case "2":
                //从网络加载数据
                mProductAPI.fetchProductsByCategory(keyWord, new DataListener<List<Product>>() {
                    @Override
                    public void onComplete(List<Product> result) {
                        if(null != result){
                            Log.e("ProductsActivity", "从网络获取的product的数量为：" + result.size());
                            mProductRecyAdapter.updateData(result);
                            mProducts = result;
                            mProDbAPI.deleteAll();//清空原先的缓存
                            mProDbAPI.saveItems(result);//保存数据到数据库
                        }else{
                            Log.e("ProductsActivity", "从网络获取的product的数量为:0");
                        }
                    }
                });
                break;
        }
        //若无联网，则从数据库加载旧数据----------------待删除
        if(mProducts == null){
            mProDbAPI.loadDatasFromDb(new DataListener<List<Product>>() {
                @Override
                public void onComplete(List<Product> result) {
                    if(null != result){
                        Log.e("ProductsActivity", "从数据库加载Product的数量为：" + result.size());
                        mProductRecyAdapter.updateData(result);
                    }else{
                        Log.e("ProductsActivity", "从数据库加载的product的数量为:0");
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //切换布局
            case R.id.iv_search_indicator:
                if(isLinearLayout){
                    //切换成网格布局
                    mProductRecyAdapter.setType(1);
                    mRecyProView.setLayoutManager(new GridLayoutManager(this, 2));
                    mProductRecyAdapter.notifyDataSetChanged();
                    isLinearLayout = false;
                    mIndicatorView.setImageResource(R.mipmap.toolbar_search_indicator_bigimg);
                }else {
                    //切换成线性布局
                    mProductRecyAdapter.setType(0);
                    mRecyProView.setLayoutManager(new LinearLayoutManager(this));
                    mProductRecyAdapter.notifyDataSetChanged();
                    isLinearLayout = true;
                    mIndicatorView.setImageResource(R.mipmap.toolbar_search_indicator_list);
                }
                break;
            //搜索栏
            case R.id.et_search_category:
                Intent intent = new Intent(ProductsActivity.this, SearchActivity.class);
                startActivity(intent);
                finish();
                break;
            //返回按钮
            case R.id.iv_search_back:
                finish();
                break;
            //置顶按钮
            case R.id.iv_pro_go_top:
                mScrollView.scrollTo(0, 0);
                break;
            //商品默认排序
            case R.id.tv_pro_order_default:
                fetchDateByOrder(DEFAULT);
                mPriceOrderImgView.setImageResource(R.mipmap.ic_pro_price_normal);
                isPriceAsc = true;
                break;
            //商品按价格排序
            case R.id.tv_pro_order_price:
                if(isPriceAsc){//价格升序
                    fetchDateByOrder(PRICE_ASC);
                    isPriceAsc = false;
                    mPriceOrderImgView.setImageResource(R.mipmap.ic_pro_price_down);
                }else{//价格降序
                    fetchDateByOrder(PRICE_DESC);
                    isPriceAsc = true;
                    mPriceOrderImgView.setImageResource(R.mipmap.ic_pro_price_up);
                }
                break;
            //商品按作者排序
            case R.id.tv_pro_order_press:
                fetchDateByOrder(AUTHOR);
                mPriceOrderImgView.setImageResource(R.mipmap.ic_pro_price_normal);
                isPriceAsc = true;
                break;
        }
    }

    /**
     * 获取按照type排序的Product列表
     * @param type
     */
    private void fetchDateByOrder(int type){
        HashMap<String, Integer> params = new HashMap<>();
        params.put("type", type);
        mProDbAPI.loadDatasFromDbByOrder(new DataListener<List<Product>>() {//从数据库加载数据
            @Override
            public void onComplete(List<Product> result) {
                if(null != result){
                    Log.e("ProductsActivity", "从数据库加载Product的数量为：" + result.size());
                    mProductRecyAdapter.updateData(result);
                }else{
                    Log.e("ProductsActivity", "从数据库加载Product的数量为：0");
                }
            }
        }, params);
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//        Log.e("ProductsActivity", "scrollY=" + scrollX + ",oldScrollY" + oldScrollY);
        //设置置顶按钮的显示与否
        if(scrollY >= 200){
            mGoTopView.setVisibility(View.VISIBLE);
        }else{
            mGoTopView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        //通过商品ID，进入该商品的ProductDetailActivity
        Intent intent = new Intent(this, ProductDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", mProducts.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
