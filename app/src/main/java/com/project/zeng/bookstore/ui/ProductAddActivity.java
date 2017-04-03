package com.project.zeng.bookstore.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.MyApplication;
import com.project.zeng.bookstore.entities.Category;
import com.project.zeng.bookstore.entities.Product;
import com.project.zeng.bookstore.entities.Result;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.CategoryAPI;
import com.project.zeng.bookstore.net.ProductAPI;
import com.project.zeng.bookstore.net.impl.CategoryAPIImpl;
import com.project.zeng.bookstore.net.impl.ProductAPIImpl;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeng on 2017/4/3.
 * 添加商品的Activity
 */

public class ProductAddActivity extends Activity implements OnClickListener{

    private MyApplication app;
    //组件
    private ImageView mBackView;
    private ImageView mProImgView;
    private EditText mProTitleView;
    private EditText mProAuthorView;
    private EditText mProPressView;
    private EditText mProPriceView;
    private EditText mProCountView;
    private RelativeLayout mProCategoryLayout;
    private TextView mProCategoryView;
    private EditText mProDescView;
    private TextView mCommitView;

    //商品网络请求API
    ProductAPI mProductAPI = new ProductAPIImpl();
    //商品类型网络请求API
    CategoryAPI mCategoryAPI = new CategoryAPIImpl();

    private List<Category> mCategories;
    private Bitmap mProImg;//当前商品的图片
    private Product mProduct;//当前的商品
    private int SELECT_PIC_BY_TACK_PHOTO = 1;//通过拍照获取图片的请求码
    private int SELECT_PIC_BY_PICK_PHOTO = 2;//通过图库获取图片的请求码
    private boolean isFinish = false;//是否已完成商品信息填写
    //Handler
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //更新商品图片
                case 0x340:
                    mProImgView.setImageBitmap(mProImg);
                    setResult(1);
                    Log.e("ProductAddActivity", "添加图片");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_product);
        app = (MyApplication) getApplication();
        init();
        initListener();
        fetchCategory();
    }

    /**
     * 初始化组件
     */
    private void init() {
        mBackView = (ImageView) findViewById(R.id.iv_pro_add_back);
        mProImgView = (ImageView) findViewById(R.id.iv_pro_add_img);
        mProTitleView = (EditText) findViewById(R.id.et_pro_add_title);
        mProAuthorView = (EditText) findViewById(R.id.et_pro_add_author);
        mProPressView = (EditText) findViewById(R.id.et_pro_add_press);
        mProPriceView = (EditText) findViewById(R.id.et_pro_add_price);
        mProCountView = (EditText) findViewById(R.id.et_pro_add_count);
        mProCategoryLayout = (RelativeLayout) findViewById(R.id.rl_pro_add_category);
        mProCategoryView = (TextView) findViewById(R.id.tv_pro_add_category);
        mProDescView = (EditText) findViewById(R.id.et_pro_add_describe);
        mCommitView = (TextView) findViewById(R.id.tv_pro_add_commit);
        mProduct = new Product();
        mCategories = new ArrayList<>();
    }

    /**
     * 初始化事件
     */
    private void initListener() {
        mBackView.setOnClickListener(this);
        mProImgView.setOnClickListener(this);
        mProCategoryLayout.setOnClickListener(this);
        mCommitView.setOnClickListener(this);
    }

    /**
     * 获取商品分类列表
     */
    private void fetchCategory() {
        mCategoryAPI.fetchCategories(new DataListener<List<Category>>() {
            @Override
            public void onComplete(List<Category> result) {
                if(result != null){
                    mCategories = result;
                    Log.e("ProductAddActivity", "从网络加载的商品类型数量为：" + mCategories.size());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回按钮
            case R.id.iv_pro_add_back:
                finish();
                break;
            //商品的分类选择
            case R.id.rl_pro_add_category:
                showProCategoryDialog();
                break;
            //商品图片添加按钮
            case R.id.iv_pro_add_img:
                showProImgDialog();
                break;
            //提交商品按钮
            case R.id.tv_pro_add_commit:
                commit();
                break;
        }
    }

    /**
     * 显示选择商品类型的Dialog
     */
    private void showProCategoryDialog() {
        if(mCategories.size() == 0){
            return;
        }
        final String[] categories = new String[mCategories.size()];
        for(int i = 0; i < mCategories.size(); i++){
            categories[i] = mCategories.get(i).getName();
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("请选择商品类型");
        dialog.setItems(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mProCategoryView.setText(categories[which]);
                mProduct.setCategoryId(mCategories.get(which).getId());
            }
        });
        dialog.show();
    }

    /**
     * 提交添加的商品
     */
    private void commit() {
        if(mProImg == null){
            Toast.makeText(this, "请上传商品图片!", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            double price = Double.parseDouble(mProPriceView.getText().toString().trim());
            if(price < 0.01){
                Toast.makeText(this, "商品价格不能少于0.01!", Toast.LENGTH_SHORT).show();
                return;
            }
            mProduct.setPrice(price);
            int count = Integer.parseInt(mProCountView.getText().toString().trim());
            if(count < 1){
                Toast.makeText(this, "商品数量不能少于1!", Toast.LENGTH_SHORT).show();
                return;
            }
            mProduct.setCount(count);
        }catch (Exception e){
            isFinish = false;
        }
        String title = mProTitleView.getText().toString().trim();
        String author = mProAuthorView.getText().toString().trim();
        String press = mProPressView.getText().toString().trim();
        String describe = mProDescView.getText().toString().trim();
        if(title.equals("") || author.equals("") || press.equals("") || mProduct.getCategoryId() == null){
            isFinish = false;
        }else{
            isFinish = true;
        }
        if(!isFinish){
            Toast.makeText(this, "请完整填写完商品数据!", Toast.LENGTH_SHORT).show();
//            Log.e("ProductAddActivity", "title=" + title + ",author=" + author + ", press=" + press
//            + ",describe=" + describe + ",price=" + mProduct.getPrice() + ",count=" + mProduct.getCount());
            return;
        }
        mProduct.setTitle(title);
        mProduct.setAuthor(author);
        mProduct.setPress(press);
        mProduct.setDescribe(describe);
        mProductAPI.addProduct(app.getToken(), mProduct, mProImg, new DataListener<Result>() {
            @Override
            public void onComplete(Result result) {
                if(result.getResult().equals("success")){
                    setResult(1);
                    finish();
                }
                Toast.makeText(ProductAddActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 添加商品图片
     */
    private void showProImgDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("添加商品图片");
        String[] items = new String[]{"相册", "拍照"};
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
//                        Toast.makeText(UserSettingActivity.this, "相册", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
                        break;
                    case 1:
                        Toast.makeText(ProductAddActivity.this, "暂无此功能，敬请期待!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PIC_BY_PICK_PHOTO){//通过相册加载图片
            if(null == data){
                Toast.makeText(this, "选择图片出错!", Toast.LENGTH_SHORT).show();//没有选择图片或者选择出错
                return;
            }
            Uri uri = data.getData();
            Picasso.with(this).load(uri).resize(720, 600).into(new Target() {
                @Override
                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                    mProImg = bitmap;
                    mHandler.sendEmptyMessage(0x340);
                }

                @Override
                public void onBitmapFailed(Drawable drawable) {
                    Toast.makeText(ProductAddActivity.this, "商品图片加载失败!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPrepareLoad(Drawable drawable) {

                }
            });
        }
    }
}
