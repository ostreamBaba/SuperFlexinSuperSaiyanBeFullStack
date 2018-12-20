package com.taotao.mapper;

import com.taotao.pojo.TbItemParam;

public interface TbItemParamMapper {
    int insert(TbItemParam record);

    int insertSelective(TbItemParam record);
}