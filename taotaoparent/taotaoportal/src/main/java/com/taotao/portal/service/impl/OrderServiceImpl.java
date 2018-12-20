package com.taotao.portal.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.portal.pojo.Order;
import com.taotao.portal.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @ Create by ostreamBaba on 18-12-10
 * @ 描述
 */

@Service
public class OrderServiceImpl implements OrderService{

    @Value("${ORDER_BASE_URL}")
    private String ORDER_BASE_URL;

    @Value("${ORDER_CRATE_URL}")
    private String ORDER_CRATE_URL;


    @Override
    public String createOrder(Order order) {
        try{
            String json = HttpClientUtil.doPostJson(ORDER_BASE_URL+ORDER_CRATE_URL, JsonUtils.objectToJson(order));
            TaotaoResult result = TaotaoResult.format(json);
            if(result != null && result.getStatus() == 200){
                Object orderId = result.getData();
                return orderId.toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
