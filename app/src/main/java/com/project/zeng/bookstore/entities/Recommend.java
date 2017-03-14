package com.project.zeng.bookstore.entities;

/**
 * 商品推荐栏的类
 */
public class Recommend {
    /**
     * 商品推荐类的域
     */
    private String proId;//跳转的商品的ID
    private String imgUrl;//图片的地址
    private String proName;//商品名称

    public Recommend(String id, String image, String name) {
        proId = id;
        imgUrl = image;
        proName = name;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }
}
