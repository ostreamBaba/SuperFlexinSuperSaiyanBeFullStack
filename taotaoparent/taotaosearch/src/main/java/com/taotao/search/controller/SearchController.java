package com.taotao.search.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.pojo.SearchResult;
import com.taotao.search.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ Create by ostreamBaba on 18-11-29
 * @ 商品查询
 */

@Controller
public class SearchController {

    @Autowired(required = false)
    private SearchService searchService;

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    public TaotaoResult search(@RequestParam("q")String queryString, @RequestParam(defaultValue = "1")Integer page,
                               @RequestParam(defaultValue = "60")Integer rows){
        //查询条件不能为空
        if(StringUtils.isBlank(queryString)){
             return TaotaoResult.build(400, "查询条件不能为空");
        }
        SearchResult searchResult;
        try {
            //queryString = new String(queryString.getBytes("iso8859-1"),"utf-8"); //解决get乱码
            searchResult = searchService.search(queryString, page, rows);
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtils.getStackTrace(e));
        }
        return TaotaoResult.ok(searchResult);
    }
}
