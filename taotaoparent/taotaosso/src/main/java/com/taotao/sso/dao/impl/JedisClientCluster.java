package com.taotao.sso.dao.impl;

import com.taotao.sso.dao.JedisClient;

/**
 * @ Create by ostreamBaba on 18-11-30
 * @ 描述
 */
public class JedisClientCluster implements JedisClient{

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public String set(String key, String value) {
        return null;
    }

    @Override
    public String hget(String hkey, String key) {
        return null;
    }

    @Override
    public long hset(String hkey, String key, String value) {
        return 0;
    }

    @Override
    public long incr(String key) {
        return 0;
    }

    @Override
    public long expire(String key, int second) {
        return 0;
    }

    @Override
    public long ttl(String key) {
        return 0;
    }

    @Override
    public void setex(String s, Integer sso_session_expire, String userJson) {

    }
}
