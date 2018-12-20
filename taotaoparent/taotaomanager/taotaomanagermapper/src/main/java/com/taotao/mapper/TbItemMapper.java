package com.taotao.mapper;

import com.taotao.pojo.TbItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbItemMapper {
    int insert(TbItem record);

    int insertSelective(TbItem record);

    TbItem findByItemId(@Param("itemId") long itemId);

    List<TbItem> getTbItemList();

}