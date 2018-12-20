package com.taotao.mapper;

import com.taotao.pojo.TbContent;

public interface TbContentMapper {
    int insert(TbContent record);

    int insertSelective(TbContent record);
}