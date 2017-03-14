package com.project.zeng.bookstore.entities.list;

import com.project.zeng.bookstore.entities.Category;

import java.util.List;

/**
 * Created by zeng on 2017/3/1.
 * 商品类型的数组
 */

public class Categories {

    private int count;//商品类型总数目
    private List<Category> categories;//商品类型列表

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
