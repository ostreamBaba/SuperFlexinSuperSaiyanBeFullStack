package com.taotao.search;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

/**
 * @ Create by ostreamBaba on 18-12-7
 * @ 描述
 */
public class SolrCloudTest {

    @Test
    public void testAddDocument() throws IOException, SolrServerException {
        //创建一个solr集群的链接
        //参数就是Zookeeper的地址列表 使用逗号分割
        String zkHost = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
        CloudSolrServer solrServer = new CloudSolrServer(zkHost);
        //设置默认的Collection
        solrServer.setDefaultCollection("collection2");
        //创建一个文档对象
        SolrInputDocument document = new SolrInputDocument();
        //向文档添加域
        document.addField("id", "test001");
        document.addField("item_title", "测试商品");
        //把文档添加到索引库
        solrServer.add(document);
        //提交
        solrServer.commit();
    }


    @Test
    public void deleteDocument() throws IOException, SolrServerException {
        //创建一个solr集群的链接
        //参数就是Zookeeper的地址列表 使用逗号分割
        String zkHost = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
        CloudSolrServer solrServer = new CloudSolrServer(zkHost);
        //设置默认的Collection
        solrServer.setDefaultCollection("collection2");
        solrServer.deleteByQuery("*:*");
        solrServer.commit();
    }

}

