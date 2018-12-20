package com.taotao.order.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.dao.JedisClient;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @ Create by ostreamBaba on 18-12-10
 * @ 描述
 */

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired(required = false)
    private TbOrderMapper tbOrderMapper;

    @Autowired(required = false)
    private TbOrderItemMapper tbOrderItemMapper;

    @Autowired(required = false)
    private TbOrderShippingMapper tbOrderShippingMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${ORDER_GEN_KEY}")
    private String ORDER_GEN_KEY;

    @Value("${ORDER_INIT_ID}")
    private String ORDER_INIT_ID;

    @Value("${ORDER_DETAIL_GEN_KEY}")
    private String ORDER_DETAIL_GEN_KEY;

    //生产订单的过程
    @Override
    public TaotaoResult createOrder(TbOrder tbOrder, List<TbOrderItem> itemList, TbOrderShipping tbOrderShipping) {
        //向订单表中插入记录
        //获取订单号
        String s = jedisClient.get(ORDER_GEN_KEY);
        if(StringUtils.isBlank(s)){
            jedisClient.set(ORDER_GEN_KEY, ORDER_INIT_ID);
        }
        long orderId = jedisClient.incr(ORDER_GEN_KEY);
        //补全pojo属性
        tbOrder.setOrderId(orderId+"");
        tbOrder.setStatus(1);
        Date date = new Date();
        tbOrder.setCreateTime(date);
        tbOrder.setUpdateTime(date);
        //0 未评价 1 评价
        tbOrder.setBuyerRate(0);
        //向订单中插入数据
        tbOrderMapper.insert(tbOrder);
        //插入订单明细 1对多
        for (TbOrderItem tbOrderItem : itemList){
            //补全订单明细
            //取订单明细id
            String s1 = jedisClient.get(ORDER_DETAIL_GEN_KEY);
            if(StringUtils.isBlank(s1)){
                jedisClient.set(ORDER_GEN_KEY, "1");
            }
            long orderDetailId = jedisClient.incr(ORDER_DETAIL_GEN_KEY);
            tbOrderItem.setId(orderDetailId+"");
            tbOrderItem.setOrderId(orderId+"");
            //向订单明细表插入记录
            tbOrderItemMapper.insert(tbOrderItem);
        }
        //插入物流表
        tbOrderShipping.setOrderId(orderId+"");
        tbOrderShipping.setCreated(date);
        tbOrderShipping.setUpdated(date);
        tbOrderShippingMapper.insert(tbOrderShipping);
        return TaotaoResult.ok(orderId);
    }

}