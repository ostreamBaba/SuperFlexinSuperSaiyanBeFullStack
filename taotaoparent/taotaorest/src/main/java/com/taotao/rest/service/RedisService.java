package com.taotao.rest.service;

import com.taotao.common.pojo.TaotaoResult;

/**
 * @ Create by ostreamBaba on 18-12-8
 * @ 缓存同步
 */
public interface RedisService {

    TaotaoResult syncContent(long contentCid);

}
