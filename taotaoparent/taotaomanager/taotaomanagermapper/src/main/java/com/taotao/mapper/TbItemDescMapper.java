package com.taotao.mapper;

import com.taotao.pojo.TbItemDesc;

public interface TbItemDescMapper {
    int insert(TbItemDesc record);

    int insertSelective(TbItemDesc record);

    TbItemDesc selectItemDescById(long itemId);


}