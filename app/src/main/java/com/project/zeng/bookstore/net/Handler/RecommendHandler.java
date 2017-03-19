package com.project.zeng.bookstore.net.Handler;

import com.project.zeng.bookstore.entities.Recommend;
import com.project.zeng.bookstore.entities.list.Recommends;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeng on 2017/2/28.
 * 商品推荐网络响应的处理Handler
 */

public class RecommendHandler implements RespHandler<List<Recommend>,String> {

    /**
     * 使用Gson解析出数据
     * @param data
     * @return
     */
    @Override
    public List<Recommend> parse(String data) {
        List<Recommend> recommends = null;
        recommends = gson.fromJson(data, Recommends.class).getRecommends();
        return recommends;
    }
}
