package com.taotao.rest.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.rest.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ Create by ostreamBaba on 18-12-8
 * @ 描述
 */

@Controller
@RequestMapping("/cache/sync")
public class RedisController {

    @Autowired(required = false)
    private RedisService redisService;

    @RequestMapping("/content/{contentCid}")
    @ResponseBody
    public TaotaoResult content(@PathVariable Long contentCid){
        return redisService.syncContent(contentCid);
    }


}
