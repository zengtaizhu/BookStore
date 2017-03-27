package com.project.zeng.bookstore.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.adapter.SearchAdapter;
import com.project.zeng.bookstore.db.AbsDBAPI;
import com.project.zeng.bookstore.db.models.DbFactory;
import com.project.zeng.bookstore.entities.Search;
import com.project.zeng.bookstore.listeners.DataListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeng on 2017/3/2.
 * 按关键字查找商品的Activity
 */

public class SearchActivity extends Activity implements OnClickListener, OnItemClickListener {

    //组件
    private ImageView mBackView;
    private EditText mSearchEditText;
    private TextView mSearchTextView;
    private ImageView mClearHistoryView;
    private ListView mSearchHistoryLv;

    //适配器
    SearchAdapter mSearchAdapter;

    //操作搜索记录的数据库对象
    AbsDBAPI<Search> mSearchDbAPI = DbFactory.createSearchModel();

    private List<Search> mSearches;//搜索记录列表
    private String SEARCH_BY_KEY = "1";//通过关键字搜索商品

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);
        init();
    }

    /**
     * 初始化组件
     */
    private void init() {
        mSearches = new ArrayList<>();
        mBackView = (ImageView) findViewById(R.id.iv_search_back);
//        mBackView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//            }
//        });
        mBackView.setOnClickListener(this);
        mSearchEditText = (EditText) findViewById(R.id.et_search_category);
        mSearchTextView = (TextView) findViewById(R.id.tv_search);
        mSearchTextView.setOnClickListener(this);
        mClearHistoryView = (ImageView) findViewById(R.id.iv_search_clear_history);
        mClearHistoryView.setOnClickListener(this);
        mSearchHistoryLv = (ListView) findViewById(R.id.lv_search_history);
        mSearchAdapter = new SearchAdapter(this);
        mSearchHistoryLv.setAdapter(mSearchAdapter);
        mSearchHistoryLv.setOnItemClickListener(this);
        loadSearchHistoryFromDB();
    }

    /**
     * 从数据库获取搜索记录
     */
    private void loadSearchHistoryFromDB() {
        mSearchDbAPI.loadDatasFromDb(new DataListener<List<Search>>() {
            @Override
            public void onComplete(List<Search> result) {
                if (null != result) {
                    Log.e("SearchActivity", "从数据库加载Search的数量为:" + result.size());
                    mSearches = result;
                    mSearchAdapter.updateData(result);
                } else {
                    Log.e("SearchActivity", "从数据库加载Search的数量为:0");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search_back:
                //结束当前Activity
                finish();
                break;
            case R.id.tv_search:
                //获得搜索框的内容
                String keyWord = mSearchEditText.getText().toString().trim();
                if (!keyWord.equals("")) {
                    if (!isExist(keyWord)) {
                        addSearchHistoryToDB(keyWord);
                    }
                    mSearchEditText.setText("");
                    //跳转到Product的Activity
                    Intent intent = new Intent(SearchActivity.this, ProductsActivity.class);
                    intent.putExtra("key", keyWord);
                    //商品搜索类型：SEARCH_BY_CODE--代表通过关键字查找商品
                    intent.putExtra("type", SEARCH_BY_KEY);
                    startActivity(intent);
//                    Toast.makeText(this, "keyWord=" + keyWord, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "请输入关键字!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_search_clear_history:
                mSearchDbAPI.deleteAll();
                mSearchAdapter.clearData();
                break;
        }
    }

    /**
     * 判断是否记录存在
     *
     * @param key
     * @return
     */
    private boolean isExist(String key) {
        for (Search search : mSearches) {
            if (search.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将搜索记录添加到数据库
     *
     * @param keyWord
     */
    private void addSearchHistoryToDB(String keyWord) {
        mSearchDbAPI.saveItem(new Search(mSearches.size() + 1, keyWord));
        loadSearchHistoryFromDB();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(SearchActivity.this, ProductsActivity.class);
        intent.putExtra("key", mSearches.get(position).getKey());
        //商品搜索类型：0--代表通过关键字查找商品
        intent.putExtra("type", "0");
        startActivity(intent);
//        Toast.makeText(this, "keyWord=" + mSearches.get(position).getKey(), Toast.LENGTH_SHORT).show();
    }
}
