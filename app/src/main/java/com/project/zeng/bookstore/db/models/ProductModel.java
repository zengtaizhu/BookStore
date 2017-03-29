package com.project.zeng.bookstore.db.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.project.zeng.bookstore.db.AbsDBAPI;
import com.project.zeng.bookstore.db.cmd.Command;
import com.project.zeng.bookstore.db.helper.DatabaseHelper;
import com.project.zeng.bookstore.entities.Product;
import com.project.zeng.bookstore.listeners.DataListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zeng on 2017/3/2.
 * 商品的数据库model
 */

public class ProductModel extends AbsDBAPI<Product> {

    //选择加载Product的方式：0：全部，1：商品ID，2：商品类型ID
    private static int type = 0;
    private static String whereArgs;

    private String price_asc = "price";//价格升序
    private String price_desc = "price desc";//价格降序
    private String author = "author";//作者排序

    public ProductModel() {
        super(DatabaseHelper.TABLE_PRODUCT);
    }

    /**
     * 解析数据并返回数据
     * @param cursor
     * @return
     */
    @Override
    protected List<Product> parseResult(Cursor cursor) {
        List<Product> products = new ArrayList<>();
        while(cursor.moveToNext()){
            Product product = new Product();
            product.setId(cursor.getString(0));
            product.setCategoryId(cursor.getString(1));
            product.setSellerId(cursor.getString(2));
            product.setPictureUrl(cursor.getString(3));
            product.setTitle(cursor.getString(4));
            product.setAuthor(cursor.getString(5));
            product.setPress(cursor.getString(6));
            product.setCount(cursor.getInt(7));
            product.setPrice(cursor.getDouble(8));
            product.setDescribe(cursor.getString(9));
            //解析数据
            products.add(product);
        }
        return products;
    }

    /**
     * 将数据保存到ContentValues中
     * @param item
     * @return
     */
    @Override
    protected ContentValues toContentValues(Product item) {
        ContentValues newValues = new ContentValues();
        newValues.put("id", item.getId());
        newValues.put("category_id", item.getCategoryId());
        newValues.put("seller_id", item.getSellerId());
        newValues.put("pictureUrl", item.getPictureUrl());
        newValues.put("title", item.getTitle());
        newValues.put("author", item.getAuthor());
        newValues.put("press", item.getPress());
        newValues.put("count", item.getCount());
        newValues.put("price", item.getPrice());
        newValues.put("describe", item.getDescribe());
        return newValues;
    }

    @Override
    public void loadDatasFromDb(DataListener<List<Product>> listener) {
        loadDatasFromDbByArgs(listener, null);
    }

    /**
     * args格式为：HashMap{"type":0, "key":2}
     * key：关键字，type(0:所有，1:关键字，2:商品类型ID、3:商品ID、4:适合年级)
     * @param listener
     * @param args
     */
    @Override
    public void loadDatasFromDbByArgs(DataListener<List<Product>> listener, final Object args) {
        if(listener != null){
            sDbExecutor.execute(new Command.ProductsCommand(listener) {
                @Override
                protected List<Product> doInBackground(SQLiteDatabase database) {
                    if(args != null){
                        analysisHashMapArgs(args);
                    }
                    List<Product> products = findProductsFromDB(database);
                    return products;
                }
            });
        }
    }



    /**
     * 解析数据
     * @param args
     */
    private void analysisHashMapArgs(Object args){
        HashMap<String, Object> map = (HashMap<String, Object>) args;
        type = Integer.valueOf(map.get("type").toString());
        whereArgs = map.get("key").toString();
    }



    /**
     * 此处根据选择，从数据库加载Product数据
     * type 0:所有，1:关键字，2:商品类型ID、3:商品ID
     */
    private List<Product> findProductsFromDB(SQLiteDatabase database){
        List<Product> products;
        switch (type){
            case 0:
                //加载所有Product数据
                products = loadAllProducts(database);
                break;
            case 1:
                //通过关键字加载Product数据
                products = loadProductsByKeyWord(database);
                break;
            case 2:
                //通过商品类型ID加载Product数据
                products = loadProductByCategory(database);
                break;
            case 3:
                //通过商品ID加载Product数据
                products = loadProductByID(database);
                break;
            default:
                products = new ArrayList<>();
                break;
            }
        return products;
    }

    /**
     * 加载所有Product
     * @param database
     * @return
     */
    private List<Product> loadAllProducts(SQLiteDatabase database){
        Cursor cursor = database.rawQuery("select * from " + DatabaseHelper.TABLE_PRODUCT, null);
        return parseResult(cursor);
    }


