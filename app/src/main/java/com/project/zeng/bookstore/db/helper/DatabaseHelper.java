package com.project.zeng.bookstore.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zeng on 2017/2/24.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    //数据库名字及其版本
    static final String DB_NAME = "book_store.db";
    static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE_SQL);
        db.execSQL(CREATE_RECOMMEND_TABLE_SQL);
        db.execSQL(CREATE_CATEGORY_TABLE_SQL);
        db.execSQL(CREATE_PRODUCT_TABLE_SQL);
//        db.execSQL(CREATE_ORDER_TABLE_SQL);
//        db.execSQL(CREATE_CART_TABLE_SQL);
        db.execSQL(CREATE_SEARCH_TABLE_SQL);
    }

    //表名
    public static final String TABLE_USER = "users";
    public static final String TABLE_CATEGORY = "categories";
    public static final String TABLE_PRODUCT = "products";
    public static final String TABLE_ORDER = "orders";
    public static final String TABLE_CART = "carts";
    public static final String TABLE_RECOMMEND = "recommends";
    public static final String TABLE_SEARCH = "search";

    //创建表的SQL语句
    private static final  String CREATE_USER_TABLE_SQL = "CREATE TABLE users( "
            + "id INTEGER PRIMARY KEY UNIQUE,"
            + "pictureUrl VARCHAR(80),"
            + "password VARCHAR(100),"
            + "username VARCHAR(30),"
            + "grade VARCHAR(30),"
            + "sex VARCHAR(10),"
            + "major VARCHAR(30),"
            + "location VARCHAR(50),"
            + "phone INTEGER NOT NULL,"
            + "more VARCHAR(100)"
            + ")";

    private static final String CREATE_CATEGORY_TABLE_SQL = "CREATE TABLE categories( "
            + "id INTEGER PRIMARY KEY UNIQUE,"
            + "name VARCHAR(30) NOT NULL,"
            + "suitableGrade VARCHAR(30) NOT NULL,"
            + "pictureUrl VARCHAR(80) NOT NULL,"
            + "describe VARCHAR(50) NOT NULL"
            + ")";

    private static final String CREATE_PRODUCT_TABLE_SQL = "CREATE TABLE products( "
            + "id INTEGER PRIMARY KEY UNIQUE,"
            + "category_id INTEGER NOT NULL,"
            + "seller_id INTEGER NOT NULL,"
            + "pictureUrl VARCHAR(80) NOT NULL,"
            + "title VARCHAR(30) NOT NULL,"
            + "author VARCHAR(30) NOT NULL,"
            + "press VARCHAR(50) NOT NULL,"
            + "count INTEGER NOT NULL,"
            + "price REAL NOT NULL,"
            + "describe VARCHAR(100) NOT NULL"
            + ")";

    private static final String CREATE_ORDER_TABLE_SQL = "CREATE TABLE orders( "
            + "id INTEGER PRIMARY KEY UNIQUE,"
            + "products VARCHAR(100) NOT NULL,"
            + "submit_time DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + "deliver_time DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + "state VARCHAR(20) NOT NULL,"
            + "phone INTEGER NOT NULL,"
            + "address VARCHAR(50) NOT NULL,"
            + "totalprice VARCHAR(20) NOT NULL,"
            + "comment VARCHAR(100)"
            + ")";

    private static final String CREATE_CART_TABLE_SQL = "CREATE TABLE carts( "
            + "id INTEGER PRIMARY KEY UNIQUE,"
            + "products VARCHAR(100) NOT NULL,"
            + "totalprice VARCHAR(20) NOT NULL"
            + ")";

    private static final String CREATE_RECOMMEND_TABLE_SQL = "CREATE TABLE recommends( "
            + "proId varchar(20) NOT NULL, "
            + "imgUrl varchar(200) NOT NULL,"
            + "proName VARCHAR(30) NOT NULL"
            + ")";

    private static final String CREATE_SEARCH_TABLE_SQL = "CREATE TABLE search( "
            + "id INTEGER PRIMARY KEY UNIQUE, "
            + "key varchar(20) NOT NULL"
            + ")";

    /**
     * 更新数据库时，先删除旧表后创建新表。
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_USER);
        db.execSQL("DROP TABLE " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE " + TABLE_ORDER);
        db.execSQL("DROP TABLE " + TABLE_CART);
        db.execSQL("DROP TABLE" + TABLE_RECOMMEND);
        db.execSQL("DROP TABLE" + TABLE_SEARCH);
        onCreate(db);
    }
}
