package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ Create by ostreamBaba on 18-11-30
 * @ 描述
 */
public interface UserService {

    TaotaoResult checkData(String content, Integer type);

    TaotaoResult createUser(TbUser tbUser);

    TaotaoResult userLogin(String username, String password, HttpServletRequest request, HttpServletResponse response);

    TaotaoResult getUserByToken(String token);

}
