package com.taotao.sso.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ Create by ostreamBaba on 18-11-30
 * @ 描述
 */

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/check/{param}/{type}")
    @ResponseBody
    public Object checkData(@PathVariable String param, @PathVariable Integer type, String callback){
        TaotaoResult result = null;
        //param 不能为空
        /*if(StringUtils.isBlank(param)){
            result = TaotaoResult.build(400,"校验内容不能为空");
        }*/
        /*if(type == null){
            result = TaotaoResult.build(400, "校验类型不能为空");
        }*/
        if(type < 1 || type > 3){
            result = TaotaoResult.build(400, "校验类型错了");
        }

        if(result == null){
            try{
                result = userService.checkData(param, type);
            } catch (Exception e){
                result = TaotaoResult.build(500, ExceptionUtils.getStackTrace(e));
            }
        }

        //jsonp ???
        if(callback != null){ //校验是否有回调
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }else {
            return result;
        }
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult createUser(TbUser tbUser){
        TaotaoResult result;
        try{
            result = userService.createUser(tbUser);
        }catch (Exception e){
            result = TaotaoResult.build(500, ExceptionUtils.getStackTrace(e));
        }
        return result;
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult userLogin(String username, String password,
                                  HttpServletRequest request, HttpServletResponse response){
        TaotaoResult result;
        try{
            result = userService.userLogin(username, password, request, response);
        }catch (Exception e){
            result = TaotaoResult.build(500, ExceptionUtils.getStackTrace(e));
        }
        return result;
    }

    @RequestMapping(value = "/token/{token}")
    @ResponseBody
    public Object getUserByToken(@PathVariable String token, String callback){
        TaotaoResult result;
        try{
            result = userService.getUserByToken(token);
        } catch (Exception e){
            result = TaotaoResult.build(500, ExceptionUtils.getStackTrace(e));
        }
        if(StringUtils.isBlank(callback)){
            return result;
        }else{
            //jsonp
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
    }

}
