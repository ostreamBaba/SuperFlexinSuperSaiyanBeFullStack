package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ Create by ostreamBaba on 18-11-28
 * @ 描述
 */

@Controller
public class PageController {


    @RequestMapping("/")
    public String showIndex(){
        return "index";
    }


    /**
     * @描述 展示其他页面
     * @param page
     * @return java.lang.String
     * @create by ostreamBaba on 下午1:24 18-11-28
     */
    @RequestMapping("/{page}")
    public String showPage(@PathVariable String page){
        return page;
    }


}
