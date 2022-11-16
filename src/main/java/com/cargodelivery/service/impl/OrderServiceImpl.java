package com.cargodelivery.service.impl;

import com.cargodelivery.dao.OrderDao;
import com.cargodelivery.dao.UserRepository;
import com.cargodelivery.dao.entity.Order;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.dao.entity.enums.OrderState;
import com.cargodelivery.exception.DBException;
import com.cargodelivery.exception.OrderServiceException;
import com.cargodelivery.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);
    private static final String TABLE_NAME = "orders";
    private final OrderDao orderRepository;
    private final UserRepository userRepository;
    private final int recordsPerPage = 5;

    public OrderServiceImpl(OrderDao orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Order> findAllOrders() throws OrderServiceException {
        try {
            return orderRepository.findAllOrders();
        } catch (DBException e) {
            LOG.error(e.getMessage(), e);
            throw new OrderServiceException(e.getMessage());
        }
    }

    @Override
    public void saveOrder(Order order) throws OrderServiceException {
        try {
            orderRepository.save(order);
        } catch (DBException e) {
            LOG.error(e.getMessage(), e);
            throw new OrderServiceException(e.getMessage());
        }
    }

    @Override
    public List<Order> findAllUserOrders(User user) throws OrderServiceException {
        try {
            if (!userRepository.isExist(user)) {
                LOG.warn("Failed to find user={}", user.getLogin());
                throw new OrderServiceException("User not exist");
            }
            return orderRepository.findUserOrders(user);
        } catch (DBException e) {
            LOG.error(e.getMessage(), e);
            throw new OrderServiceException(e.getMessage());
        }
    }

    @Override
    public void deleteOrder(int orderId) throws OrderServiceException {
        try {
            if (orderRepository.findByField(orderId).isEmpty()) {
                LOG.warn("Failed to find order={}", orderId);
                throw new OrderServiceException(String.format("Order with orderId=[%s] not exist", orderId));
            }
            orderRepository.deleteById(orderId);
            LOG.info("Successfully deleted order with id={}", orderId);
        } catch (DBException e) {
            LOG.error(e.getMessage(), e);
            throw new OrderServiceException(e.getMessage());
        }
    }

    @Override
    public User payForOrder(int orderId, User user) throws OrderServiceException {
        try {
            Optional<User> userDetails = getUserAfterPayment(orderId, user);
            if (userDetails.isEmpty()) {
                LOG.warn("Failed to find user={}", user.getLogin());
                throw new OrderServiceException(String.format("User=%s not exists", user.getLogin()));
            }
            return userDetails.get();
        } catch (DBException e) {
            LOG.error(e.getMessage(), e);
            throw new OrderServiceException(e.getMessage());
        }
    }

    private Optional<User> getUserAfterPayment(int orderId, User user) throws DBException, OrderServiceException {
        Optional<User> userDetails = userRepository.findByField(user.getLogin());
        Optional<Order> orderDetails = orderRepository.findByField(orderId);

        if (userDetails.isEmpty()) {
            LOG.warn("Failed to find user={}", user.getLogin());
            throw new OrderServiceException(String.format("User=%s not exists", user.getLogin()));
        }
        if (orderDetails.isEmpty()) {
            LOG.warn("Failed to find order={}", orderId);
            throw new OrderServiceException(String.format("Order=%d not exist", orderId));
        }
        if (!orderDetails.get().getState().equals(OrderState.WAITING_FOR_PAYMENT)) {
            LOG.error("Invalid state={} for order={}", orderDetails.get().getState().toString(), orderId);
            throw new OrderServiceException(String.format("Invalid state=%s for order=%d", orderDetails.get().getState().toString(), orderId));
        }
        var balance = userDetails.get().getBalance();
        var price = orderDetails.get().getPrice();

        var balanceAfterPayment = balance.subtract(price);

        if (balanceAfterPayment.doubleValue() < 0) {
            LOG.warn("User={} balance too low to make a purchase", user.getLogin());
            throw new OrderServiceException(String.format("User=%s balance too low to make a purchase", user.getLogin()));
        }
        userDetails.get().setBalance(balanceAfterPayment);
        orderDetails.get().setState(OrderState.PAID);

        userRepository.update(userDetails.get());
        orderRepository.update(orderDetails.get());

        return userRepository.findByField(userDetails.get().getLogin());
    }

    @Override
    public void updateState(int orderId, OrderState orderState) throws OrderServiceException {
        try {
            var order = findOrder(orderId);
            order.setState(orderState);
            orderRepository.update(order);
        } catch (DBException e) {
            LOG.error(e.getMessage(), e);
            throw new OrderServiceException(e.getMessage());
        }
    }

    @Override
    public List<Order> getOrdersLimit(int page) throws OrderServiceException {
        try {
             return orderRepository.findAllBetween(((page - 1) * recordsPerPage), recordsPerPage);
        } catch (DBException e) {
            LOG.error(e.getMessage(), e);
            throw new OrderServiceException(e.getMessage());
        }
    }

    @Override
    public int getNumbOfPages() throws OrderServiceException{
        try {
            int numbOfRecords = orderRepository.countNumbOfRecords(TABLE_NAME);
            return (int) Math.ceil(numbOfRecords * 1.0 / recordsPerPage);
        } catch (DBException e) {
            throw new OrderServiceException(e);
        }
    }

    @Override
    public Order findOrder(int orderId) throws OrderServiceException {
        try {
            Optional<Order> order = orderRepository.findByField(orderId);
            if (order.isEmpty()) {
                throw new OrderServiceException("No order was found in db");
            }
            return order.get();
        } catch (DBException e) {
            LOG.error(e.getMessage(), e);
            throw new OrderServiceException(e);
        }
    }

}
