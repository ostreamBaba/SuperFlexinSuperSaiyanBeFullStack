package com.taotao.portal.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ Create by ostreamBaba on 18-12-10
 * @
 */

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping("/add/{itemId}")
    public String addCartItem(@PathVariable Long itemId, @RequestParam(defaultValue = "1")
                              Integer num, HttpServletRequest request, HttpServletResponse response){
        TaotaoResult result = cartService.addCartItem(itemId, num, request, response);
        return "redirect:/cart/success.html"; //防止刷新访问该url改变购物车的数量
    }

    @RequestMapping("/success")
    public String showSuccess(){
        return "cartSuccess";
    }


    @RequestMapping("/cart")
    public String showCart(HttpServletRequest request, HttpServletResponse response, Model model){
        List<CartItem> cartItemList = cartService.getCartItemList(request, response);
        model.addAttribute("cartList", cartItemList);
        return "cart";
    }


    @RequestMapping("/delete/{itemId}")
    public String deleteCart(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response){
        cartService.deleteCartItem(itemId, request, response); //从cookie删除
        return "redirect:/cart/cart.html"; //重新获得购物车列表
        //伪静态化
    }

}
