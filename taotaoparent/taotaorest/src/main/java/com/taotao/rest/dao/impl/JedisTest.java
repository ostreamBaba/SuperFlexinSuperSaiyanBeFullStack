package com.taotao.rest.dao.impl;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.HashSet;

/**
 * @ Create by ostreamBaba on 18-12-8
 * @ 描述
 */
public class JedisTest {


    @Test
    public void testJedisCluster() throws IOException {
        HashSet<HostAndPort> nodes = new HashSet<>();
        int port = 7001;
        String host = "127.0.0.1";
        //6个节点
        for (int i = 0; i < 6; i++) {
            nodes.add(new HostAndPort(host, port++));
        }
        //java.lang.NumberFormatException 版本问题
        JedisCluster cluster = new JedisCluster(nodes);
        cluster.set("key3", "100");
        String s = cluster.get("key2");
        System.out.println(s);
        cluster.close();
    }

    //测试集群版本
    @Test
    public void testSpringJedisCluster() throws IOException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        JedisCluster jedisCluster = (JedisCluster) applicationContext.getBean("redisClient");
        String s = jedisCluster.get("key1");
        System.out.println(s);
        jedisCluster.close();
    }

}
