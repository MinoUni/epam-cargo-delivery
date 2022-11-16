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

    int getNumbOfPages() throws UserServiceException;

    User findUser(User user) throws UserServiceException;
}
