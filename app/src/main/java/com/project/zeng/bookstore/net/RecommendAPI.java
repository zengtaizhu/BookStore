package com.project.zeng.bookstore.net;

import com.project.zeng.bookstore.entities.Recommend;
import com.project.zeng.bookstore.listeners.DataListener;

import java.util.List;

/**
 * Created by zeng on 2017/2/20.
 * 首页商品推荐网络请求的接口
 */
public interface RecommendAPI {
    /**
     * 获取商品推荐列表
     * @param listener
     */
    public void fetchRecommends(DataListener<List<Recommend>> listener);
}