    /**
     * 通过关键字，加载Product
     * @param database
     * @return
     */
    private List<Product> loadProductsByKeyWord(SQLiteDatabase database){
        Cursor cursor = database.rawQuery("select * from " + DatabaseHelper.TABLE_PRODUCT
            + " where title like ? and author like ?", new String[]{"%" + whereArgs + "%", "%" + whereArgs + "%"});
        return parseResult(cursor);
    }

    /**
     * 根据ID加载Product
     * @param database
     * @return
     */
    private List<Product> loadProductByID(SQLiteDatabase database){
//        Log.e("ProductModel", "whereArgs=" + whereArgs);
        Cursor cursor = database.rawQuery("select * from " + DatabaseHelper.TABLE_PRODUCT
            + " where id = ?", new String[]{whereArgs});
        return parseResult(cursor);
    }

    /**
     *  根据商品类型ID加载Product
     * @param database
     * @return
     */
    private List<Product> loadProductByCategory(SQLiteDatabase database){
        Cursor cursor = database.rawQuery("select * from " + DatabaseHelper.TABLE_PRODUCT
                + " where category_id = ?", new String[]{whereArgs});
        return parseResult(cursor);
    }

    /**
     * args格式为：HashMap{"type":0}
     * type(0:默认，1:价格升序，2:价格降序、3:作者名)
     * @param listener
     * @param args
     */
    @Override
    public void loadDatasFromDbByOrder(DataListener<List<Product>> listener, final Object args) {
        if(listener != null){
            sDbExecutor.execute(new Command.ProductsCommand(listener) {
                @Override
                protected List<Product> doInBackground(SQLiteDatabase database) {
                    if(args != null){
                        analysisOrder(args);
                    }
                    List<Product> products = findProductsFromDBByOrder(database);
                    return products;
                }
            });
        }
    }

    /**
     * 解析数据
     * @param args
     */
    private void analysisOrder(Object args){
        HashMap<String, Integer> map = (HashMap<String, Integer>) args;
        type = map.get("type");
        Log.e("ProductModel", "type = " + type);
    }


    /**
     * 此处根据选择，从数据库按顺序加载Product数据
     * type 0:默认，1:价格升序，2:价格降序、3:作者名排序
     */
    private List<Product> findProductsFromDBByOrder(SQLiteDatabase database){
        List<Product> products;
        switch (type){
            case 0:
                //默认排序，加载Product数据
                products = loadProductsByDefault(database);
                break;
            case 1:
                //按价格升序，加载Product数据
                products = loadProductsByPriceAsc(database);
                break;
            case 2:
                //按价格降序，加载Product数据
                products = loadProductByPriceDsc(database);
                break;
            case 3:
                //按作者排序，加载Product数据
                products = loadProductByAuthor(database);
                break;
            default:
                products = null;
                break;
        }
        return products;
    }

    /**
     * 默认排序（ID），加载Product
     * @param database
     * @return
     */
    private List<Product> loadProductsByDefault(SQLiteDatabase database){
        Cursor cursor = database.rawQuery("select * from " + DatabaseHelper.TABLE_PRODUCT, null);
        return parseResult(cursor);
    }

    /**
     * 价格升序，加载Product
     * @param database
     * @return
     */
    private List<Product> loadProductsByPriceAsc(SQLiteDatabase database){
        Cursor cursor = database.rawQuery("select * from " + DatabaseHelper.TABLE_PRODUCT
                + " order by " + price_asc, null);
        return parseResult(cursor);
    }

//    private List<Product> loadProductsByPriceAsc(SQLiteDatabase database){
    //按照关键字
//        Cursor cursor = database.rawQuery("select * from " + DatabaseHelper.TABLE_PRODUCT
//                + " where title like ? and author like ?", new String[]{"%" + whereArgs + "%", "%" + whereArgs + "%"});
//        return parseResult(cursor);
//    }

    /**
     * 价格降序，加载Product
     * @param database
     * @return
     */
    private List<Product> loadProductByPriceDsc(SQLiteDatabase database){
        Cursor cursor = database.rawQuery("select * from " + DatabaseHelper.TABLE_PRODUCT
                + " order by " + price_desc, null);
        return parseResult(cursor);
    }

    /**
     *  作者排序，加载Product
     * @param database
     * @return
     */
    private List<Product> loadProductByAuthor(SQLiteDatabase database){
        Cursor cursor = database.rawQuery("select * from " + DatabaseHelper.TABLE_PRODUCT
                + " order by " + author, null);
        return parseResult(cursor);
    }
}
