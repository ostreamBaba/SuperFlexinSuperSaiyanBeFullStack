package com.taotao.order.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;

import java.util.List;

/**
 * @ Create by ostreamBaba on 18-12-10
 * @ 描述
 */

public interface OrderService {

    TaotaoResult createOrder(TbOrder tbOrder, List<TbOrderItem> itemList, TbOrderShipping tbOrderShipping);

}
