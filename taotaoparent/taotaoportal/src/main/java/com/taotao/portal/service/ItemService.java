package com.taotao.portal.service;

import com.taotao.portal.pojo.ItemInfo;

/**
 * @ Create by ostreamBaba on 18-12-5
 * @ 描述
 */
public interface ItemService {

    ItemInfo getItemById(Long itemId);

    String getItemDescById(Long itemId);

    String getItemParam(Long itemId);

}
