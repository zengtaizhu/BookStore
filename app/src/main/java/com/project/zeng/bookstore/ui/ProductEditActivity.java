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
import android.widget.TextView;
import android.widget.Toast;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.MyApplication;
import com.project.zeng.bookstore.entities.Product;
import com.project.zeng.bookstore.entities.Result;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.ProductAPI;
import com.project.zeng.bookstore.net.impl.ProductAPIImpl;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by zeng on 2017/4/3.
 * 商品编辑的Activity
 */

public class ProductEditActivity extends Activity implements OnClickListener{

    private MyApplication app;
    //组件
    private ImageView mBackView;
    private ImageView mProImgView;
    private EditText mProTitleView;
    private EditText mProAuthorView;
    private EditText mProPressView;
    private EditText mProPriceView;
    private ImageView mProMinusView;
    private EditText mProCountView;
    private ImageView mProPlusView;
    private EditText mProDescribeView;
    private TextView mCommitView;

    //商品网络请求API
    ProductAPI mProductAPI = new ProductAPIImpl();

    private Product mProduct;//当前的商品
    private int mCount;//当前商品数量
    private Bitmap mProImg;//当前商品的图片
    private int SELECT_PIC_BY_TACK_PHOTO = 1;//通过拍照获取图片的请求码
    private int SELECT_PIC_BY_PICK_PHOTO = 2;//通过图库获取图片的请求码
    //Handler
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //更新商品图片
                case 0x322:
                    mProImgView.setImageBitmap(mProImg);
                    setResult(1);
//                    Log.e("ProductEditActivity", "修改商品图片");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_product);
        app = (MyApplication) getApplication();
        mProduct = (Product) getIntent().getExtras().get("product");
        init();
        initListener();
        initView();
    }

    /**
     * 初始化组件
     */
    private void init() {
        mBackView = (ImageView) findViewById(R.id.iv_pro_edit_back);
        mProImgView = (ImageView) findViewById(R.id.iv_pro_edit_img);
        mProTitleView = (EditText) findViewById(R.id.et_pro_edit_title);
        mProAuthorView = (EditText) findViewById(R.id.et_pro_edit_author);
        mProPressView = (EditText) findViewById(R.id.et_pro_edit_press);
        mProPriceView = (EditText) findViewById(R.id.et_pro_edit_price);
        mProMinusView = (ImageView) findViewById(R.id.iv_pro_edit_minus);
        mProCountView = (EditText) findViewById(R.id.et_pro_edit_count);
        mProPlusView = (ImageView) findViewById(R.id.iv_pro_edit_plus);
        mProDescribeView = (EditText) findViewById(R.id.tv_pro_edit_describe);
        mCommitView = (TextView) findViewById(R.id.tv_pro_edit_commit);
    }

    /**
     * 初始化事件
     */
    private void initListener() {
        mBackView.setOnClickListener(this);
        mProImgView.setOnClickListener(this);
        mProMinusView.setOnClickListener(this);
        mProPlusView.setOnClickListener(this);
        mCommitView.setOnClickListener(this);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        Picasso.with(this).load(mProduct.getPictureUrl()).fit().into(mProImgView);
        mProTitleView.setText(mProduct.getTitle());
        mProAuthorView.setText(mProduct.getAuthor());
        mProPressView.setText(mProduct.getPress());
        mProPriceView.setText(mProduct.getPrice() + "");
        mProCountView.setText(mProduct.getCount() + "");
        mProDescribeView.setText(mProduct.getDescribe());
        mCount = mProduct.getCount();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回按钮
            case R.id.iv_pro_edit_back:
                setResult(0);
                finish();
                break;
            //修改商品图片
            case R.id.iv_pro_edit_img:
                showProImgDialog();
                break;
            //商品数量减少按钮
            case R.id.iv_pro_edit_minus:
                if(mCount > 1){
                    mCount--;
                    mProCountView.setText(mCount + "");
                }
                break;
            //商品数量增加按钮
            case R.id.iv_pro_edit_plus:
                mCount++;
                mProCountView.setText(mCount + "");
                break;
            //提交修改按钮
            case R.id.tv_pro_edit_commit:
                commitChange();
                break;
        }
    }

    /**
     * 提交商品信息修改
     */
    private void commitChange(){
        mProduct.setTitle(mProTitleView.getText().toString());
        mProduct.setAuthor(mProAuthorView.getText().toString());
        mProduct.setPress(mProPressView.getText().toString());
        mProduct.setPrice(Double.parseDouble(mProPriceView.getText().toString().trim()));
        mProduct.setCount(mCount);
        mProduct.setDescribe(mProDescribeView.getText().toString());
        mProductAPI.modifyProduct(app.getToken(), mProduct, new DataListener<Result>() {
            @Override
            public void onComplete(Result result) {
                Toast.makeText(ProductEditActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                if(result.getResult().equals("success")){
                    setResult(1);//修改成功
                    finish();
                }
            }
        });
    }

    /**
     * 修改商品图片
     */
    private void showProImgDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("修改商品图片");
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
                        Toast.makeText(ProductEditActivity.this, "暂无此功能，敬请期待!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SELECT_PIC_BY_PICK_PHOTO){//从相册取图片
            if(null == data){
                Toast.makeText(this, "选择图片出错!", Toast.LENGTH_SHORT).show();//没有选择图片或者选择出错
                return;
            }
            Uri uri = data.getData();
            Picasso.with(this).load(uri).resize(720, 600).into(new Target() {
                @Override
                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                    mProductAPI.modifyProductImg(app.getToken(), mProduct.getId(), bitmap, new DataListener<Result>() {
                        @Override
                        public void onComplete(Result result) {
                            if(result.getResult().equals("success")){
                                mProImg = bitmap;
                                mHandler.sendEmptyMessage(0x322);
                            }
                            Toast.makeText(ProductEditActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onBitmapFailed(Drawable drawable) {
                    Toast.makeText(ProductEditActivity.this, "商品图片加载失败!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPrepareLoad(Drawable drawable) {

                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
