package com.project.zeng.bookstore.net.Handler;

import com.project.zeng.bookstore.entities.Cart;
import com.project.zeng.bookstore.entities.list.Carts;

import java.util.List;

/**
 * Created by zeng on 2017/3/18.
 * 购物车网络请求的Handler
 */

public class CartHandler implements RespHandler<List<Cart>, String>{

    /**
     * 解析数据，获得购物车列表
     * @param data
     * @return
     */
    @Override
    public List<Cart> parse(String data) {
        List<Cart> carts = gson.fromJson(data, Carts.class).getCarts();
        return carts;
    }
}
