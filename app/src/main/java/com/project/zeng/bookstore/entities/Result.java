package com.project.zeng.bookstore.entities;

/**
 * Created by zeng on 2017/3/24.
 * 网络请求的Response
 */

public class Result {

    private String result;//结果：success/error
    private String message;//若成功，则显示对应的内容;若失败，则显示失败原因

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
