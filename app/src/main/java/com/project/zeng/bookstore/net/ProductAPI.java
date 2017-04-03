package com.project.zeng.bookstore.net;

import android.graphics.Bitmap;

import com.project.zeng.bookstore.entities.Product;
import com.project.zeng.bookstore.entities.Result;
import com.project.zeng.bookstore.listeners.DataListener;

import java.util.List;

/**
 * Created by zeng on 2017/3/2.
 * 商品网络请求的接口
 */

public interface ProductAPI {

    /**
     * 通过商品ID来获取商品
     * @param proId
     * @param listener
     */
    void fetchProductByID(String proId, DataListener<Product> listener);

    /**
     * 通过商品类型的ID来获取商品
     * @param categoryId
     * @param listener
     */
    void fetchProductsByCategory(String categoryId, DataListener<List<Product>> listener);

    /**
     * 通过关键字来加载商品（商品名称关键字）
     * @param word
     * @param listener
     */
    void fetchProductsByWord(String word, DataListener<List<Product>> listener);

    /**
     * 加载广告
     * @param listener
     */
    void fetchRecommends(DataListener<List<Product>> listener);

    /**
     * 通过卖家ID，获得商品列表
     * @param sellerId
     * @param listener
     */
    void fetchProductBySeller(String sellerId, DataListener<List<Product>> listener);

    /**
     * 删除商品
     * @param token
     * @param proId
     * @param listener
     */
    void deleteProduct(String token, String proId, DataListener<Result> listener);

    /**
     * 修改商品信息
      * @param token
     * @param product
     * @param listener
     */
    void modifyProduct(String token, Product product, DataListener<Result> listener);

    /**
     * 修改商品图片
     * @param token
     * @param proId
     * @param bitmap
     * @param listener
     */
    void modifyProductImg(String token, String proId, Bitmap bitmap, DataListener<Result> listener);

    /**
     * 添加商品
     * @param token
     * @param product
     * @param bitmap
     * @param listener
     */
    void addProduct(String token, Product product, Bitmap bitmap, DataListener<Result> listener);
}
