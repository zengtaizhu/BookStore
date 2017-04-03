package com.project.zeng.bookstore.net.impl;

import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.zeng.bookstore.entities.Product;
import com.project.zeng.bookstore.entities.Result;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.Handler.ProductHandler;
import com.project.zeng.bookstore.net.Handler.ResultHandler;
import com.project.zeng.bookstore.net.ProductAPI;
import com.project.zeng.bookstore.utils.ImageToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zeng on 2017/3/2.
 * 商品网络请求的实现类
 */

public class ProductAPIImpl extends AbsNetwork<List<Product>, String> implements ProductAPI{

    private static String url = "http://192.168.191.1:5000/api/v1.0/products/";

    public ProductAPIImpl() {
        mRespHandler = new ProductHandler();
    }

    @Override
    public void fetchProductByID(String proId, final DataListener<Product> listener) {
        String realUrl = url + proId;
        StringRequest request = new StringRequest(realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.e("ProductAPIImplByID", "response=" + response);
                if(listener != null){
                    listener.onComplete(ProductHandler.parseOne(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("ProductAPIImpl", error.getMessage());
            }
        });
        //将请求添加到网络请求队列中
        performRequest(request);
    }

    @Override
    public void fetchProductsByCategory(String categoryId, final DataListener<List<Product>> listener) {
        String realUrl = url + "category/" + categoryId;
        StringRequest request = new StringRequest(realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.e("ProductAPIImpl", "response=" + response);
                if(listener != null){
                    listener.onComplete(mRespHandler.parse(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("ProductAPIImpl", error.getMessage());
            }
        });
        //将请求添加到网络请求队列中
        performRequest(request);
    }

    /**
     * 通过关键字，获得商品列表
     * @param word
     * @param listener
     */
    @Override
    public void fetchProductsByWord(String word, final DataListener<List<Product>> listener) {
        String realUrl = url + "keyword/" + word;
        StringRequest request = new StringRequest(realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(null != listener){
//                    Log.e("ProductAPIImpl", "response=" + response);
                    listener.onComplete(mRespHandler.parse(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("ProductAPIImpl", error.getMessage());
            }
        });
        //将网络请求添加到队列中
        performRequest(request);
    }

    /**
     * 获得商品推荐
     * @param listener
     */
    @Override
    public void fetchRecommends(final DataListener<List<Product>> listener) {
        String realUrl = url + "recommend/";
        StringRequest request = new StringRequest(realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(null != listener){
//                    Log.e("ProductAPIImpl", "response=" + response);
                    listener.onComplete(mRespHandler.parse(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("ProductAPIImpl", error.getMessage());
                listener.onComplete(null);
            }
        });
        //将网络请求添加到队列中
        performRequest(request);
    }

    /**
     * 通过卖家ID，获得商品列表
     * @param sellerId
     * @param listener
     */
    @Override
    public void fetchProductBySeller(String sellerId, final DataListener<List<Product>> listener) {
        String realUrl = url + "seller/" + sellerId;
        StringRequest request = new StringRequest(realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(null != listener){
//                    Log.e("ProductAPIImpl", "response=" + response);
                    listener.onComplete(mRespHandler.parse(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("ProductAPIImpl", error.getMessage());
                listener.onComplete(null);
            }
        });
        //将网络请求添加到队列中
        performRequest(request);
    }

    /**
     * 删除商品
     * @param token
     * @param proId
     * @param listener
     */
    @Override
    public void deleteProduct(final String token, final String proId, final DataListener<Result> listener) {
        String realUrl = url + "delete/";
        StringRequest request = new StringRequest(Request.Method.POST, realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(null != listener){
//                    Log.e("ProductAPIImpl", "delete:response=" + response);
                    listener.onComplete(ResultHandler.getResult(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("ProductAPIImpl", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("id", proId);
                return params;
            }
        };
        //将网络请求添加到队列中
        performRequest(request);
    }

    /**
     * 修改商品信息
     * @param token
     * @param product
     * @param listener
     */
    @Override
    public void modifyProduct(final String token, final Product product, final DataListener<Result> listener) {
        String realUrl = url + "modify/";
        StringRequest request = new StringRequest(Request.Method.POST, realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(null != listener){
                    Log.e("ProductAPIImpl", "modify:response=" + response);
                    listener.onComplete(ResultHandler.getResult(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("ProductAPIImpl", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("id", product.getId());
                params.put("title", product.getTitle());
                params.put("author", product.getAuthor());
                params.put("press", product.getPress());
                params.put("count", product.getCount() + "");
                params.put("price", product.getPrice() + "");
                params.put("describe", product.getDescribe());
                return params;
            }
        };
        //将网络请求添加到队列中
        performRequest(request);
    }

    /**
     * 修改商品图片
     * @param token
     * @param proId
     * @param bitmap
     * @param listener
     */
    @Override
    public void modifyProductImg(final String token, final String proId, final Bitmap bitmap, final DataListener<Result> listener) {
        String realUrl = url + "modifyImg/";
        StringRequest request = new StringRequest(Request.Method.POST, realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(null != listener){
                    Log.e("ProductAPIImpl", "modify:response=" + response);
                    listener.onComplete(ResultHandler.getResult(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("ProductAPIImpl", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("id", proId);
                params.put("image", ImageToString.change(bitmap));
                return params;
            }
        };
        //将网络请求添加到队列中
        performRequest(request);
    }

    /**
     * 添加商品
     * @param token
     * @param product
     * @param bitmap
     * @param listener
     */
    @Override
    public void addProduct(final String token, final Product product, final Bitmap bitmap, final DataListener<Result> listener) {
        String realUrl = url + "add/";
        StringRequest request = new StringRequest(Request.Method.POST, realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(null != listener){
                    Log.e("ProductAPIImpl", "modify:response=" + response);
                    listener.onComplete(ResultHandler.getResult(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("ProductAPIImpl", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("image", ImageToString.change(bitmap));
                params.put("title", product.getTitle());
                params.put("author", product.getAuthor());
                params.put("press", product.getPress());
                params.put("count", product.getCount() + "");
                params.put("price", product.getPrice() + "");
                params.put("describe", product.getDescribe());
                params.put("categoryId", product.getCategoryId());
                return params;
            }
        };
        //将网络请求添加到队列中
        performRequest(request);
    }
}
