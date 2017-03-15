package com.project.zeng.bookstore.net;

import com.android.volley.Response.ErrorListener;
import com.project.zeng.bookstore.entities.Product;
import com.project.zeng.bookstore.listeners.DataListener;

import java.util.List;

/**
 * Created by zeng on 2017/3/2.
 * 获取商品的接口
 */

public interface ProductAPI {

    /**
     * 通过商品ID来获取商品
     * @param proId
     * @param listener
     */
    public void fetchProductByID(String proId, DataListener<Product> listener);

    /**
     * 通过商品类型的ID来获取商品
     * @param categoryId
     * @param listener
     */
    public void fetchProductsByCategory(String categoryId, DataListener<List<Product>> listener);

    /**
     * 通过页数来，加载更多商品
     * @param categoryId
     * @param listener
     * @param errorListener
     */
    public void loadMore(String categoryId, DataListener<List<Product>> listener, ErrorListener errorListener);

    /**
     * 通过关键字来加载商品（商品名称关键字）
     * @param word
     * @param listener
     */
    public void fetchProductsByWord(String word, DataListener<List<Product>> listener);

    /**
     * 通过适合年级来加载商品
     * @param grade
     * @param listener
     */
//    public void fetchProductsByGrade(String grade, DataListener<List<Product>> listener);
}
