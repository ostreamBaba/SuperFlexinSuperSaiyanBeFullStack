package com.taotao.sso.service.impl;

import com.alibaba.fastjson.JSON;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.sso.dao.JedisClient;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ Create by ostreamBaba on 18-11-30
 * @ 用户管理
 */

@Service
public class UserServiceImpl implements UserService{

    @Autowired(required = false)
    private TbUserMapper tbUserMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_USER_SESSION_KEY}")
    private String REDIS_USER_SESSION_KEY;

    @Value("${SSO_SESSION_EXPIRE}")
    private Integer SSO_SESSION_EXPIRE;

    @Override
    public TaotaoResult checkData(String content, Integer type) {
        //对数据进行校验 1 2 3 代表 username phone email
        List<TbUser> tbUser;
        if(1 == type){
            tbUser = tbUserMapper.selectByUsername(content);
        } else if(2 == type){
            tbUser = tbUserMapper.selectByPhone(content);
        } else {
            tbUser = tbUserMapper.selectByEmail(content);
        }
        if(tbUser != null && tbUser.size() > 0){
            return TaotaoResult.ok(false);
        }
        return TaotaoResult.ok(true);
    }

    @Override
    public TaotaoResult createUser(TbUser tbUser) {
        tbUser.setCreated(new Date());
        tbUser.setUpdated(new Date());
        tbUser.setPassword(DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes()));
        tbUserMapper.insert(tbUser);
        return TaotaoResult.ok();
    }

    //用户登录的逻辑
    @Override
    public TaotaoResult userLogin(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        List<TbUser> list = tbUserMapper.selectByUsername(username);
        if(list == null || list.size() == 0){
            return TaotaoResult.build(400, "用户名或者密码错误");
        }
        TbUser user = list.get(0);
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if(!md5Password.equals(user.getPassword())){
            return TaotaoResult.build(400, "用户名或者密码错误");
        }
        user.setPassword(null);
        //生产一个token
        String token = UUID.randomUUID().toString();
        //将用户信息保存在redis里面 去除密码
        String userJson = JsonUtils.objectToJson(user);
        jedisClient.setex(REDIS_USER_SESSION_KEY+":"+token, SSO_SESSION_EXPIRE, userJson);

        String cookieName = "TT_TOKEN";
        String cookieValue = token;
        //添加写Cookie的逻辑 cookie有效期是关闭浏览器就失效
        CookieUtils.setCookie(request, response, cookieName, cookieValue);
        //返回token
        return TaotaoResult.ok(token);
    }

    @Override
    public TaotaoResult getUserByToken(String token) {
        //根据token从redis查询用户信息
        String key = REDIS_USER_SESSION_KEY+":"+token;
        String json = jedisClient.get(key);
        if(StringUtils.isBlank(json)){
            return TaotaoResult.build(400, "该session已经过期 请从新登录");
        }
        //更新过期时间
        jedisClient.expire(REDIS_USER_SESSION_KEY+":"+token, SSO_SESSION_EXPIRE);
        return TaotaoResult.ok(JSON.parseObject(json));
    }


    public static void main(String[] args) {
        System.out.println(DigestUtils.md5DigestAsHex("123".getBytes()));
    }

}
