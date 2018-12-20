package com.taotao.portal.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.service.CartService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @ Create by ostreamBaba on 18-12-9
 * @ 购物车service
 */
@Service
public class CartServiceImpl implements CartService{

    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;

    @Value("${ITEM_INFO_URL}")
    private String ITEM_INFO_URL;

    //利用cookie来达成不登录可添加到购物车
    //缺点 换电脑就需要重新加购物车
    @Override
    public TaotaoResult addCartItem(long itemId, int num, HttpServletRequest request, HttpServletResponse response) {
        CartItem cartItem = null;
        //取商品购物车列表
        List<CartItem> cartItemList = getCartItemList(request);
        if (cartItemList == null){
            cartItemList = new ArrayList<>();
        }
        for (CartItem item : cartItemList){
            if(item.getId() == itemId){
                cartItem = item;
                cartItem.setNum(cartItem.getNum()+num);
                break;
            }
        }
        if(cartItem == null){
            cartItem = new CartItem();
            //根据商品查询基本信息
            String json = HttpClientUtil.doGet(REST_BASE_URL+ITEM_INFO_URL+itemId);
            TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, TbItem.class);
            if(taotaoResult != null && taotaoResult.getStatus() == 200){
                TbItem tbItem = (TbItem) taotaoResult.getData();
                cartItem.setId(tbItem.getId());
                cartItem.setPrice(tbItem.getPrice());
                cartItem.setTitle(tbItem.getTitle());
                cartItem.setImage(tbItem.getImage()==null?"":tbItem.getImage().split(",")[0]);
                cartItem.setNum(num);
            }
            //添加到购物车列表
            cartItemList.add(cartItem);
        }
        //把购物车重新写入cookie
        CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(cartItemList), true);
        return TaotaoResult.ok();
    }

    @Override
    public List<CartItem> getCartItemList(HttpServletRequest request, HttpServletResponse response) {
         return getCartItemList(request);
    }

    //删除购物车
    //1.取购物车
    //2.删除其中的商品
    //3.更新cookie
    @Override
    public TaotaoResult deleteCartItem(long itemId, HttpServletRequest request, HttpServletResponse response) {
        List<CartItem> cartItemList = getCartItemList(request);
        if(cartItemList != null){
            boolean flag = cartItemList.removeIf(cartItem->cartItem.getId()==itemId);
            //重新写入cookie
            if(flag){
                CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(cartItemList), true);
            }
        }
        return TaotaoResult.ok();
    }

    //从cookie取商品列表
    private List<CartItem> getCartItemList(HttpServletRequest request){
        String cartJson = CookieUtils.getCookieValue(request, "TT_CART",true);
        if(cartJson == null){
            return new ArrayList<>();
        }
        //把json转化成商品列表
        try {
            return JsonUtils.jsonToList(cartJson, CartItem.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
