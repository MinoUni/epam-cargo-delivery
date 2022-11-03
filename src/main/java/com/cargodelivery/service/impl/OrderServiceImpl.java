package com.cargodelivery.service.impl;

import com.cargodelivery.dao.CargoDao;
import com.cargodelivery.dao.OrderDao;
import com.cargodelivery.dao.UserDao;
import com.cargodelivery.dao.entity.Cargo;
import com.cargodelivery.dao.entity.Order;
import com.cargodelivery.dao.entity.OrderState;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.exception.DBException;
import com.cargodelivery.exception.OrderServiceException;
import com.cargodelivery.service.OrderService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);

    private final OrderDao orderRepository;

    private final CargoDao cargoRepository;

    private final UserDao userRepository;

    private final int recordsPerPage = 5;

    public OrderServiceImpl(CargoDao cargoRepository, OrderDao orderRepository, UserDao userRepository) {
        this.cargoRepository = cargoRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }


    @Override
    public List<Order> findAllOrders() throws OrderServiceException {
        try {
            return orderRepository.findAllOrders();
        } catch (DBException e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            throw new OrderServiceException(e);
        }
    }

    @Override
    public void saveOrder(Order order) throws OrderServiceException {
        try {
            orderRepository.save(order);
        } catch (DBException e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            throw new OrderServiceException(e);
        }
    }

    @Override
    public List<Order> findAllUserOrders(User user) throws OrderServiceException {
        try {
            if (!userRepository.isExist(user)) {
                logger.log(Level.ERROR, "User not exist");
                throw new OrderServiceException("User not exist");
            }
            return orderRepository.findAllOrdersByUserId(user);
        } catch (DBException e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            throw new OrderServiceException(e);
        }
    }

    @Override
    public Cargo findCargo(String cargoId) throws OrderServiceException {
        int parsedCargoId = parseString(cargoId);
        try {
            if (!cargoRepository.isExist(parsedCargoId)) {
                logger.log(Level.ERROR, String.format("Cargo with cargoId=[%s] not exist", cargoId));
                throw new OrderServiceException(String.format("Cargo with cargoId=[%s] not exist", cargoId));
            }
            return cargoRepository.findById(parsedCargoId).get();
        } catch (DBException e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            throw new OrderServiceException(e);
        }

    }

    @Override
    public void deleteOrder(String orderId, String cargoId) throws OrderServiceException {
        var parsedOrderId = parseString(orderId);
        var parsedCargoId = parseString(cargoId);
        try {
            if (!orderRepository.isExist(parsedOrderId)) {
                throw new OrderServiceException(String.format("Order with orderId=[%s] not exist", orderId));
            }
            orderRepository.deleteById(parsedOrderId);
            if (orderRepository.isExistOrderWithCargoId(parsedCargoId)) {
                throw new OrderServiceException("There are another order with that cargo=" + cargoId);
            }
            cargoRepository.deleteById(parsedCargoId);
            logger.log(Level.INFO, "Order delete successfully");
        } catch (DBException e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            throw new OrderServiceException(e);
        }
    }

    @Override
    public User payForOrder(String orderId, User user) throws OrderServiceException {
        int id = parseString(orderId);
        try {
            if (!userRepository.isExist(user)) {
                throw new OrderServiceException(String.format("User with login=[%s] not exists", user.getId()));
            }
            if (!orderRepository.isExist(id)) {
                throw new OrderServiceException(String.format("Order with orderId=[%s] not exist", orderId));
            }
            payment(id, user);
            return userRepository.findById(user).get();
        } catch (DBException e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            throw new OrderServiceException(e);
        }
    }

    @Override
    public void updateOrderState(String orderId, OrderState orderState) throws OrderServiceException {
        int id = parseString(orderId);
        try {
            if (!orderRepository.isExist(id)) {
                throw new OrderServiceException(String.format("Order with orderId=%s not exist", orderId));
            }
            orderRepository.updateOrderState(id, orderState);
        } catch (DBException e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            throw new OrderServiceException(e);
        }
    }

    @Override
    public List<Order> getOrdersLimit(String page) throws OrderServiceException {
        List<Order> orders;
        int curPage = 1;
        if (parseString(page) != 1) {
            curPage = parseString(page);
        }
        try {
             orders = orderRepository.findOrders(((curPage - 1) * recordsPerPage), recordsPerPage);
        } catch (DBException e) {
            throw new OrderServiceException(e);
        }
        return orders;
    }

    @Override
    public int getNumbOfPages() throws OrderServiceException{
        try {
            int numbOfRecords = orderRepository.getNumbOfRecords();
            return (int) Math.ceil(numbOfRecords * 1.0 / recordsPerPage);
        } catch (DBException e) {
            throw new OrderServiceException(e);
        }
    }

    private void payment(int orderId, User user) throws OrderServiceException, DBException {
        var userBalance = userRepository.getUserBalance(user);
        var orderPrice = orderRepository.findOrderPrice(orderId);
        if (userBalance.subtract(orderPrice).doubleValue() < 0) {
            throw new OrderServiceException("Low balance");
        }
        userRepository.updateUserBalance(user, userBalance.subtract(orderPrice));
        orderRepository.updateOrderState(orderId, OrderState.PAID);
    }

    @Override
    public Cargo saveCargo(Cargo cargo) throws OrderServiceException {
        try {
            return cargoRepository.save(cargo);
        } catch (DBException e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            throw new OrderServiceException(e.getMessage());
        }
    }

    private int parseString(String param) throws OrderServiceException {
        try {
            return Integer.parseInt(param);
        }  catch (NumberFormatException e) {
            logger.log(Level.ERROR, String.format("Invalid to parse param=%s", param));
            throw new OrderServiceException(String.format("Invalid to parse param=%s", param));
        }
    }
}
