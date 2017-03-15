package com.project.zeng.bookstore.net.Handler;

import com.project.zeng.bookstore.entities.Product;
import com.project.zeng.bookstore.entities.list.Products;

import java.util.List;

/**
 * Created by zeng on 2017/3/2.
 * 商品的Handler
 */

public class ProductHandler implements RespHandler<List<Product>, String>{

    /**
     * 用Gson解析出商品列表数据
     * @param data
     * @return
     */
    @Override
    public List<Product> parse(String data) {
        List<Product> products = null;
        products = gson.fromJson(data, Products.class).getProducts();
        return products;
    }

    /**
     * 用Gson解析出一个商品数据
     * @param data
     * @return
     */
    public static Product parseOne(String data){
        Product product = gson.fromJson(data, Product.class);
        return  product;
    }
}
