package com.ghx.hackaton.analytics.service.pagination;

public class Pagination {
    private int page = 1;
    private int itemsPerPage = 20;

    public Pagination() {
    }

    public Pagination(int page, int itemsPerPage) {
        this.page = page;
        this.itemsPerPage = itemsPerPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public int getFirstResult() {
        return (getPage() - 1) * getItemsPerPage();
    }
}
