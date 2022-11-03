package com.cargodelivery.dao;

import com.cargodelivery.dao.entity.Order;
import com.cargodelivery.dao.entity.OrderState;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.exception.DBException;

import java.math.BigDecimal;
import java.util.List;

public interface OrderDao {

    void save(Order order) throws DBException;

    List<Order> findAllOrders() throws DBException;

    List<Order> findAllOrdersByUserId(User user) throws DBException;

    
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean isExist(int orderId) throws DBException;

    boolean isExistOrderWithCargoId(int cargoId) throws DBException;

    void deleteById(int orderId) throws DBException;

    BigDecimal findOrderPrice(int id) throws DBException;

    void updateOrderState(int orderId, OrderState orderState) throws DBException;

    List<Order> findOrders(int start, int numbOfRecords) throws DBException;

    int getNumbOfRecords() throws DBException;
}
