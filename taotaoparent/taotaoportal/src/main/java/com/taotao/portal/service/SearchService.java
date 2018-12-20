package com.taotao.portal.service;

import com.taotao.portal.pojo.SearchResult;

/**
 * @ Create by ostreamBaba on 18-12-5
 * @ 描述
 */
public interface SearchService {

    SearchResult search(String queryString, Integer page);

}
