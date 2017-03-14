package com.project.zeng.bookstore.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.project.zeng.bookstore.db.cmd.Command;
import com.project.zeng.bookstore.db.engine.DbExecutor;
import com.project.zeng.bookstore.listeners.DataListener;

import java.util.List;

/**
 * Created by zeng on 2017/2/24.
 * 数据库操作的抽象类
 * @param <T>
 */

public abstract class AbsDBAPI<T> {

    //数据库操作引擎
    protected static DbExecutor sDbExecutor =  DbExecutor.getDbExecutor();
    //数据库表名
    protected String mTableName;

    public AbsDBAPI(String tableName){
        mTableName = tableName;
    }

    /**
     * 保存一行数据到数据库中
     * @param item
     */
    public void saveItem(final T item){
        sDbExecutor.execute(new Command.NoReturnCmd() {
            @Override
            protected Void doInBackground(SQLiteDatabase database) {
                database.insertWithOnConflict(mTableName, null, toContentValues(item),
                        SQLiteDatabase.CONFLICT_REPLACE);
                return null;
            }
        });
    }

    /**
     * 将数据加载到ContentProvider可访问的内存中
     * @param item
     * @return
     */
    protected ContentValues toContentValues(T item){
        return null;
    }

    /**
     * 保存所有数据到数据库（一行一行存放）
     * @param datas
     */
    public void saveItems(List<T> datas){
        for (T item:datas) {
            saveItem(item);
        }
    }

    /**
     * 从数据库中加载所有数据
     * @param listener
     */
    public void loadDatasFromDb(DataListener<List<T>> listener){
        sDbExecutor.execute(new Command<List<T>>(listener) {
            @Override
            protected List<T> doInBackground(SQLiteDatabase database) {
                Cursor cursor = database.query(mTableName, null, null, null,
                        null, null, loadDatasOrderBy());
                List<T> result = parseResult(cursor);
                cursor.close();
                return result;
            }
        });
    }

    /**
     * 根据参数，从数据库中加载所有数据
     * @param listener
     * @param args
     */
    public void loadDatasFromDbByArgs(DataListener<List<T>> listener, Object args){
        new Command<List<T>>(listener) {
            @Override
            protected List<T> doInBackground(SQLiteDatabase database) {
                Cursor cursor = database.query(mTableName, null, null, null,
                        null, null, loadDatasOrderBy());
                List<T> result = parseResult(cursor);
                cursor.close();
                return result;
            }
        };
    }

    /**
     * 返回排序的参数
     * @return
     */
    protected String loadDatasOrderBy(){
        return "";
    }

    /**
     * 从cursor解析出数据
     * @param cursor
     * @return
     */
    protected List<T> parseResult(Cursor cursor){
        return null;
    }

    /**
     * 按照条件删除数据库数据
     * @param whereArgs
     */
    public void deleteWithWhereArgs(final String whereArgs){
        sDbExecutor.execute(new Command<Void>(){
            @Override
            protected Void doInBackground(SQLiteDatabase database) {
                database.execSQL("delete from " + mTableName + whereArgs);
                return null;
            }
        });
    }

    /**
     * 删除数据库mTableName表的所有数据
     */
    public void deleteAll(){
        sDbExecutor.execute(new Command<Void>() {
            @Override
            protected Void doInBackground(SQLiteDatabase database) {
                database.execSQL("delete from " + mTableName);
                return null;
            }
        });
    }
}
