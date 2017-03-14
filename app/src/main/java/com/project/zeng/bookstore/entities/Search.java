package com.project.zeng.bookstore.entities;

/**
 * Created by zeng on 2017/3/2.
 */

public class Search {
    /**
     * Search的域
     */
    private int id;
    private String key;

    public Search(int id, String key) {
        this.id = id;
        this.key = key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
