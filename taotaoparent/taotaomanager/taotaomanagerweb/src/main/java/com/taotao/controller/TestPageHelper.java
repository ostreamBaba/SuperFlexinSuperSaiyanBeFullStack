package com.taotao.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * @ Create by ostreamBaba on 18-11-28
 * @ PageHelper分页插件
 */

public class TestPageHelper {

    @Test
    public void testPageHelper(){
        //创建Spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        //从容器中获得代理对象
        TbItemMapper tbItemMapper = applicationContext.getBean( TbItemMapper.class);
        //执行查询 分页
        PageHelper.startPage(2, 10);
        List<TbItem> list = tbItemMapper.getTbItemList();
        //取商品信息
        for (TbItem tbItem: list){
            System.out.println(tbItem.getTitle());
        }
        //取分页信息
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        System.out.println("共有商品信息: "+total);
    }

}
