package com.project.zeng.bookstore.db.cmd;

import android.database.sqlite.SQLiteDatabase;

import com.project.zeng.bookstore.db.helper.DatabaseMgr;
import com.project.zeng.bookstore.entities.Cart;
import com.project.zeng.bookstore.entities.Category;
import com.project.zeng.bookstore.entities.Order;
import com.project.zeng.bookstore.entities.Product;
import com.project.zeng.bookstore.entities.Recommend;
import com.project.zeng.bookstore.entities.User;
import com.project.zeng.bookstore.listeners.DataListener;

import java.util.List;

/**
 * Created by zeng on 2017/2/24.
 * 数据库执行的命令的抽象类
 * @param <T>
 */

public abstract class Command<T> {

    public DataListener<T> dataListener;

    public Command() {
    }

    public Command(DataListener<T> listener){
        this.dataListener = listener;
    }

    public final T execute(){
        SQLiteDatabase database = DatabaseMgr.getDatabase();
        DatabaseMgr.beginTransaction();
        T result = doInBackground(database);
        DatabaseMgr.setTransactionSuccess();
        DatabaseMgr.endTransaction();
        database.releaseReference();
        return result;
    }

    /**
     * 查询数据的抽象接口，用于加载数据库数据
     * @param database
     * @return
     */
    protected abstract T doInBackground(SQLiteDatabase database);

    /**
     * 无返回值的数据库查询命令
     */
    public static abstract class NoReturnCmd extends Command<Void>{
    }

    /**
     * 返回用户列表的数据库命令
     */
//    public static abstract class UsersCommand extends Command<List<User>>{
//        public UsersCommand(DataListener<List<User>> listener){
//            dataListener = listener;
//        }
//    }

    /**
     * 返回商品类型列表的数据库命令
     */
    public static abstract class CategoriesCommand extends Command<List<Category>>{
        public CategoriesCommand(DataListener<List<Category>> listener){
            dataListener = listener;
        }
    }

    /**
     * 返回商品列表的数据库命令
     */
    public static abstract class ProductsCommand extends Command<List<Product>>{
        public ProductsCommand(DataListener<List<Product>> listener){
            dataListener = listener;
        }
    }

    /**
     * 返回购物车列表的数据库命令
     */
//    public static abstract class CartsCommand extends Command<List<Cart>>{
//        public CartsCommand(DataListener<List<Cart>> listener){
//            dataListener = listener;
//        }
//    }

    /**
     * 返回订单列表的数据库命令
     */
//    public static abstract class OrdersCommand extends Command<List<Order>>{
//        public OrdersCommand(DataListener<List<Order>> listener){
//            dataListener = listener;
//        }
//    }

    /**
     * 返回商品推荐列表
     */
    public static abstract class RecommendCommand extends Command<List<Recommend>>{
        public RecommendCommand(DataListener<List<Recommend>> listener){
            dataListener = listener;
        }
    }
}
