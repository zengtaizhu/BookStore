package com.project.zeng.bookstore.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by zeng on 2017/4/3.
 * 将图片转化为字符串的工具类
 */

public class ImageToString {

    /**
     * 将图片转化为字符串
     * @param bmp
     * @return
     */
    public static String change(Bitmap bmp){
        if(null == bmp){
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();//转为Byte数组
        String str = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return str;
    }
}
