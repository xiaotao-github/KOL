package com.recharge_cash.dto;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class GiftRecord {
    //日期
    Date date;
    //订单号
    String order_number;
    //用户ID
    String id;
    //用户姓名
    String username;
    //订单数量
    int order_amount;
    //订单单价
    Double order_price;
    //订单金额
    Double order_money;
    //状态
    int state;
    //X币余额
    Double x_balance;
    //账号金额
    Double account_balance;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(int order_amount) {
        this.order_amount = order_amount;
    }

    public Double getOrder_price() {
        return order_price;
    }

    public void setOrder_price(Double order_price) {
        this.order_price = order_price;
    }

    public Double getOrder_money() {
        return order_money;
    }

    public void setOrder_money(Double order_money) {
        this.order_money = order_money;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Double getX_balance() {
        return x_balance;
    }

    public void setX_balance(Double x_balance) {
        this.x_balance = x_balance;
    }

    public Double getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(Double account_balance) {
        this.account_balance = account_balance;
    }
}
