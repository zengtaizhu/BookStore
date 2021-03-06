package com.project.zeng.bookstore.net;

import com.project.zeng.bookstore.entities.Category;
import com.project.zeng.bookstore.entities.Result;
import com.project.zeng.bookstore.listeners.DataListener;

import java.util.List;

/**
 * Created by zeng on 2017/3/1.
 * 商品类型网络请求的接口
 */

public interface CategoryAPI {

    /**
     * 获取商品类型列表
     * @param listener
     */
    void fetchCategories(DataListener<List<Category>> listener);

    /**
     * 获得商品的适合年级
     * @param listener
     */
    void fetchGrades(DataListener<Result> listener);
}
