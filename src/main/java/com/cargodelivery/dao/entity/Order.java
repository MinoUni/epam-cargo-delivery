package com.cargodelivery.dao.entity;

import com.cargodelivery.dao.entity.enums.OrderState;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class Order extends Model {

    private int userId;
    private String route;
    private Cargo cargo;
    private Date registrationDate;
    private Date deliveryDate;
    private OrderState state;
    private BigDecimal price;

    public Order(int userId, String route, Cargo cargo, Date registrationDate, OrderState state, BigDecimal price) {
        this.userId = userId;
        this.route = route;
        this.cargo = cargo;
        this.registrationDate = registrationDate;
        this.state = state;
        this.price = price;
    }

    public Order(int id, int userId, String route, Cargo cargo, Date registrationDate, Date deliveryDate, OrderState state, BigDecimal price) {
        this.setId(id);
        this.userId = userId;
        this.route = route;
        this.cargo = cargo;
        this.registrationDate = registrationDate;
        this.deliveryDate = deliveryDate;
        this.state = state;
        this.price = price;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return userId == order.userId && Objects.equals(route, order.route) && Objects.equals(cargo, order.cargo) && Objects.equals(registrationDate, order.registrationDate) && Objects.equals(deliveryDate, order.deliveryDate) && state == order.state && Objects.equals(price, order.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, route, cargo, registrationDate, deliveryDate, state, price);
    }

    @Override
    public String toString() {
        return String.format("Order{id=%d, userId=%d, route=%s, cargo=%s, registrationDate=%s, deliveryDate=%s, state=%s, price=%.2f}", getId(), userId, route, cargo, registrationDate, deliveryDate, state.toString(), price);
    }
}
