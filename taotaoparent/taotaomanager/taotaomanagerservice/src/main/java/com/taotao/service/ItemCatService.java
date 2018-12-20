package com.taotao.service;

import com.taotao.common.pojo.EUTreeNode;

import java.util.List;

/**
 * @ Create by ostreamBaba on 18-11-28
 * @ 描述
 */
public interface ItemCatService {

    List<EUTreeNode> getCatList(long parentId);

}
