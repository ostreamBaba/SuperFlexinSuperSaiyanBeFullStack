package com.taotao.portal.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.pojo.TbUser;
import com.taotao.portal.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @ Create by ostreamBaba on 18-12-4
 * @ 用户管理的Service
 */

@Service
public class UserServiceImpl implements UserService{

    @Value("${SSO_BASE_URL}")
    private String SSO_BASE_URL;

    @Value("${SSO_USER_TOKEN}")
    private String SSO_USER_TOKEN;

    @Value("${SSO_PAGE_LOGIN}")
    private String SSO_PAGE_LOGIN;

    @Override
    public TbUser getUserByToken(String token) {
        try{
            //调用sso的服务 根据token取用户信息
            String json = HttpClientUtil.doGet(SSO_BASE_URL + SSO_USER_TOKEN + token);
            //把json转换
            TaotaoResult result = TaotaoResult.formatToPojo(json, TbUser.class);
            if(result != null && result.getStatus() == 200){
                return (TbUser) result.getData();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String getSSO_BASE_URL() {
        return SSO_BASE_URL;
    }

    public String getSSO_USER_TOKEN() {
        return SSO_USER_TOKEN;
    }

    public String getSSO_PAGE_LOGIN() {
        return SSO_PAGE_LOGIN;
    }
}
