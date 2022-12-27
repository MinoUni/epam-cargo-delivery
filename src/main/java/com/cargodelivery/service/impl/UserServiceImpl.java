package com.cargodelivery.service.impl;

import com.cargodelivery.dao.UserRepository;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.exception.DBException;
import com.cargodelivery.exception.UserServiceException;
import com.cargodelivery.security.PasswordEncoder;
import com.cargodelivery.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String TABLE_NAME = "users";

    private final UserRepository userRepository;
    private final int recordsPerPage = 5;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void signup(User user) throws UserServiceException {
        try {
            if (userRepository.isExist(user)) {
                throw new UserServiceException(String.format("User with id=%s already exist", user.getId()));
            }
            user.setPassword(PasswordEncoder.hash(user.getPassword()));
            userRepository.save(user);
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
                throw new UserServiceException(String.format("User with provided login not exists, login=%s", user.getLogin()));
            }
            User userData = userRepository.findByField(user.getLogin()).get();
            if (!userData.getPassword().equals(PasswordEncoder.hash(user.getPassword()))) {
                throw new UserServiceException("Invalid password. Didn't match with password from db");
            }
            return userData;
        } catch (DBException e) {
            throw new UserServiceException(e.getMessage());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOG.error("Failed to hash login user's password to compare", e);
            throw new UserServiceException(e.getMessage());
        }
    }

    @Override
    public List<User> findAllUsers(int page) throws UserServiceException {
        try {
            return userRepository.findAllBetween(((page - 1) * recordsPerPage), recordsPerPage);
        } catch (DBException e) {
            throw new UserServiceException(e.getMessage());
        }
    }

    @Override
    public int getNumbOfPages() throws UserServiceException {
        try {
            int numbOfRecords = userRepository.countNumbOfRecords(TABLE_NAME);
            return (int) Math.ceil(numbOfRecords * 1.0 / recordsPerPage);
        } catch (DBException e) {
            throw new UserServiceException(e.getMessage());
        }
    }

    @Override
    public User findUser(User user) throws UserServiceException {
        try {
            Optional<User> userDetails = userRepository.findByField(user.getLogin());
            if (userDetails.isEmpty()) {
                LOG.warn("Failed to find user={}", user.getLogin());
                throw new UserServiceException(String.format("User=%s not exists", user.getLogin()));
            }
            return userDetails.get();
        } catch (DBException e) {
            LOG.error(e.getMessage(), e);
            throw new UserServiceException(e.getMessage());
        }
    }

    @Override
    public void addBalance(User user) throws UserServiceException {
        try {
            user.setBalance(user.getBalance().add(BigDecimal.valueOf(10_000)));
            userRepository.update(user);
        } catch (DBException e) {
            LOG.error(e.getMessage(), e);
            throw new UserServiceException(e.getMessage());
        }
    }
}
