package com.project.zeng.bookstore.listeners;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by zeng on 2017/3/7.
 * 字体改变监听器
 */

public class TextChangeListener implements TextWatcher {

    //文字清除按钮
    private ImageView mImageView;

    private String oldText;//改变前的文字
    private int MaxPlaces = 20;//最大的位数

    public TextChangeListener(ImageView imageView, int maxPlaces) {
        this.mImageView = imageView;
        this.MaxPlaces = maxPlaces;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        Log.e("beforeTextChanged", "s=" + s + ",start=" + start + ",count=" + count + ",after=" + after);
        oldText = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        Log.e("onTextChanged", "s=" + s + ",start=" + start + ",before=" + before + ",count=" + count);
    }

    @Override
    public void afterTextChanged(Editable s) {
//        Log.e("TxtChangeListener", "s=" + s.toString());
        if(s.toString().equals("")){
            //若为空，则隐藏ImageView
            mImageView.setVisibility(View.INVISIBLE);
        }else{
            //若改变字体，则显示ImageView
            mImageView.setVisibility(View.VISIBLE);
        }
        //禁止输入中文
        for(int i = 0; i < s.length(); i++){
            if(isChina(s.charAt(i))){
                s.replace(0, s.length(), oldText, 0, oldText.length());
                break;
            }
        }
        //最大只允许输入20位
        if(s.length() > MaxPlaces){
            s.replace(0, s.length(), s, 0, MaxPlaces);
        }
    }

    /**
     * 判断字符是否是中文
     * @param c
     * @return
     */
    private boolean isChina(char c){
        if(Character.getType(c) == Character.OTHER_LETTER){
            return true;
        }
        return false;
    }
}
