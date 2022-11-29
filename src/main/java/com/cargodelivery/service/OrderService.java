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
     * @return {@link Order} DTOs as list
     * @throws OrderServiceException when error occur in database or SQL problem
     */
    List<Order> findAllOrders() throws OrderServiceException;

    /**
     * Verify user existence, then get all his orders
     * from database and collect to ArrayList
     *
     * @param user {@link User} DTO
     * @return {@link Order} DTOs as list
     * @throws OrderServiceException when user not exists or error occur in database
     */
    List<Order> findAllUserOrders(User user) throws OrderServiceException;

    /**
     * Add a new order in database
     *
     * @param order {@link Order} DTO
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
     * @throws OrderServiceException error occur in database
     */
    void updateState(int orderId, OrderState orderState) throws OrderServiceException;

    /**
     * Get certain amount of records from database(Pagination realization)
     *
     * @param page number of current page
     * @return {@link Order} DTOs as list
     * @throws OrderServiceException error occur in database
     */
    List<Order> getOrdersLimit(int page) throws OrderServiceException;

    /**
     * Count number of unique records in orders table
     *
     * @return number of unique records
     * @throws OrderServiceException error occur in database
     */
    int getNumbOfPages() throws OrderServiceException;

    /**
     * Look for details about order in database by order id
     *
     * @param orderId {@link Order} id of the order
     * @return {@link Order} order DTO
     * @throws OrderServiceException error occur in database
     */
    Order findOrder(int orderId) throws OrderServiceException;
}
