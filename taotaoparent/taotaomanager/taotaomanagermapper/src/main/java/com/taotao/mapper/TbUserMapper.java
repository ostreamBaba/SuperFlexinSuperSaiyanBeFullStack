package com.taotao.mapper;

import com.taotao.pojo.TbUser;

import java.util.List;

public interface TbUserMapper {
    int insert(TbUser record);

    int insertSelective(TbUser record);

    List<TbUser> selectByUsername(String username);

    List<TbUser> selectByPhone(String phone);

    List<TbUser> selectByEmail(String email);

    TbUser findByUserNameAndPassword(String userName, String password);

}