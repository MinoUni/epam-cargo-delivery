package com.cargodelivery.service;

import com.cargodelivery.dao.entity.User;
import com.cargodelivery.exception.UserServiceException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface UserService {

    /**
     * Find user in database by user login(id) and return as User model
     *
     * @param user model object of registration form page
     * @return User object or empty Optional
     * @throws UserServiceException error
     */
    Optional<User> findUser(User user) throws UserServiceException;

    /**
     * Request parameters from registration form validation
     * Checks parameter for null & blank value by parameter name
     *
     * @param param   parameter name of request
     * @param request Http request from registration page
     * @return parameter value
     * @throws IllegalArgumentException when parameter is null or blank
     */
    String checkRequestParam(HttpServletRequest request, String param);

    /**
     * Verify user existence
     * Hash user password
     * Add user into database
     *
     * @param user model object of registration form page
     * @throws UserServiceException when problem with insert user data into database or
     *                              password hashing problem occurred
     */
    void signup(User user) throws UserServiceException;

    User login(User user) throws UserServiceException;
}
