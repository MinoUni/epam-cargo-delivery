package com.cargodelivery.dao;

import com.cargodelivery.dao.entity.Order;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.exception.DBException;

import java.util.List;

/*
 * Interface to define special functionality for work with orders table
 */
public interface OrderDao extends GenericDAO<Order, Integer> {

    List<Order> findAllOrders() throws DBException;

    List<Order> findUserOrders(User user) throws DBException;

    void deleteById(int orderId) throws DBException;

}
