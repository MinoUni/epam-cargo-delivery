package com.cargodelivery.dao.impl;

import com.cargodelivery.dao.ConnPool;
import com.cargodelivery.dao.UserRepository;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.dao.entity.enums.UserRole;
import com.cargodelivery.exception.DBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepoImpl implements UserRepository {

    private static final Logger LOG = LoggerFactory.getLogger(UserRepoImpl.class);

    @Override
    public void save(User user) throws DBException {
        String sql = "INSERT INTO users(login, name, surname, email, password, registration_date, role) VALUES(?, ?, ?, ?, ?, ?, ?);";
        try (var connection = ConnPool.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(sql)) {
                mapFromModel(preparedStatement, user);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.error("Failed to create new User in database user={}", user, e);
            throw new DBException(String.format("Failed to create new User in database user=%s", user), e);
        }
    }

    @Override
    public Optional<User> findByField(String login) throws DBException {
        String sql = "SELECT * FROM users WHERE login = ?";
        Optional<User> user = Optional.empty();
        try (var connection = ConnPool.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, login);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    user = Optional.ofNullable(mapToModel(resultSet));
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed to find user by his id, user={}", user, e);
            throw new DBException(String.format("Failed to find user by his id, user=%s", user), e);
        }
        return user;
    }

    @Override
    public List<User> findAllBetween(int start, int numbOfRecords) throws DBException {
        List<User> users = new ArrayList<>(numbOfRecords);
        String sql = "SELECT * FROM users LIMIT ?, ?;";
        try (var conn = ConnPool.getConnection()) {
            try (var prepStatement = conn.prepareStatement(sql)) {
                prepStatement.setInt(1, start);
                prepStatement.setInt(2, numbOfRecords);
                ResultSet resSet = prepStatement.executeQuery();
                while (resSet.next()) {
                    users.add(mapToModel(resSet));
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed to obtain users LIMIT", e);
            throw new DBException("Failed to obtain users LIMIT", e);
        }
        return users;
    }

    @Override
    public void update(User user) throws DBException {
        String sql = "UPDATE `users` SET balance = ? WHERE login = ?;";
        try (var connection = ConnPool.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setBigDecimal(1, user.getBalance());
                preparedStatement.setString(2, user.getLogin());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.error("Failed to update for user={}", user, e);
            throw new DBException(String.format("Failed to update for user=%s", user), e);
        }
    }

    @Override
    public boolean isExist(User user) throws DBException {
        String sql = "SELECT EXISTS(SELECT id FROM users WHERE login = ?)";
        boolean isExist = Boolean.FALSE;
        try (var connection = ConnPool.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                preparedStatement.setString(1, user.getLogin());
                var resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    isExist = resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed to check user existing, user={}", user, e);
            throw new DBException(String.format("Failed to check user existing, user=%s", user), e);
        }
        return isExist;
    }

    @Override
    public User mapToModel(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                resultSet.getString("surname"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getDate("registration_date"),
                UserRole.valueOf(resultSet.getString("role")),
                resultSet.getBigDecimal("balance")
        );
    }

    @Override
    public void mapFromModel(PreparedStatement preparedStatement, User user) throws SQLException {
        System.out.println(new Date(user.getRegistrationDate().getTime()));
        preparedStatement.setString(1, user.getLogin());
        preparedStatement.setString(2, user.getName());
        preparedStatement.setString(3, user.getSurname());
        preparedStatement.setString(4, user.getEmail());
        preparedStatement.setString(5, user.getPassword());
        preparedStatement.setDate(6, new Date(user.getRegistrationDate().getTime()));
        preparedStatement.setString(7, user.getRole().toString());
    }

}
