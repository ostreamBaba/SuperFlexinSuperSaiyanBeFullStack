package com.taotao.order.dao;

/**
 * @ Create by ostreamBaba on 18-11-30
 * @ 描述
 */

public interface JedisClient {

    String get(String key);

    String set(String key, String value);

    String hget(String hkey, String key);

    long hset(String hkey, String key, String value);

    long incr(String key);

    long expire(String key, int second);

    long ttl(String key);

    void setex(String s, Integer sso_session_expire, String userJson);

    long del(String key);

    long hdel(String hkey, String key);

}
