package com.project.zeng.bookstore.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.MyApplication;
import com.project.zeng.bookstore.adapter.OrderProductAdapter;
import com.project.zeng.bookstore.entities.Order;
import com.project.zeng.bookstore.entities.Product;
import com.project.zeng.bookstore.entities.Result;
import com.project.zeng.bookstore.entities.User;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.OrderAPI;
import com.project.zeng.bookstore.net.impl.OrderAPIImpl;
import com.project.zeng.bookstore.widgets.MyListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

import c.b.BP;
import c.b.PListener;

/**
 * Created by zeng on 2017/3/31.
 * 支付的Activity
 */

public class PayActivity extends Activity implements OnClickListener{

    private MyApplication app;//全局变量
    //组件
    private ImageView mBackView;
    private TextView mUserNameView;
    private TextView mUserPhoneView;
    private TextView mUserLocationView;
    private TextView mSellerIdView;
    private MyListView mProsView;
    private TextView mSendWayView;
    private EditText mCommentEdtView;
    private TextView mProsCountView;
    private TextView mProsPriceView;
    private TextView mCommitView;

    private OrderProductAdapter mProsAdapter;//订单商品的Adapter

    private User mUser;//当前用户
    private String mSellerId;//卖家ID
    private List<Product> mProducts;//商品列表
    private Order newOrder = new Order();//用于保存到服务器的订单

