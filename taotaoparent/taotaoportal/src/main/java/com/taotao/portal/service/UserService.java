package com.taotao.portal.service;


import com.taotao.pojo.TbUser;

/**
 * @ Create by ostreamBaba on 18-12-4
 * @ 描述
 */
public interface UserService {

    TbUser getUserByToken(String token);

}
