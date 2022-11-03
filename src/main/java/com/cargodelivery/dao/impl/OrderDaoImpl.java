package com.cargodelivery.dao.impl;

import com.cargodelivery.dao.HikariCP;
import com.cargodelivery.dao.OrderDao;
import com.cargodelivery.dao.entity.Order;
import com.cargodelivery.dao.entity.OrderState;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.exception.DBException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {

    private static final Logger LOG = LogManager.getLogger(OrderDaoImpl.class);

    @Override
    public void save(Order order) throws DBException {
        String addOrderSql = "INSERT INTO `order` VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String getLastOrderIdSql = "SELECT id FROM `order` ORDER BY id desc LIMIT 1;";
        int id = 0;
        int i = 0;
        try (var connection = HikariCP.getHikariConnection()) {
            try (var prepStatement = connection.prepareStatement(addOrderSql)) {
                ResultSet resultSet = prepStatement.executeQuery(getLastOrderIdSql);
                while (resultSet.next()) {
                    id = resultSet.getInt("id");
                }
                prepStatement.setInt(++i, ++id);
                prepStatement.setString(++i, order.getUserId());
                prepStatement.setInt(++i, order.getCargoId());
                prepStatement.setBigDecimal(++i, order.getPrice());
                prepStatement.setString(++i, order.getRouteStart());
                prepStatement.setString(++i, order.getRouteEnd());
                prepStatement.setDate(++i,  new Date(order.getRegistrationDate().getTime()));
                prepStatement.setDate(++i, new Date(order.getDeliveryDate().getTime()));
                prepStatement.setString(++i, order.getState().toString());

                prepStatement.executeUpdate();
                LOG.log(Level.INFO, "Order created successfully!");
            }
        } catch (SQLException e) {
            LOG.log(Level.ERROR, "Failed to create a new order", e);
            throw new DBException("Failed to create a new order", e);
        }
    }

    @Override
    public List<Order> findAllOrders() throws DBException {
        String sql = "SELECT * FROM `order`";
        List<Order> orders = new ArrayList<>(5);
        try (var connection = HikariCP.getHikariConnection()) {
            try (var preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    orders.add(build(resultSet));
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
    public List<Order> findAllOrdersByUserId(User user) throws DBException {
        String sql = "SELECT * FROM `order` WHERE user_id = ?";
        List<Order> userOrders = new ArrayList<>(5);
        try (var connection = HikariCP.getHikariConnection()) {
            try (var preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getId());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    userOrders.add(build(resultSet));
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.ERROR, "Failed to read all user orders from database!", e);
            throw new DBException("Failed to read all user orders from database!", e);
        }
        LOG.log(Level.INFO, "Successfully read all user orders from database!");
        return userOrders;
    }

    @Override
    public boolean isExist(int orderId) throws DBException {
        String sql = "SELECT EXISTS(SELECT id FROM `order` WHERE id = ?)";
        boolean isOrderExist = Boolean.FALSE;
        try (var connection = HikariCP.getHikariConnection()) {
            try (var preparedStatement = connection.prepareStatement(sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                preparedStatement.setInt(1, orderId);
                var resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    isOrderExist = resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.ERROR, String.format("Failed to check order existence with orderId=%d", orderId), e);
            throw new DBException(String.format("Failed to check order existence with orderId=%d", orderId), e);
        }
        LOG.log(Level.INFO, String.format("Successfully check order existence with orderId=%d", orderId));
        return isOrderExist;
    }

    @Override
    public boolean isExistOrderWithCargoId(int cargoId) throws DBException {
        String sql = "SELECT EXISTS(SELECT id FROM `order` WHERE cargo_id = ?)";
        boolean isExist = Boolean.FALSE;
        try (var connection = HikariCP.getHikariConnection()) {
            try (var preparedStatement = connection.prepareStatement(sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                preparedStatement.setInt(1, cargoId);
                var resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    isExist = resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.ERROR, String.format("Failed to check order existence with cargoId=%d", cargoId), e);
            throw new DBException(String.format("Failed to check order existence with cargoId=%d", cargoId), e);
        }
        LOG.log(Level.INFO, String.format("Successfully check order existence with cargoId=%d", cargoId));
        return isExist;
    }

    @Override
    public void deleteById(int orderId) throws DBException {
        String sql = "DELETE FROM `order` WHERE id = ?;";
        try (var connection = HikariCP.getHikariConnection()) {
            try (var preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, orderId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.log(Level.ERROR, String.format("Failed to delete order with orderId=%d", orderId), e);
            throw new DBException(String.format("Failed to delete order with orderId=%d", orderId), e);
        }
        LOG.log(Level.INFO, String.format("Successfully delete order with orderId=%d", orderId));
    }

    @Override
    public BigDecimal findOrderPrice(int id) throws DBException {
        String sql = "SELECT price FROM `order` WHERE id = ?";
        BigDecimal orderPrice = null;
        try (var connection = HikariCP.getHikariConnection()) {
            try (var preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    orderPrice = resultSet.getBigDecimal("price");
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.ERROR, "Failed to get order's price", e);
            throw new DBException("Failed to get order's price", e);
        }
        LOG.log(Level.INFO, "Successfully get order's price");
        return orderPrice;
    }

    @Override
    public void updateOrderState(int orderId, OrderState orderState) throws DBException {
        String sql = "UPDATE `order` SET state = ? WHERE id = ?;";
        int i = 0;
        try (var connection = HikariCP.getHikariConnection()) {
            try (var preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(++i, orderState.toString());
                preparedStatement.setInt(++i, orderId);
                preparedStatement.executeUpdate();
                LOG.log(Level.INFO, "Successfully update order's state in database");
            }
        } catch (SQLException e) {
            LOG.log(Level.ERROR, "Failed to update order's state in database", e);
            throw new DBException("Failed to update order's state in database", e);
        }
    }

    @Override
    public List<Order> findOrders(int start, int numbOfRecords) throws DBException {
        List<Order> orders = new ArrayList<>(numbOfRecords);
        String sql = "SELECT * FROM `order` LIMIT ?, ?;";
        try (var conn = HikariCP.getHikariConnection()) {
            try (var prepStatement = conn.prepareStatement(sql)) {
                prepStatement.setInt(1, start);
                prepStatement.setInt(2, numbOfRecords);
                ResultSet resSet = prepStatement.executeQuery();
                while (resSet.next()) {
                    orders.add(build(resSet));
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed to obtain orders LIMIT", e);
            throw new DBException("Failed to obtain orders LIMIT", e);
        }
        return orders;
    }

    @Override
    public int getNumbOfRecords() throws DBException {
        int numbOfRecords = 0;
        String sql = "SELECT COUNT(id) AS 'id' FROM `order`;";
        try (var conn = HikariCP.getHikariConnection()) {
            try (var prepStatement = conn.prepareStatement(sql)) {
                ResultSet resSet = prepStatement.executeQuery();
                while (resSet.next()) {
                    numbOfRecords = resSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed to receive number of records table [order]", e);
            throw new DBException("Failed to receive number of records table [order]", e);
        }
        return numbOfRecords;
    }

    private Order build(ResultSet resultSet) throws SQLException {
        return new Order(
                resultSet.getInt("id"),
                resultSet.getString("user_id"),
                resultSet.getInt("cargo_id"),
                resultSet.getBigDecimal("price"),
                resultSet.getString("route_start"),
                resultSet.getString("route_end"),
                resultSet.getDate("date_departure"),
                resultSet.getDate("date_arrival"),
                OrderState.valueOf(resultSet.getString("state"))
        );
    }
}
