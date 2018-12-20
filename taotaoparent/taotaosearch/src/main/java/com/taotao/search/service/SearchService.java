package com.taotao.search.service;

import com.taotao.search.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrServerException;

/**
 * @ Create by ostreamBaba on 18-11-29
 * @ 描述
 */
public interface SearchService {

    SearchResult search(String queryString, int page, int rows) throws SolrServerException;

}
