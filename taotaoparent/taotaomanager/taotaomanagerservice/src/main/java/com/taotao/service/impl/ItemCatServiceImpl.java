package com.taotao.service.impl;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Create by ostreamBaba on 18-11-28
 * @ 描述
 */

@Service
public class ItemCatServiceImpl implements ItemCatService{

    @Autowired(required = false)
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public List<EUTreeNode> getCatList(long parentId) {
        List<TbItemCat> list = tbItemCatMapper.getCatByParentId(parentId);
        List<EUTreeNode> resultList = new ArrayList<>();
        for (TbItemCat tbItemCat : list){
            EUTreeNode node = new EUTreeNode();
            node.setId(tbItemCat.getId());
            node.setText(tbItemCat.getName());
            node.setState(tbItemCat.getIsParent()?"closed":"open"); //是父节点closed 不是父节点 open
            resultList.add(node);
        }
        return resultList;
    }
}
