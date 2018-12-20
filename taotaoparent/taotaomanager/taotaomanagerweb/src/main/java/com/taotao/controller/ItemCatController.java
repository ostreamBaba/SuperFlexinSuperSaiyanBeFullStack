package com.taotao.controller;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @ Create by ostreamBaba on 18-11-28
 * @ 商品分类管理
 */

@Controller
@RequestMapping("/item/cat")
public class ItemCatController {

    @Autowired(required = false)
    private ItemCatService itemCatService;


    @RequestMapping("/list")
    @ResponseBody
    public List<EUTreeNode> getCatList(@RequestParam(value = "id", defaultValue = "0") long parentId){
        List<EUTreeNode> resultList = itemCatService.getCatList(parentId);
        return resultList;
    }

}
