package com.taotao.mapper;

import com.taotao.pojo.TbItemCat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbItemCatMapper {
    int insert(TbItemCat record);

    int insertSelective(TbItemCat record);

    List<TbItemCat> getCatByParentId(@Param("parentId") long ParentId);

}