package com.project.zeng.bookstore.ui.frgm;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.MyApplication;
import com.project.zeng.bookstore.adapter.CategoryAdapter;
import com.project.zeng.bookstore.adapter.CategoryProductAdapter;
import com.project.zeng.bookstore.db.AbsDBAPI;
import com.project.zeng.bookstore.db.models.DbFactory;
import com.project.zeng.bookstore.entities.Category;
import com.project.zeng.bookstore.entities.Product;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.CategoryAPI;
import com.project.zeng.bookstore.net.ProductAPI;
import com.project.zeng.bookstore.net.impl.CategoryAPIImpl;
import com.project.zeng.bookstore.net.impl.ProductAPIImpl;
import com.project.zeng.bookstore.ui.ProductDetailActivity;
import com.project.zeng.bookstore.ui.SearchActivity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zeng on 2017/2/28.
 */

public class CategoryFragment extends Fragment implements OnItemClickListener, OnClickListener{

    private Context mContext;
    private MyApplication app;
    //组件
    private GridView mGridView;
    private ListView mListView;
    private EditText mSearchText;
    //适配器
    private CategoryAdapter mCategoryAdapter;
    private CategoryProductAdapter mProductAdapter;

    //商品类型网络请求API
    CategoryAPI mCategoryAPI = new CategoryAPIImpl();
    //商品网络请求API
    ProductAPI mProductAPI = new ProductAPIImpl();

    //操作商品类型的数据库对象
    AbsDBAPI<Category> mCatgDbAPI = DbFactory.createCategoryModel();
    //操作商品的数据库对象
    AbsDBAPI<Product> mProdDbAPI = DbFactory.createProductModel();

    //选中的商品类型的位置
    public static int mPosition = 0;

    private List<Category> mCategories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        mContext = getActivity().getApplication();
        app = (MyApplication) mContext;
        init(view);
        initListener();
        return view;
    }

    /**
     * 初始化组件
     */
    private void init(View view){
        mGridView = (GridView) view.findViewById(R.id.gv_category_product);
        mListView = (ListView) view.findViewById(R.id.lv_category);
        mSearchText = (EditText) view.findViewById(R.id.et_category_search);
    }

    /**
     * 初始化事件及其适配器
     */
    private void initListener(){
        mCategoryAdapter = new CategoryAdapter(getActivity().getApplication());
        mListView.setAdapter(mCategoryAdapter);
        mListView.setOnItemClickListener(this);
        mProductAdapter = new CategoryProductAdapter(getActivity().getApplication());
        mProductAdapter.setOnItemClickListener(new com.project.zeng.bookstore.listeners.OnItemClickListener<Product>() {
            @Override
            public void onClick(Product item) {
//                Log.e("CategoryFragment", item.getTitle());
                Intent intent = new Intent(getActivity().getApplicationContext(), ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", item);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mGridView.setAdapter(mProductAdapter);
        mSearchText.setOnClickListener(this);
    }

    /**
     * 获取数据
     */
    public void fetchCategories(){
        mCatgDbAPI.loadDatasFromDb(new DataListener<List<Category>>() {
            @Override
            public void onComplete(final List<Category> dbResult) {
                mCategoryAPI.fetchCategories(new DataListener<List<Category>>() {
                    @Override
                    public void onComplete(List<Category> result) {
                        if(null != result){
                            Log.e("CategoryFragment", "获得的Category数量为：" + result.size());
                            mCategories = result;
                            mCategoryAdapter.updateData(result);
                            //默认加载第一个商品类型
                            mListView.performItemClick(mListView.getChildAt(mPosition), mPosition, mListView.getItemIdAtPosition(mPosition));
                            mCatgDbAPI.saveItems(result);//保存数据到数据库
                        }else{//若网络请求失败或无返回数据，则加载数据库数据
                            if(null != dbResult || !app.getReceiver().isAvailable){
                                Log.e("CategoryFragment", "从数据库加载的category数量为:" + dbResult.size());
                                mCategories = dbResult;
                                mCategoryAdapter.updateData(dbResult);
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //记录选中的商品类型的位置
        mPosition = position;
        mCategoryAdapter.notifyDataSetChanged();
        fetchProductsByCategory(mCategories.get(position).getId());
    }

    /**
     * 通过商品类型ID来获取商品数据
     */
    private void fetchProductsByCategory(final String categoryId){
        //设置查询条件
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", 2);
        hashMap.put("key", categoryId);
        mProdDbAPI.loadDatasFromDbByArgs(new DataListener<List<Product>>() {
            @Override
            public void onComplete(List<Product> result) {
//                Log.e("CategoryFragment", "从数据库获取的Product数量=" + result.size());
                mProductAPI.fetchProductsByCategory(categoryId, new DataListener<List<Product>>() {
                    @Override
                    public void onComplete(List<Product> result) {
//                        Log.e("CategoryFragment", "从网络获取的Product数量=" + result.size());
                        mProductAdapter.updateData(result);
                        mProdDbAPI.saveItems(result);
                    }
                });
            }
        }, hashMap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_category_search:
                Intent intent = new Intent(getActivity().getApplication(), SearchActivity.class);
                startActivity(intent);
                break;
        }
    }
}
