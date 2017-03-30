package com.project.zeng.bookstore.net;

import com.project.zeng.bookstore.entities.Category;
import com.project.zeng.bookstore.entities.Recommend;
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
}
