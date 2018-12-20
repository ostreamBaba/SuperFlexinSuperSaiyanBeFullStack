package com.taotao.service;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;


/**
 * @ Create by ostreamBaba on 18-11-27
 * @ 描述
 */

public interface ItemService {

    TbItem getItemById(long itemId);

    EUDataGridResult<TbItem> getItemList(int page, int rows);

    TaotaoResult createItem(TbItem tbItem, String desc) throws Exception;

}
