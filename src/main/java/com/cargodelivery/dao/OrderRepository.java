package com.cargodelivery.dao;

import com.cargodelivery.dao.entity.Order;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.exception.DBException;

import java.util.List;

/*
 * Interface to define special functionality for work with orders table
 */
public interface OrderRepository extends GenericDAO<Order, Integer> {

    /**
     * Select all orders info from database
     * and collect to List
     *
     * @return {@link Order} DTOs as list
     * @throws DBException when error occur in database or SQL problem
     */
    List<Order> findAllOrders() throws DBException;

    /**
     * Verify user existence, then get all his orders
     * from database and collect to ArrayList
     *
     * @param user {@link User} DTO
     * @return {@link Order} DTOs as list
     * @throws DBException error occur in database
     */
    List<Order> findUserOrders(User user) throws DBException;

    /**
     * Verify order existence, then remove it and
     * connected cargo from database
     *
     * @param orderId {@link Order} id
     * @throws DBException when error occur in database or SQL problem
     */
    void deleteById(int orderId) throws DBException;

}
