package com.taotao.portal.pojo;

import com.taotao.pojo.TbItem;

/**
 * @ Create by ostreamBaba on 18-12-5
 * @ 描述
 */
public class ItemInfo extends TbItem{

    public String[] getImages(){
        String image = getImage();
        if(image != null){
            String[] images = image.split(",");
            return images;
        }
        return null;
    }

}
