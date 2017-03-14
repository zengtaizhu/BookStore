package com.project.zeng.bookstore.entities.list;

import com.project.zeng.bookstore.entities.Recommend;

import java.util.List;

/**
 * Created by zeng on 2017/2/28.
 * 商品推荐的数组
 */

public class Recommends {

    private int count;//总条目数
    private List<Recommend> recommends;//商品推荐的数组

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Recommend> getRecommends() {
        return recommends;
    }

    public void setRecommends(List<Recommend> recommends) {
        this.recommends = recommends;
    }
}
