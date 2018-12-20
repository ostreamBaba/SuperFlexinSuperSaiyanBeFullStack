package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * @ Create by ostreamBaba on 18-11-27
 * @ 描述
 */

@Service
public class ItemServiceImpl implements ItemService{

    @Autowired(required = false)
    private TbItemMapper tbItemMapper;

    @Autowired(required = false)
    private TbItemDescMapper tbItemDescMapper;

    @Override
    public TbItem getItemById(long itemId) {
        return tbItemMapper.findByItemId(itemId);
    }


    /**
     * @描述 查询商品总条数
     * @param page
     * @param rows
     * @return com.taotao.common.pojo.EUDataGridResult<com.taotao.pojo.TbItem>
     * @create by ostreamBaba on 下午2:38 18-11-28
     */

    @Override
    public EUDataGridResult<TbItem> getItemList(int page, int rows) {
        PageHelper.startPage(page, rows);
        List<TbItem> list = tbItemMapper.getTbItemList();
        //创建一个返回值对象
        EUDataGridResult<TbItem> result = new EUDataGridResult<>();
        result.setRows(list);
        //取记录总条数
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    @Override
    public TaotaoResult createItem(TbItem tbItem, String desc) throws Exception {
        Long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);
        //1 上架 2 下架 3 删除
        tbItem.setStatus((byte)1);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        tbItemMapper.insert(tbItem);
        //添加商品描述信息
        TaotaoResult result = insertItemDesc(itemId, desc);
        if(result.getStatus() != 200){
            throw new Exception();
        }
        return TaotaoResult.ok();
    }

    /**
     * @描述 分表 添加商品描述
     * @param itemId
     * @param desc
     * @return com.taotao.common.pojo.TaotaoResult
     * @create by ostreamBaba on 下午8:11 18-11-28
     */

    private TaotaoResult insertItemDesc(Long itemId, String desc){
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        tbItemDescMapper.insert(itemDesc);
        return TaotaoResult.ok();
    }

}
