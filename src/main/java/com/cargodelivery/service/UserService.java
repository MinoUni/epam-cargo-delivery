package com.cargodelivery.service;

import com.cargodelivery.dao.entity.User;
import com.cargodelivery.exception.UserServiceException;

import java.util.List;

public interface UserService {

    /**
     * Verify user existence
     * Hash user password
     * Add user into database
     *
     * @param user {@link User} model object of registration form page
     * @throws UserServiceException when that user data are already in database
     *                              or error with password hashing function
     */
    void signup(User user) throws UserServiceException;

    /**
     * Verify user existence
     * Compare input password with user's hashed password
     *
     * @param user {@link User} model contains user's id and password
     * @return {@link User} information object
     * @throws UserServiceException when user not exists in database or
     *                              password didn't match with hash
     */
    User login(User user) throws UserServiceException;

    /**
     * Select all users info from database
     * and collect to List
     *
     * @return {@link User} information objects as ArrayList
     * @throws UserServiceException when error occur in database or
     *                              SQL problem
     */
    List<User> findAllUsers(int page) throws UserServiceException;

    /**
     * Count number of unique records in orders table
     *
     * @return number of unique records
     * @throws UserServiceException error occur in database
     */
    int getNumbOfPages() throws UserServiceException;

    /**
     * Look for details about user in database by user login
     *
     * @param user {@link User} DTO subclass of {@link com.cargodelivery.dao.entity.Model}
     * @return {@link User} DTO subclass of {@link com.cargodelivery.dao.entity.Model}
     * @throws UserServiceException error occur in database
     */
    User findUser(User user) throws UserServiceException;

    /**
     * Updates balance for user
     *
     * @param user {@link User} DTO subclass of {@link com.cargodelivery.dao.entity.Model}
     * @throws UserServiceException error occur in database
     */
    void addBalance(User user) throws UserServiceException;
}