    private ProgressDialog mDialog;
    private boolean isClickPay = false;//判断是否多次点击购买
    //订单的网络请求API
    private OrderAPI mOrderAPI = new OrderAPIImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pay);
        app = (MyApplication) getApplication();
        mUser = app.getUser();
        Bundle bundle = getIntent().getExtras();
        mSellerId = bundle.getString("sellerId");//获得卖家ID
        mProducts = (List<Product>) bundle.getParcelableArrayList("products").get(0);//获得商品列表
        init();
        initListener();
        initView();
        BP.init(app.getAPPID());//初始化BmobPay对象
    }

    /**
     * 初始化组件
     */
    private void init(){
        mBackView = (ImageView) findViewById(R.id.iv_order_back);
        mUserNameView = (TextView) findViewById(R.id.tv_order_receiver);
        mUserPhoneView = (TextView) findViewById(R.id.tv_order_phone);
        mUserLocationView = (TextView) findViewById(R.id.tv_order_location);
        mSellerIdView = (TextView) findViewById(R.id.tv_order_seller_id);
        mProsView = (MyListView) findViewById(R.id.lv_order_pros);
        mSendWayView = (TextView) findViewById(R.id.tv_order_send_way);
        mCommentEdtView = (EditText) findViewById(R.id.et_order_comment);
        mProsCountView = (TextView) findViewById(R.id.tv_order_pros_count);
        mProsPriceView = (TextView) findViewById(R.id.tv_order_pros_price);
        mCommitView = (TextView) findViewById(R.id.tv_order_commit);
    }

    /**
     * 初始化事件
     */
    private void initListener(){
        mBackView.setOnClickListener(this);
        mSendWayView.setOnClickListener(this);
        mCommitView.setOnClickListener(this);
        mProsAdapter = new OrderProductAdapter(this, mProducts);
        mProsView.setAdapter(mProsAdapter);
    }

    /**
     * 初始化界面
     */
    private void initView(){
        mUserNameView.setText("收货人:" + mUser.getUsername());
        mUserPhoneView.setText(mUser.getPhone());
        mUserLocationView.setText("收货地址:" + mUser.getLocation());
        mSellerIdView.setText("卖家ID:" + mSellerId);
        mProsCountView.setText("共" + getProsCount() + "件商品");
        mProsPriceView.setText("￥" + getTotalPrice());
    }

    /**
     * 计算订单的总价
     * @return
     */
    private double getTotalPrice(){
        double totalPrice = 0;
        for(Product p : mProducts){
            totalPrice += p.getPrice() * p.getCount();
        }
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(totalPrice));
    }

    /**
     * 获得订单商品的数量
     * @return
     */
    private int getProsCount(){
        int count = 0;
        for(Product p : mProducts){
            count += p.getCount();
        }
        return count;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回按钮
            case R.id.iv_order_back:
                setResult(1);//支付失败
                finish();
                break;
            //选择发货方式
            case R.id.tv_order_send_way:
                showSendWayDialog();
                break;
            //提交订单
            case R.id.tv_order_commit:
//                Toast.makeText(this, "提交订单", Toast.LENGTH_SHORT).show();
                if(!isClickPay){
                    isClickPay = true;
                    pay();
//                    submitOrder();//绕过支付
                }
                break;
        }
    }

    /**
     * 调用支付
     */
    private void pay(){
        if(!checkAliPayInstalled()){
            Toast.makeText(this, "请手动安装支付宝!", Toast.LENGTH_SHORT).show();
            return;
        }
        showDialog("正在获取订单...\n");
        try{
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName("com.bmob.app.sport",
                    "com.bmob.app.sport.wxapi.BmobActivity");
            intent.setComponent(cn);
            startActivity(intent);
        }catch (Throwable e){
            e.printStackTrace();
        }
        String comment = mCommentEdtView.getText().toString();
        BP.pay(mSellerId, comment, getTotalPrice(), true, new PListener() {
            //无论成功与否，总是返回订单ID，仅保存到服务器数据库
            @Override
            public void orderId(String s) {
                newOrder.setOrderId(s);
                showDialog("获取订单成功!请等待跳转到支付界面!");
                hideDialog();
                Log.e("PayActivity", "orderId=" + newOrder.getOrderId());
            }

            //支付成功
            @Override
            public void succeed() {
                submitOrder();
                hideDialog();
            }

            //支付失败，用户中断支付或者网络故障
            @Override
            public void fail(int i, String s) {
                if(i == -3){//-3：没有安装BmobPlugin的插件
                    Toast.makeText(PayActivity.this,
                            "检测到您尚未安装支付插件，无法进行支付，将自动安装本地插件，稍后重新支付!",
                            Toast.LENGTH_SHORT).show();
                    installApk("bp.db");
                }else{//-2:代表用户中断支付操作
                    Toast.makeText(PayActivity.this, "支付中断!", Toast.LENGTH_SHORT).show();
                }
                hideDialog();
            }

            //因为网络等原因,支付结果未知(小概率事件)
            @Override
            public void unknow() {
                Toast.makeText(PayActivity.this, "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });
        isClickPay = false;
    }

    /**
     * 提交订单到购物车
     */
    private void submitOrder(){
        newOrder.setPhone(mUser.getPhone());
        newOrder.setTotalprice(getTotalPrice());
        newOrder.setComment(mCommentEdtView.getText().toString());
        newOrder.setUser_id(mSellerId);
        newOrder.setSendWay(mSendWayView.getText().toString());
        mOrderAPI.submitOrder(app.getToken(), newOrder, getProducts(), new DataListener<Result>() {
            @Override
            public void onComplete(Result result) {
                if(result.getResult().contains("success")){
                    setResult(0);//支付成功，则返回0
                    finish();
                }
                //显示支付结果
                Toast.makeText(PayActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获得商品列表的字符串，格式为：products格式为：ID|count,ID2|count2,
     * @return
     */
    private String getProducts(){
        String products = "";
        for(Product p : mProducts){
            products += p.getId() + "|" + p.getCount() + ",";
        }
        return products;
    }

    /**
     * 显示进度的Dialog
     * @param message
     */
    private void showDialog(String message){
        if(mDialog == null){
            mDialog = new ProgressDialog(this);
            mDialog.setCancelable(true);
        }
        mDialog.setMessage(message);
        mDialog.show();
    }

    /**
     * 隐藏Dialog
     */
    private void hideDialog(){
        if(mDialog != null && mDialog.isShowing()){
            try{
                mDialog.dismiss();
            }catch (Exception e){
            }
        }
    }

    private static final int REQUESTPERMISSION = 101;

    /**
     * 安装本地支付插件
     * @param s
     */
    private void installApk(String s){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTPERMISSION);
        } else {
            installBmobPayPlugin(s);
        }
    }

    /**
     * 安装assets里的apk文件
     * @param fileName
     */
    private void installBmobPayPlugin(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fileName + ".apk");
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 检测是否安装了支付宝
     * @return
     */
    private boolean checkAliPayInstalled(){
        try{
            getPackageManager().getPackageInfo("com.eg.android.AlipayGphone", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

    /**
     * 显示送货方式选择Dialog
     */
    private void showSendWayDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("请选择送货方式");
        final String[] sendWays = new String[]{"快递 到付", "上门取货"};
        dialog.setItems(sendWays, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSendWayView.setText(sendWays[which]);
            }
        });
        dialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            setResult(1);//支付失败
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
