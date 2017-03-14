package com.project.zeng.bookstore.db.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.mob.tools.utils.SQLiteHelper;
import com.project.zeng.bookstore.db.AbsDBAPI;
import com.project.zeng.bookstore.db.helper.DatabaseHelper;
import com.project.zeng.bookstore.entities.Search;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeng on 2017/3/2.
 */

public class SearchModel extends AbsDBAPI<Search> {

    public SearchModel() {
        super(DatabaseHelper.TABLE_SEARCH);
    }

    @Override
    protected List<Search> parseResult(Cursor cursor) {
        List<Search> searches = new ArrayList<>();
        while(cursor.moveToNext()) {
            searches.add(new Search(cursor.getInt(0), cursor.getString(1)));
        }
        return searches;
    }

    @Override
    protected ContentValues toContentValues(Search item) {
        ContentValues newValues = new ContentValues();
        newValues.put("id", item.getId());
        newValues.put("key", item.getKey());
        return newValues;
    }
}
