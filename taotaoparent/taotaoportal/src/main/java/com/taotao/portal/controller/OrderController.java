package com.taotao.portal.controller;

import com.taotao.common.utils.CookieUtils;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.pojo.Order;
import com.taotao.portal.service.CartService;
import com.taotao.portal.service.OrderService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ Create by ostreamBaba on 18-12-10
 * @ 描述
 */

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @RequestMapping("/order-cart")
    public String showOrderCart(HttpServletRequest request, HttpServletResponse response, Model model){
        List<CartItem> list = cartService.getCartItemList(request, response);
        System.out.println(list.size());
        model.addAttribute("cartList", list);
        return "order-cart";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createOrder(Order order, Model model, HttpServletRequest request, HttpServletResponse response){
        try{
            //CookieUtils.deleteCookie(request, response,"TT_CART"); 清空购物车
            String orderId = orderService.createOrder(order);
            model.addAttribute("orderId", orderId);
            model.addAttribute("payment", order.getPayment());
            model.addAttribute("date", new DateTime().plusDays(3).toString("yyyy-MM-dd"));
            return "redirect:/order/success.html";
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("message", "创建订单出错");
            return "error/exception";
        }
    }

    @RequestMapping("/success")
    public String showSuccess(String orderId, String payment, String date, Model model){
        model.addAttribute("orderId", orderId);
        model.addAttribute("payment", payment);
        model.addAttribute("date", date);
        return "success";

    }

}
