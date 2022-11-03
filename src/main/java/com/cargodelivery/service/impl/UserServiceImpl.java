package com.cargodelivery.service.impl;

import com.cargodelivery.dao.UserDao;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.exception.DBException;
import com.cargodelivery.exception.UserServiceException;
import com.cargodelivery.security.PasswordEncoder;
import com.cargodelivery.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userRepository;

    public UserServiceImpl(UserDao userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void signup(User user) throws UserServiceException {
        try {
            if (userRepository.isExist(user)) {
                throw new UserServiceException(String.format("User with id=%s already exist", user.getId()));
            }
            user.setPassword(PasswordEncoder.hash(user.getPassword()));
            userRepository.create(user);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOG.error("Failed to hash new user's password", e);
            throw new UserServiceException(e.getMessage());
        } catch (DBException e) {
            throw new UserServiceException(e.getMessage());
        }
    }

    @Override
    public User login(User user) throws UserServiceException {
        try {
            if (!userRepository.isExist(user)) {
                throw new UserServiceException(String.format("User with provided login not exists, login=%s", user.getId()));
            }
            if (!userRepository.getPasswordById(user).equals(PasswordEncoder.hash(user.getPassword()))) {
                throw new UserServiceException("Invalid password. Didn't match with password from db");
            }
            return userRepository.findById(user).get();
        } catch (DBException e) {
            throw new UserServiceException(e.getMessage());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOG.error("Failed to hash login user's password to compare", e);
            throw new UserServiceException(e.getMessage());
        }
    }

    @Override
    public List<User> findAllUsers() throws UserServiceException {
        try {
            return userRepository.readAllUsers();
        } catch (DBException e) {
            throw new UserServiceException(e.getMessage());
        }
    }
}
