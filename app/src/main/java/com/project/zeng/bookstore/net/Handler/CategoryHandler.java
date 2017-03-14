package com.project.zeng.bookstore.net.Handler;

import com.project.zeng.bookstore.entities.Category;
import com.project.zeng.bookstore.entities.list.Categories;

import java.util.List;

/**
 * Created by zeng on 2017/3/1.
 * 商品类型的Handler
 */

public class CategoryHandler implements RespHandler<List<Category>, String> {

    /**
     * 使用Gson解析出商品类型数据
     * @param data
     * @return
     */
    @Override
    public List<Category> parse(String data) {
        List<Category> categories = null;
        categories = gson.fromJson(data, Categories.class).getCategories();
        return categories;
    }
}
