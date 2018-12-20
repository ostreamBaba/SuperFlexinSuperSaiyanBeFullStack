package com.taotao.mapper;

import com.taotao.pojo.TbOrderShipping;

public interface TbOrderShippingMapper {
    int insert(TbOrderShipping record);

    int insertSelective(TbOrderShipping record);
}