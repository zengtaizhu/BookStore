package com.project.zeng.bookstore.db.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.project.zeng.bookstore.db.AbsDBAPI;
import com.project.zeng.bookstore.db.helper.DatabaseHelper;
import com.project.zeng.bookstore.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeng on 2017/3/10.
 */

public class UserModel extends AbsDBAPI<User>{

    public UserModel() {
        super(DatabaseHelper.TABLE_USER);
    }

    /**
     * 解析数据并返回数据
     * @param cursor
     * @return
     */
    @Override
    protected List<User> parseResult(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getString(0));
        user.setPictureUrl(cursor.getString(1));
        user.setPasswordOrToken(cursor.getString(2));
        user.setUsername(cursor.getString(3));
        user.setGrade(cursor.getString(4));
        user.setSex(cursor.getString(5));
        user.setMajor(cursor.getString(6));
        user.setLocation(cursor.getString(7));
        user.setPhone(cursor.getString(8));
        user.setMore(cursor.getString(9));
        List<User> users = new ArrayList<>();
        users.add(user);
        return users;
    }

    /**
     * 将数据保存到ContentValues中
     * @param item
     * @return
     */
    @Override
    protected ContentValues toContentValues(User item) {
        ContentValues newValues = new ContentValues();
        newValues.put("id", item.getId());
        newValues.put("pictureUrl", item.getPictureUrl());
        newValues.put("password", item.getPasswordOrToken());
        newValues.put("username", item.getUsername());
        newValues.put("grade", item.getGrade());
        newValues.put("sex", item.getSex());
        newValues.put("major", item.getMajor());
        newValues.put("location", item.getLocation());
        newValues.put("phone", item.getPhone());
        newValues.put("more", item.getMore());
        return newValues;
    }
}
