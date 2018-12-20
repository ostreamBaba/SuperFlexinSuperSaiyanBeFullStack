package com.taotao.mapper;

import com.taotao.pojo.TbItemParamItem;

public interface TbItemParamItemMapper {
    int insert(TbItemParamItem record);

    int insertSelective(TbItemParamItem record);

    TbItemParamItem selectByItemId(long itemId);

}