package com.project.zeng.bookstore.db.models;

import com.project.zeng.bookstore.db.AbsDBAPI;
import com.project.zeng.bookstore.entities.Cart;
import com.project.zeng.bookstore.entities.Category;
import com.project.zeng.bookstore.entities.Order;
import com.project.zeng.bookstore.entities.Product;
import com.project.zeng.bookstore.entities.Recommend;
import com.project.zeng.bookstore.entities.Search;
import com.project.zeng.bookstore.entities.User;

/**
 * Created by zeng on 2017/2/25.
 * 数据库API工厂
 */

public class DbFactory {

    public static AbsDBAPI<User> createUserModel(){
        return new UserModel();
    }

    public static AbsDBAPI<Category> createCategoryModel(){
        return new CategoryModel();
    }

    public static AbsDBAPI<Product> createProductModel(){
        return  new ProductModel();
    }

//    public static AbsDBAPI<Order> createOrderModel(){
//        return new OrderModel();
//    }
//
//    public static AbsDBAPI<Cart> createCartModel(){
//        return new CartModel();
//    }

    public static AbsDBAPI<Recommend> createRecommendModel(){
        return new RecommendModel();
    }

    public static AbsDBAPI<Search> createSearchModel(){
        return new SearchModel();
    }
}
