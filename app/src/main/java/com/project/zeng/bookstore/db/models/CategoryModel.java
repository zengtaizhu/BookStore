package com.project.zeng.bookstore.db.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.project.zeng.bookstore.db.AbsDBAPI;
import com.project.zeng.bookstore.db.helper.DatabaseHelper;
import com.project.zeng.bookstore.entities.Category;
import com.project.zeng.bookstore.entities.Recommend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeng on 2017/3/1.
 */

public class CategoryModel extends AbsDBAPI<Category> {

    public CategoryModel() {
        super(DatabaseHelper.TABLE_CATEGORY);
    }

    /**
     * 解析数据并返回数据
     * @param cursor
     * @return
     */
    @Override
    protected List<Category> parseResult(Cursor cursor) {
        List<Category> categories = new ArrayList<>();
        while(cursor.moveToNext()){
            Category item = new Category();
            item.setId(cursor.getString(0));
            item.setName(cursor.getString(1));
            item.setSuitableGrade(cursor.getString(2));
            item.setPictureUrl(cursor.getString(3));
            item.setDescribe(cursor.getString(4));
            //解析数据
            categories.add(item);
        }
        return categories;
    }


    /**
     * 将数据保存到ContentValues中
     * @param item
     * @return
     */
    @Override
    protected ContentValues toContentValues(Category item) {
        ContentValues newValues = new ContentValues();
        newValues.put("id", item.getId());
        newValues.put("name", item.getName());
        newValues.put("suitableGrade", item.getSuitableGrade());
        newValues.put("pictureUrl", item.getPictureUrl());
        newValues.put("describe", item.getDescribe());
        return newValues;
    }
}
