package com.taotao.rest.service;

import com.taotao.common.pojo.TaotaoResult;

/**
 * @ Create by ostreamBaba on 18-12-5
 * @ 描述
 */
public interface ItemService {

    TaotaoResult getItemBaseInfo(long itemId);

    TaotaoResult getItemDesc(long itemId);

    TaotaoResult getItemParam(long itemId);

}
