package com.cargodelivery.service;

import com.cargodelivery.dao.entity.Order;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.dao.entity.enums.OrderState;
import com.cargodelivery.exception.OrderServiceException;

import java.util.List;

public interface OrderService {

    /**
     * Select all orders info from database
     * and collect to List
     *
     * @return {@link Order} information objects as ArrayList
     * @throws OrderServiceException when error occur in database or SQL problem
     */
    List<Order> findAllOrders() throws OrderServiceException;

    /**
     * Verify user existence, then get all his orders
     * from database and collect to ArrayList
     *
     * @param user {@link User} data object
     * @return {@link Order} information objects as ArrayList
     * @throws OrderServiceException when user not exists or error occur in database
     */
    List<Order> findAllUserOrders(User user) throws OrderServiceException;

    /**
     * Add a new order in database
     *
     * @param order {@link Order} data object
     * @throws OrderServiceException when error occur in database or SQL problem
     */
    void saveOrder(Order order) throws OrderServiceException;

    /**
     * Verify order existence, then remove it and
     * connected cargo from database
     *
     * @param orderId {@link Order} id
     * @throws OrderServiceException when error occur in database or SQL problem
     */
    void deleteOrder(int orderId) throws OrderServiceException;

    /**
     * Verifies user and order existence, then
     * check user balance for payment process
     * and collect to List
     *
     * @param orderId {@link Order} id
     * @param user {@link User} user data object
     * @return {@link User} updated user info after purchase
     * @throws OrderServiceException when user or order not exists,
     *                               user don't have enough money to pay for order
     */
    User payForOrder(int orderId, User user) throws  OrderServiceException;

    /**
     * Verify order existence and then change their state
     *
     * @param orderId {@link Order} id
     * @param orderState state of the {@link OrderState}
     * @throws OrderServiceException when order not exists in database
     *                               or SQL problem
     */
    void updateState(int orderId, OrderState orderState) throws OrderServiceException;

    List<Order> getOrdersLimit(int page) throws OrderServiceException;

    int getNumbOfPages() throws OrderServiceException;

    Order findOrder(int orderId) throws OrderServiceException;
}
