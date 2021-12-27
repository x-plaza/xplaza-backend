package com.backend.xplaza.model;

public class OrderResponse {
    private String invoice_number;

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    private Double grand_total_price;

    public void setGrand_total_price (Double grand_total_price) {
        this.grand_total_price = grand_total_price;
    }

    public OrderResponse(){}
}
