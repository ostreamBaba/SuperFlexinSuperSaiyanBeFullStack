package com.taotao.portal.pojo;

import java.util.List;

/**
 * @ Create by ostreamBaba on 18-11-29
 * @ 描述
 */
public class SearchResult {

    /*商品列表*/
    private List<Item> itemList;

    //总记录数
    private long recordCount;

    //总页数
    private long pageCount;

    //当前页
    private long curPage;

    public List<Item> getItemList() {
        return itemList;
    }

    public long getRecordCount() {
        return recordCount;
    }

    public long getPageCount() {
        return pageCount;
    }

    public long getCurPage() {
        return curPage;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public void setCurPage(long curPage) {
        this.curPage = curPage;
    }
}
