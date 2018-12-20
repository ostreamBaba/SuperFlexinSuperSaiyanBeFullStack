package com.taotao.order.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.order.pojo.Order;
import com.taotao.order.service.OrderService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ Create by ostreamBaba on 18-12-10
 * @ 描述
 */

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    //@RequestBody可以接受对应的json数据 并将其转换成对应的数据类型
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult createOrder(@RequestBody Order order){
        try{
            //对应的订单号
            return orderService.createOrder(order, order.getOrderItems(), order.getOrderShipping());
        }catch (Exception e){
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtils.getStackTrace(e));
        }
    }

}
