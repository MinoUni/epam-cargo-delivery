package com.cargodelivery.dao.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Order {

    private int id;
    private final String userId;

    private final int cargoId;

    private final BigDecimal price;

    private final String routeStart;

    private final String routeEnd;

    private final Date registrationDate;

    private final Date deliveryDate;

    private final OrderState state;

    public Order(String userId,
                 int cargoId,
                 BigDecimal price,
                 String routeStart,
                 String routeEnd,
                 Date registrationDate,
                 Date deliveryDate,
                 OrderState state) {
        this.userId = userId;
        this.cargoId = cargoId;
        this.price = price;
        this.routeStart = routeStart;
        this.routeEnd = routeEnd;
        this.registrationDate = registrationDate;
        this.deliveryDate = deliveryDate;
        this.state = state;
    }

    public Order(int id,
                 String userId,
                 int cargoId,
                 BigDecimal price,
                 String routeStart,
                 String routeEnd,
                 Date registrationDate,
                 Date deliveryDate,
                 OrderState state) {
        this.id = id;
        this.userId = userId;
        this.cargoId = cargoId;
        this.price = price;
        this.routeStart = routeStart;
        this.routeEnd = routeEnd;
        this.registrationDate = registrationDate;
        this.deliveryDate = deliveryDate;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public int getCargoId() {
        return cargoId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getRouteStart() {
        return routeStart;
    }

    public String getRouteEnd() {
        return routeEnd;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public OrderState getState() {
        return state;
    }

    @Override
    public String toString() {
        return String.format("Order{id=%d, user=%s, cargo=%d, price=%.2f, rS=%s, rE=%s, regDate=%s, delDate=%s, state=%s}",
                id, userId, cargoId, price, routeStart, routeEnd, registrationDate, deliveryDate, state.toString());
    }
}
