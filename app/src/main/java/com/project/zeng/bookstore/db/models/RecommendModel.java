package com.project.zeng.bookstore.db.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.project.zeng.bookstore.db.AbsDBAPI;
import com.project.zeng.bookstore.db.helper.DatabaseHelper;
import com.project.zeng.bookstore.entities.Recommend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeng on 2017/3/1.
 */

public class RecommendModel extends AbsDBAPI<Recommend> {

    public RecommendModel() {
        super(DatabaseHelper.TABLE_RECOMMEND);
    }

    /**
     * 解析数据并返回数据
     * @param cursor
     * @return
     */
    @Override
    protected List<Recommend> parseResult(Cursor cursor) {
        List<Recommend> recommends = new ArrayList<>();
        while(cursor.moveToNext()){
            String proId = cursor.getString(0);
            String imgUrl = cursor.getString(1);
            String proName = cursor.getString(2);
            //解析数据
            recommends.add(new Recommend(proId, imgUrl, proName));
        }
        return recommends;
    }

    /**
     * 将数据保存到ContentValues中
     * @param item
     * @return
     */
    @Override
    protected ContentValues toContentValues(Recommend item) {
        ContentValues newValues = new ContentValues();
        newValues.put("proId", item.getProId());
        newValues.put("imgUrl", item.getImgUrl());
        newValues.put("proName", item.getProName());
        return newValues;
    }
}
