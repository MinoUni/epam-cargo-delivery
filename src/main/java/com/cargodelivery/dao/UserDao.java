package com.cargodelivery.dao;

import com.cargodelivery.dao.entity.User;
import com.cargodelivery.exception.DBException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserDao {

    /**
     * Save details about new user into
     * database
     *
     * @param user {@link User} data-object with
     *             basic details
     * @throws DBException Sql error
     */
    void create(User user) throws DBException;

    /**
     * Search user into database by user
     * login(id)
     *
     * @param user {@link User} data-object with
     *             basic details for search
     * @return Optional of user data-object
     * @throws DBException Sql error
     */
    Optional<User> findById(User user) throws DBException;

    /**
     * Verify that user already exist
     * in database
     *
     * @param user {@link User} data-object with base
     *             details needed for search
     * @return Boolean.TRUE when user exists
     * @throws DBException Sql error
     */
    boolean isExist(User user) throws DBException;

    /**
     * Search for user password hash in
     * database by user login(id)
     *
     * @param user {@link User} data-object with base
     *             details needed for search
     * @return user password hash
     * @throws DBException Sql error
     */
    String getPasswordById(User user) throws DBException;

    /**
     * Read all information about all users that
     * were saved in database and return it as list
     *
     * @return List of all users saved in database
     * @throws DBException Sql error
     */
    List<User> readAllUsers() throws DBException;

    /**
     * Search for user's money balance in
     * database by user login(id)
     *
     * @param user {@link User} data-object with base
     *             details needed for search
     * @return {@link User} money balance
     * @throws DBException Sql error
     */
    BigDecimal getUserBalance(User user) throws DBException;

    /**
     * Search for user's money balance in
     * database by user login(id) and update it
     *
     * @param user {@link User} data-object with base
     *             details needed for search
     * @throws DBException Sql error
     */
    void updateUserBalance(User user, BigDecimal userBalance) throws DBException;
}
