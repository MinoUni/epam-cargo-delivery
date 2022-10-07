package com.cargodelivery.service.impl;

import com.cargodelivery.dao.UserDao;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.dao.impl.UserDaoImpl;
import com.cargodelivery.exception.DBException;
import com.cargodelivery.exception.UserServiceException;
import com.cargodelivery.security.PasswordEncoder;
import com.cargodelivery.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserDao userRepository = new UserDaoImpl();

    private static final Logger logger = LogManager.getLogger();

    @Override
    public Optional<User> findUser(User user) {
        return Optional.empty();
    }

    @Override
    public String checkRequestParam(HttpServletRequest request, String param) throws IllegalArgumentException {
        String result = request.getParameter(param);
        if (result == null || result.isBlank()) {
            logger.log(Level.ERROR, String.format("Request param Blank or null par=%s res=%s", param, result));
            throw new IllegalArgumentException(String.format("Request param Blank or null par=%s res=%s", param, result));
        }
        return result;
    }

    @Override
    public void signup(User user) throws UserServiceException {
        try {
            if (!userRepository.isExist(user)) {
                user.setPassword(PasswordEncoder.hash(user.getPassword()));
                userRepository.create(user);
            } else {
                logger.log(Level.ERROR, "User already exist");
                throw new UserServiceException("User already exist");
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.log(Level.ERROR, "Failed to hash password", e);
            throw new UserServiceException(e);
        } catch (DBException e) {
            logger.log(Level.ERROR, "Failed to signup new User", e);
            throw new UserServiceException(e);
        }
    }

    @Override
    public User login(User user) throws UserServiceException {
        Optional<User> loggedUser = Optional.empty();
        try {
            if (userRepository.isExist(user) &&
                    userRepository.getPasswordById(user).equals(PasswordEncoder.hash(user.getPassword()))) {
                loggedUser = userRepository.findById(user);
            }
        } catch (DBException e) {
            logger.log(Level.ERROR, "Failed to login a user", e);
            throw new UserServiceException(e);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.log(Level.ERROR, "Passwords didn't match", e);
            throw new UserServiceException(e);
        }

        if (loggedUser.isEmpty()) {
            throw new UserServiceException("No such user was found");
        }
        return loggedUser.get();
    }
}
