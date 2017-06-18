package com.accenture.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Entity class for orders
 */
@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer orderId;

    private Integer accountId;
    private String addressee;
    private String address;
    private BigDecimal cost;
    private String date;

    /**
     * Order class constructor
     */
    public Order() {}

    /**
     * Order class constructor
     * @param accountId account id associated to the order
     * @param addressee addresse of the order
     * @param address address of the order
     * @param cost total cost of the order
     * @param date date of the order
     */
    public Order(Integer accountId, String addressee, String address, BigDecimal cost, String date) {
        this.accountId = accountId;
        this.addressee = addressee;
        this.address = address;
        this.cost = cost;
        this.date = date;
    }

    public Integer getOrderId() {
        return orderId;
    }
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getAccountId() {
        return accountId;
    }
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getAddressee() {
        return addressee;
    }
    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getCost() {
        return cost;
    }
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", accountId=" + accountId +
                ", addressee='" + addressee + '\'' +
                ", address='" + address + '\'' +
                ", cost=" + cost +
                ", date=" + date +
                '}';
    }
}
