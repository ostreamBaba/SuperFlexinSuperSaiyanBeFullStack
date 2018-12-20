package com.taotao.rest.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @ Create by ostreamBaba on 18-12-5
 * @ 描述
 */

@Service
public class ItemServiceImpl implements ItemService{

    @Autowired(required = false)
    private TbItemMapper tbItemMapper;

    @Autowired(required = false)
    private TbItemDescMapper tbItemDescMapper;

    @Autowired(required = false)
    private TbItemParamItemMapper tbItemParamItemMapper;

    @Autowired(required = false)
    private JedisClient jedisClient;

    @Value("${REDIS_ITEM_KEY}")
    private String REDIS_ITEM_KEY;

    @Value("${REDIS_ITEM_EXPIRE}")
    private Integer REDIS_ITEM_EXPIRE;

    @Override
    public TaotaoResult getItemBaseInfo(long itemId) {
        //添加缓存逻辑
        //从缓存中取商品信息
        try{
            String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":base");
            if(!StringUtils.isBlank(json)){
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                return TaotaoResult.ok(tbItem);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        TbItem tbItem = tbItemMapper.findByItemId(itemId);
        //把商品信息写入缓存
        //设置过期时间
        jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":base", JsonUtils.objectToJson(tbItem));
        jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":base", REDIS_ITEM_EXPIRE);
        return TaotaoResult.ok(tbItem);

    }

    @Override
    public TaotaoResult getItemDesc(long itemId) {
        try{
            String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":desc");
            if(!StringUtils.isBlank(json)){
                TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return TaotaoResult.ok(tbItemDesc);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        TbItemDesc tbItemDesc = tbItemDescMapper.selectItemDescById(itemId);
        jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":desc", JsonUtils.objectToJson(tbItemDesc));
        jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":desc", REDIS_ITEM_EXPIRE);
        return TaotaoResult.ok(tbItemDesc);
    }

    @Override
    public TaotaoResult getItemParam(long itemId) {
        try{
            String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":param");
            if(!StringUtils.isBlank(json)){
                TbItemParamItem tbItemParamItem = JsonUtils.jsonToPojo(json, TbItemParamItem.class);
                return TaotaoResult.ok(tbItemParamItem);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        TbItemParamItem tbItemParamItem = tbItemParamItemMapper.selectByItemId(itemId);
        if(tbItemParamItem != null){
            jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":param", JsonUtils.objectToJson(tbItemParamItem));
            jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":param", REDIS_ITEM_EXPIRE);
            return TaotaoResult.ok(tbItemParamItem);
        }
        return TaotaoResult.build(400, "没有此商品的规格参数");
    }
}
