package com.taotao.controller;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemCatService;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ Create by ostreamBaba on 18-11-27
 * @ 描述
 */

@RequestMapping("/item")
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/{itemId}")
    public @ResponseBody
    TbItem getItemById(@PathVariable long itemId){
        //System.out.println("尽量");
        return itemService.getItemById(itemId);
    }


    @RequestMapping("/list")
    @ResponseBody
    public EUDataGridResult<TbItem> getItemList(Integer page, Integer rows){
        EUDataGridResult<TbItem> result = itemService.getItemList(page, rows);
        return result;
    }

    @RequestMapping(value="/save", method=RequestMethod.POST)
    @ResponseBody
    public TaotaoResult createItem(TbItem tbItem, String desc) throws Exception {
        return itemService.createItem(tbItem, desc);
    }

}
