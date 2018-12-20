package com.taotao.common.pojo;

import java.util.List;

/**
 * @ Create by ostreamBaba on 18-11-28
 * @ 返回符合EasyUI的json数据类型
 */

public class EUDataGridResult<T> {

    private long total;

    private List<T> rows;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public List<T> getRows() {
        return rows;
    }
}
