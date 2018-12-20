package com.taotao.mapper;

import com.taotao.pojo.TbContentCategory;

public interface TbContentCategoryMapper {
    int insert(TbContentCategory record);

    int insertSelective(TbContentCategory record);
}