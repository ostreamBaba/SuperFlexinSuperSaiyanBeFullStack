package com.taotao.search.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.mapper.ItemMapper;
import com.taotao.search.pojo.Item;
import com.taotao.search.service.ItemService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ Create by ostreamBaba on 18-11-28
 * @ 描述
 */

@Service
public class ItemServiceImpl implements ItemService{

    @Autowired(required = false)
    private ItemMapper itemMapper;

    //集群版本
    @Resource(name = "cloudSolrServer")
    private SolrServer solrServer;

    @Override
    public TaotaoResult importAllItems() {
        List<Item> result = itemMapper.getItemList();
        //将商品信息写入索引库
        try {
            for (Item item : result){
                SolrInputDocument document = new SolrInputDocument();
                document.setField("id", item.getId());
                document.setField("item_title",item.getTitle());
                document.setField("item_price",item.getPrice());
                document.setField("item_image",item.getImage());
                document.setField("item_sell_point",item.getSell_point());
                document.setField("item_desc",item.getItem_des());
                document.setField("item_category_name",item.getCategory_name());
                solrServer.add(document);
            }
            solrServer.commit();
        }catch (Exception e){
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtils.getStackTrace(e));
        }
        return TaotaoResult.ok();
    }

}
