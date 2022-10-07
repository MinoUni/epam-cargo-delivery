package com.cargodelivery.dao.impl;

import com.cargodelivery.dao.HikariCP;
import com.cargodelivery.dao.UserDao;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.dao.entity.UserRole;
import com.cargodelivery.exception.DBException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    private static final Logger logger = LogManager.getLogger();

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
                logger.log(Level.INFO, "User signup operation was successful");
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Failed to create new User in database", e);
            throw new DBException("Failed to create new User in database", e);
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
                    User userDetails = buildUser(resultSet);
                    result = Optional.of(userDetails);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Failed to find user by his id", e);
            throw new DBException("Failed to find user by his id", e);
        }
        logger.log(Level.INFO, "Successfully read user details from database");
        return result;
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
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

    @Override
    public boolean isExist(User user) throws DBException {
        String sql = "SELECT EXISTS(SELECT id FROM user WHERE id = ?)";
        boolean isExist = Boolean.FALSE;
        int i = 0;
        try (var connection = HikariCP.getHikariConnection()) {
            try (var preparedStatement = connection.prepareStatement(sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)){
                preparedStatement.setString(++i, user.getId());
                var resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    isExist = resultSet.getBoolean(i);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Failed to check user existing", e);
            throw new DBException("Failed to check user existing", e);
        }
        logger.log(Level.INFO, "Successfully execute isUserExist query");
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
            logger.log(Level.ERROR, "Failed to get user password", e);
            throw new DBException("Failed to get user password", e);
        }
        logger.log(Level.INFO, "Successfully get user password");
        return password;
    }


}
