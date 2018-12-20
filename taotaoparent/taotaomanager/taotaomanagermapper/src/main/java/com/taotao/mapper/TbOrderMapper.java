package com.taotao.mapper;

import com.taotao.pojo.TbOrder;

public interface TbOrderMapper {
    int insert(TbOrder record);

    int insertSelective(TbOrder record);
}