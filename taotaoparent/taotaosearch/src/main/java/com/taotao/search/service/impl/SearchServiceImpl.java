package com.taotao.search.service.impl;

import com.taotao.search.dao.SearchDao;
import com.taotao.search.pojo.SearchResult;
import com.taotao.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ Create by ostreamBaba on 18-11-29
 * @ 搜索service
 */


@Service
public class SearchServiceImpl implements SearchService{

    @Autowired(required = false)
    private SearchDao searchDao;

    //默认查询域 页 总数量
    @Override
    public SearchResult search(String queryString, int page, int rows) throws SolrServerException {
        //创建查询条件
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件
        solrQuery.setQuery(queryString);
        //设置分页
        solrQuery.setStart((page-1)*rows);
        solrQuery.setRows(rows);
        //设置默认搜索域
        solrQuery.set("df","item_keywords");
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("item_title");
        solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
        solrQuery.setHighlightSimplePost("</em>");
        //执行查询
        SearchResult result = searchDao.search(solrQuery);
        //计算总页数
        long recordCount = result.getRecordCount();
        long pageCount = recordCount / rows;
        if(recordCount % rows > 0){
            ++pageCount;
        }
        result.setPageCount(pageCount);
        result.setCurPage(page);
        return result;
    }
}
