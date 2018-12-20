package com.taotao.search.mapper;

import com.taotao.search.pojo.Item;

import java.util.List;

/**
 * @ Create by ostreamBaba on 18-11-28
 * @ 描述
 */


public interface ItemMapper {

    List<Item> getItemList();

    Item getItemById(long id);

}
