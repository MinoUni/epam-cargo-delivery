package com.cargodelivery.dao.impl;

import com.cargodelivery.dao.ConnPool;
import com.cargodelivery.dao.OrderRepository;
import com.cargodelivery.dao.entity.Cargo;
import com.cargodelivery.dao.entity.Order;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.dao.entity.enums.OrderState;
import com.cargodelivery.exception.DBException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepoImpl implements OrderRepository {

    private static final Logger LOG = LogManager.getLogger(OrderRepoImpl.class);

    @Override
    public void save(Order order) throws DBException {
        String addOrderSql = "INSERT INTO `orders`(users_id, route, registration_date, price, state, length, width, height, weight) VALUE (?,?,?,?,?,?,?,?,?);";
        try (var connection = ConnPool.getConnection()) {
            try (var prepStatement = connection.prepareStatement(addOrderSql)) {
                mapFromModel(prepStatement, order);
                prepStatement.executeUpdate();
                LOG.log(Level.INFO, "Order created successfully!");
            }
        } catch (SQLException e) {
            LOG.log(Level.ERROR, "Failed to create a new order", e);
            throw new DBException("Failed to create a new order", e);
        }
    }

    @Override
    public List<Order> findAllBetween(int start, int numbOfRecords) throws DBException {
        List<Order> orders = new ArrayList<>(numbOfRecords);
        String sql = "SELECT * FROM `orders` LIMIT ?, ?;";
        try (var conn = ConnPool.getConnection()) {
            try (var prepStatement = conn.prepareStatement(sql)) {
                prepStatement.setInt(1, start);
                prepStatement.setInt(2, numbOfRecords);
                ResultSet resSet = prepStatement.executeQuery();
                while (resSet.next()) {
                    orders.add(mapToModel(resSet));
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed to obtain orders LIMIT", e);
            throw new DBException("Failed to obtain orders LIMIT", e);
        }
        return orders;
    }


    @Override
    public Optional<Order> findByField(Integer orderId) throws DBException {
        String sql = "SELECT * FROM orders WHERE id = ?";
        Optional<Order> order = Optional.empty();
        try (var connection = ConnPool.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, orderId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    order = Optional.ofNullable(mapToModel(resultSet));
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed to find order by it id, orderId={}", orderId, e);
            throw new DBException(String.format("Failed to find order by it id, orderId=%d", orderId), e);
        }
        return order;
    }

    @Override
    public List<Order> findAllOrders() throws DBException {
        String sql = "SELECT * FROM `orders`";
        List<Order> orders = new ArrayList<>(5);
        try (var conn = ConnPool.getConnection()) {
            try (var prepStat = conn.prepareStatement(sql)) {
                ResultSet resSet = prepStat.executeQuery();
                while (resSet.next()) {
                    orders.add(mapToModel(resSet));
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.ERROR, "Failed to read all orders from database!", e);
            throw new DBException("Failed to read all orders from database!", e);
        }
        LOG.log(Level.INFO, "Successfully read all orders from database!");
        return orders;
    }

    @Override
    public List<Order> findUserOrders(User user) throws DBException {
        String sql = "SELECT * FROM `orders` WHERE users_id = ?";
        List<Order> orders = new ArrayList<>(5);
        try (var conn = ConnPool.getConnection()) {
            try (var prepStat = conn.prepareStatement(sql)) {
                prepStat.setInt(1, user.getId());
                ResultSet resSet = prepStat.executeQuery();
                while (resSet.next()) {
                    orders.add(mapToModel(resSet));
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.ERROR, "Failed to read all user orders from database!", e);
            throw new DBException("Failed to read all user orders from database!", e);
        }
        LOG.log(Level.INFO, "Successfully read all user orders from database!");
        return orders;
    }

    @Override
    public void deleteById(int orderId) throws DBException {
        String sql = "DELETE FROM `orders` WHERE id = ?;";
        try (var conn = ConnPool.getConnection()) {
            try (var prepStat = conn.prepareStatement(sql)) {
                prepStat.setInt(1, orderId);
                prepStat.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.log(Level.ERROR, String.format("Failed to delete order with orderId=%d", orderId), e);
            throw new DBException(String.format("Failed to delete order with orderId=%d", orderId), e);
        }
        LOG.log(Level.INFO, String.format("Successfully delete order with orderId=%d", orderId));
    }

    @Override
    public void update(Order order) throws DBException {
        String sql = "UPDATE `orders` SET state = ? WHERE id = ?;";
        try (var conn = ConnPool.getConnection()) {
            try (var prepStat = conn.prepareStatement(sql)) {
                prepStat.setString(1, order.getState().toString());
                prepStat.setInt(2, order.getId());
                prepStat.executeUpdate();
                LOG.log(Level.INFO, "Successfully update order's state in database");
            }
        } catch (SQLException e) {
            LOG.log(Level.ERROR, "Failed to update order's state in database", e);
            throw new DBException("Failed to update order's state in database", e);
        }
    }

    @Override
    public Order mapToModel(ResultSet resSet) throws SQLException {
        return new Order(
                resSet.getInt("id"),
                resSet.getInt("users_id"),
                resSet.getString("route"),
                new Cargo(
                        resSet.getDouble("length"),
                        resSet.getDouble("width"),
                        resSet.getDouble("height"),
                        resSet.getDouble("weight")
                ),
                resSet.getDate("registration_date"),
                resSet.getDate("delivery_date"),
                OrderState.valueOf(resSet.getString("state")),
                resSet.getBigDecimal("price")
        );
    }

    @Override
    public void mapFromModel(PreparedStatement prepStat, Order order) throws SQLException {
        prepStat.setInt(1, order.getUserId());
        prepStat.setString(2, order.getRoute());
        prepStat.setDate(3, new Date(order.getRegistrationDate().getTime()));
        prepStat.setBigDecimal(4, order.getPrice());
        prepStat.setString(5, order.getState().toString());
        prepStat.setDouble(6, order.getCargo().getLength());
        prepStat.setDouble(7, order.getCargo().getWidth());
        prepStat.setDouble(8, order.getCargo().getHeight());
        prepStat.setDouble(9, order.getCargo().getHeight());
    }

}
