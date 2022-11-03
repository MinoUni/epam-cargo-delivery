package com.cargodelivery.dao.impl;

import com.cargodelivery.dao.HikariCP;
import com.cargodelivery.dao.UserDao;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.dao.entity.UserRole;
import com.cargodelivery.exception.DBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    private static final Logger LOG = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public void create(User user) throws DBException {
        String sql = "INSERT INTO user(id, name, surname, email, password, role, balance) VALUES(?, ?, ?, ?, ?, ?, ?);";
        int i = 0;
        try (var connection = HikariCP.getHikariConnection()) {
            try (var preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(++i, user.getId());
                preparedStatement.setString(++i, user.getName());
                preparedStatement.setString(++i, user.getSurname());
                preparedStatement.setString(++i, user.getEmail());
                preparedStatement.setString(++i, user.getPassword());
                preparedStatement.setString(++i, user.getUserRole().toString());
                preparedStatement.setBigDecimal(++i, user.getBalance());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.error("Failed to create new User in database user={}", user, e);
            throw new DBException(String.format("Failed to create new User in database user=%s", user), e);
        }
    }

    @Override
    public Optional<User> findById(User user) throws DBException {
        String sql = "SELECT * FROM user WHERE id = ?";
        Optional<User> result = Optional.empty();
        try (var connection = HikariCP.getHikariConnection()) {
            try (var preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getId());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    User userDetails = build(resultSet);
                    result = Optional.of(userDetails);
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed to find user by his id, user={}", user, e);
            throw new DBException(String.format("Failed to find user by his id, user=%s", user), e);
        }
        return result;
    }

    @Override
    public boolean isExist(User user) throws DBException {
        String sql = "SELECT EXISTS(SELECT id FROM user WHERE id = ?)";
        boolean isExist = Boolean.FALSE;
        int i = 0;
        try (var connection = HikariCP.getHikariConnection()) {
            try (var preparedStatement = connection.prepareStatement(sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                preparedStatement.setString(++i, user.getId());
                var resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    isExist = resultSet.getBoolean(i);
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed to check user existing, user={}", user, e);
            throw new DBException(String.format("Failed to check user existing, user=%s", user), e);
        }
        return isExist;
    }

    @Override
    public String getPasswordById(User user) throws DBException {
        String sql = "SELECT password FROM user WHERE id = ?";
        String password = "";
        try (var connection = HikariCP.getHikariConnection()) {
            try (var preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getId());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    password = resultSet.getString("password");
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed to get user password, user={}", user, e);
            throw new DBException(String.format("Failed to get user password, user=%s", user), e);
        }
        return password;
    }

    @Override
    public List<User> readAllUsers() throws DBException {
        String sql = "SELECT * FROM user";
        List<User> users = new ArrayList<>(5);
        try (var connection = HikariCP.getHikariConnection()) {
            try (var preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    users.add(build(resultSet));
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed to read all users from database", e);
            throw new DBException("Failed to read all users from database", e);
        }
        return users;
    }

    @Override
    public BigDecimal getUserBalance(User user) throws DBException {
        String sql = "SELECT balance FROM user WHERE id = ?";
        BigDecimal userBalance = null;
        try (var connection = HikariCP.getHikariConnection()) {
            try (var preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getId());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    userBalance = resultSet.getBigDecimal("balance");
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed to get user's balance, user={}", user, e);
            throw new DBException(String.format("Failed to get user's balance, user=%s", user), e);
        }
        return userBalance;
    }

    @Override
    public void updateUserBalance(User user, BigDecimal userBalance) throws DBException {
        String sql = "UPDATE user SET balance = ? WHERE id = ?;";
        int i = 0;
        try (var connection = HikariCP.getHikariConnection()) {
            try (var preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setBigDecimal(++i, userBalance);
                preparedStatement.setString(++i, user.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.error("Failed to update user's balance in database, user={}", user, e);
            throw new DBException(String.format("Failed to update user's balance in database, user=%s", user), e);
        }
    }

    private User build(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getString("id"),
                resultSet.getString("name"),
                resultSet.getString("surname"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                UserRole.valueOf(resultSet.getString("role")),
                resultSet.getBigDecimal("balance")
        );
    }

}
