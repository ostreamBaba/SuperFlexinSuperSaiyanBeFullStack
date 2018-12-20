package com.taotao.portal.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.portal.pojo.SearchResult;
import com.taotao.portal.service.SearchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @ Create by ostreamBaba on 18-12-5
 * @ 描述
 */

@Service
public class SearchServiceImpl implements SearchService{

    @Value("${SEARCH_BASE_URL}")
    private String SEARCH_BASE_URL;

    @Override
    public SearchResult search(String queryString, Integer page) {
        Map<String, String> param = new HashMap<>();
        //查询对象
        String pageStr = "";
        if(page != null){
            pageStr += String.valueOf(page);
        }
        param.put("q", queryString);
        param.put("page", pageStr);
        //调用服务
        try{
            String json = HttpClientUtil.doGet(SEARCH_BASE_URL, param);
            TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, SearchResult.class);
            if(taotaoResult != null && taotaoResult.getStatus() == 200){
                return (SearchResult) taotaoResult.getData();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
