package com.taotao.mapper;

import com.taotao.pojo.TbOrderItem;

public interface TbOrderItemMapper {
    int insert(TbOrderItem record);

    int insertSelective(TbOrderItem record);
}