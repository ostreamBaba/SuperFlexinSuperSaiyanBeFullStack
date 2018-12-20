package com.taotao.rest.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.service.RedisService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ Create by ostreamBaba on 18-12-8
 * @ 描述
 */

@Service
public class RedisServiceImpl implements RedisService{

    @Resource(name = "clientCluster")
    private JedisClient jedisClient;

    @Value("${INDEX_CONTENT_REDIS_KEY}")
    private String INDEX_CONTENT_REDIS_KEY;

    @Override
    public TaotaoResult syncContent(long contentCid) {
        try{
            jedisClient.hdel(INDEX_CONTENT_REDIS_KEY, contentCid+"");
            return TaotaoResult.ok();
        } catch (Exception e){
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtils.getStackTrace(e));
        }
    }
}
