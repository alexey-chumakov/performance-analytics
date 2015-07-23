package com.ghx.hackaton.analytics.service.order;

public class Order {
    private String sortField;
    private boolean ascending;

    public Order() {
    }

    public Order(String sortField, boolean ascending) {
        this.sortField = sortField;
        this.ascending = ascending;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }
}
