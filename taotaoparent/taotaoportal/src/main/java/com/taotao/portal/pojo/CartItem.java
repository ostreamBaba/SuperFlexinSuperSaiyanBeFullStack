package com.taotao.portal.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @ Create by ostreamBaba on 18-12-9
 * @ 减少不需要的数据
 */


public class CartItem {

    private long id;

    private String title;

    private Integer num;

    private long price;

    private String image;

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getNum() {
        return num;
    }

    public long getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @JsonIgnore //序列化成json数据的时候忽略此方法
    public String[] getImages(){
        if(image != null){
            return image.split(",");
        }
        return null;
    }

}
