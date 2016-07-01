package com.somnus.pay.payment.pojo;

import java.util.ArrayList;
import java.util.List;

public class QueryResult<T> {

    private List<T> list;
    private Page    page;

    public QueryResult() {
        page = new Page();
        list = new ArrayList<T>();
    }

    public QueryResult(List<T> list, Page page) {
        super();
        this.list = list;
        this.page = page;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
