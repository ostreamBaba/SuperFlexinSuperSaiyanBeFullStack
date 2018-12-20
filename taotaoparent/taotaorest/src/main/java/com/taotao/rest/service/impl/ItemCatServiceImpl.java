package com.taotao.rest.service.impl;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Create by ostreamBaba on 18-12-4
 * @ 描述
 */

@Service
public class ItemCatServiceImpl implements ItemCatService{

    @Autowired(required = false)
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public CatResult getItemCatList() {
        CatResult catResult = new CatResult();
        //查询分类列表
        catResult.setData(getCatList(0));
        return catResult;
    }

    private List<Object> getCatList(long parentId){
        List<TbItemCat> list = tbItemCatMapper.getCatByParentId(parentId);
        List<Object> result = new ArrayList<>();
        int count = 0;
        for(TbItemCat tbItemCat : list){
            if(++count > 14){
                break;
            }
            if(tbItemCat.getIsParent()){
                CatNode catNode = new CatNode();
                if(parentId == 0){
                    catNode.setName("<a href='/products/"+tbItemCat.getId()+".html'>"+tbItemCat.getName()+"</a>");
                } else {
                    catNode.setName(tbItemCat.getName());
                }
                catNode.setUrl("/products/"+tbItemCat.getId()+".html");
                catNode.setItem(getCatList(tbItemCat.getId()));
                result.add(catNode);
            }else {
                result.add("/products/"+tbItemCat.getId()+".html|"+tbItemCat.getName());
            }
        }
        return result;
    }

}

