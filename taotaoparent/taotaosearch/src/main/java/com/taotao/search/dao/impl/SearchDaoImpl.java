package com.taotao.search.dao.impl;

import com.taotao.search.dao.SearchDao;
import com.taotao.search.pojo.Item;
import com.taotao.search.pojo.SearchResult;
import com.taotao.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ Create by ostreamBaba on 18-11-29
 * @ 商品搜索
 * @ 根据查询条件查询索引库
 */
@Repository
public class SearchDaoImpl implements SearchDao {

    //单机版本
    @Resource(name = "httpSolrServer")
    private SolrServer solrServer;

    @Override
    public SearchResult search(SolrQuery query) throws SolrServerException {
        //返回值对象
        SearchResult result = new SearchResult();
        //根据查询对象查询索引库
        QueryResponse queryResponse = solrServer.query(query);
        //取得查询结果
        SolrDocumentList solrDocuments = queryResponse.getResults();
        //取得查询结果总数量
        result.setRecordCount(solrDocuments.getNumFound());
        //取高亮显示
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        //商品列表
        List<Item> itemList = new ArrayList<>();
        for (SolrDocument solrDocument : solrDocuments){
            String id = (String) solrDocument.get("id");
            List<String> list = highlighting.get(id).get("item_title");
            String title;
            if(list != null && list.size() > 0){
                title = list.get(0);
            }else{
                title = (String) solrDocument.get("item_title");
            }
            Item item = new Item();
            item.setId( (String) solrDocument.get("id") );
            item.setTitle( title ); //title设置高亮
            item.setImage( (String) solrDocument.get("item_image") );
            item.setCategory_name( (String) solrDocument.get("item_category_name") );
            item.setPrice( (Long) solrDocument.get("item_price") );
            item.setSell_point( (String) solrDocument.get("item_sell_point") );
            itemList.add(item);
        }
        result.setItemList(itemList);
        return result;
    }

    public static void main(String[] args) throws SolrServerException {
        SearchDao searchDao = new SearchDaoImpl();
        SolrQuery solrQuery = new SolrQuery("手机");
        SearchResult searchResult = searchDao.search(solrQuery);
        System.out.println(searchResult.getCurPage());
    }
}
